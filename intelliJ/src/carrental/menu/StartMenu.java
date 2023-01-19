package carrental.menu;

import carrental.databaseconn.Database;
import carrental.menuimpl.CarDaoImpl;
import carrental.menuimpl.CompanyDaoImpl;
import carrental.menuimpl.CustomerDaoImpl;
import carrental.menuobjects.Car;
import carrental.menuobjects.Company;
import carrental.menuobjects.Customer;

import java.util.List;
import java.util.Scanner;

public class StartMenu {
    static Scanner input = new Scanner(System.in);

    CompanyDaoImpl companyDAO;
    CarDaoImpl carDAO;
    CustomerDaoImpl customerDao;

    Database database;

    public StartMenu(String databaseName) {
        database = new Database(databaseName);
        companyDAO = new CompanyDaoImpl(database.getConnection());
        carDAO = new CarDaoImpl(database.getConnection());
        customerDao = new CustomerDaoImpl(database.getConnection());
    }

    public void menuAction() {
        while (true) {
            try {
                System.out.println("""
                        1. Log in as a manager
                        2. Log in as a customer
                        3. Create a customer
                        0. Exit""");
                int option = input.nextInt();
                input.nextLine();
                switch (option) {
                    case 1 -> compMenu();
                    case 2 -> customerMenu(2);
                    case 3 -> customerMenu(3);
                    case 0 -> {
                        database.exit();
                        return;
                    }
                    default -> System.out.println("Please enter a valid number");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private void compMenu() {
        while (true) {
            try {
                System.out.println("""
                        1. Company list
                        2. Create a company
                        0. Back""");
                int option = input.nextInt();
                input.nextLine();
                switch (option) {
                    case 1:
                        List<Company> companies = companyDAO.getAll();
                        if (companies.size() == 0) {
                            System.out.println("The company list is empty!");
                            break;
                        }
                        chooseComp(companies, 0);
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Enter the company name:");
                        String newCompanyName = input.nextLine();
                        companyDAO.addBrand(new Company(newCompanyName));
                        System.out.println("The company was created!");
                        System.out.println();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Please enter a valid number!");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private void chooseComp(List<Company> companies, int customer_id) {
        while (true) {
            try {
                System.out.printf("Choose %s company:\n", customer_id == 0 ? "the" : "a");
                for (Company company : companies) {
                    System.out.println(company.getID() + ". " + company.getName());
                }
                System.out.println("0. Back");
                int comp_num = input.nextInt();
                input.nextLine();
                if (comp_num == 0) {
                    return;
                }
                companyDAO.getBrand(comp_num).ifPresentOrElse(
                        (company) -> carMenu(company, customer_id),
                        () -> System.out.println("Please enter a valid number!")
                );
                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private void carMenu(Company company, int customer_id) {
        if (customer_id > 0) {
            chooseCar(company, customer_id);
            return;
        }
        System.out.printf("'%s' company\n", company.getName());
        while (true) {
            try {
                System.out.println("""
                        1. Car list
                        2. Create a car
                        0. Back""");
                int option = input.nextInt();
                input.nextLine();
                List<Car> cars = carDAO.getCompanySpecific(company.getID());
                switch (option) {
                    case 1:
                        if (cars.size() == 0) {
                            System.out.println("The car list is empty!");
                            break;
                        }
                        int i = 1;
                        for (Car car : cars) {
                            System.out.println(i + ". " + car.getName());
                            i++;
                        }
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Enter the car name:");
                        String newCarName = input.nextLine();
                        carDAO.addBrand(new Car(newCarName, company.getID()));
                        System.out.println("The car was added!");
                        System.out.println();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Please enter a valid number!");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private void chooseCar(Company company, int customer_id) {
        List<Car> cars = carDAO.getCompanySpecific(company.getID());
        try {
            if (cars.size() == 0) {
                System.out.println("The car list is empty!");
                return;
            }
            System.out.println("Choose a car:");
            for (Car car : cars) {
                System.out.println(car.getID() + ". " + car.getName());
            }
            int car_id = input.nextInt();
            input.nextLine();
            if (car_id == 0) {
                return;
            }

            carDAO.getBrand(cars.get(car_id - 1).getID()).ifPresentOrElse(
                    (car) -> {
                        customerDao.updateCar(car, customer_id);
                        System.out.printf("You rented '%s'\n", car.getName());
                    },
                    () -> System.out.println("Please enter a valid number!")
            );
        } catch (Exception e) {
            System.out.println("Please enter a valid number!");
        }
    }

    private void customerMenu(int option) {
        switch (option) {
            case 2:
                List<Customer> customers = customerDao.getAll();
                if (customers.size() == 0) {
                    System.out.println("The customer list is empty!");
                    break;
                }
                chooseCustomer(customers);
                System.out.println();
                break;
            case 3:
                System.out.println("Enter the customer name:");
                String newCustomerName = input.nextLine();
                customerDao.addBrand(new Customer(newCustomerName));
                System.out.println("The customer was added!");
                System.out.println();
                break;
        }
    }

    private void chooseCustomer(List<Customer> customers) {
        while (true) {
            try {
                System.out.println("Choose a customer:");
                for (Customer customer : customers) {
                    System.out.println(customer.getID() + ". " + customer.getName());
                }
                System.out.println("0. Back");
                int customer_num = input.nextInt();
                input.nextLine();
                if (customer_num == 0) {
                    return;
                }
                customerDao.getBrand(customer_num).ifPresentOrElse(
                        customer -> rentActionsMenu(customer.getID()),
                        () -> System.out.println("Please enter a valid number!")
                );
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private void rentActionsMenu(int customer_id) {
        while (true) {
            try {
                System.out.println("""
                        1. Rent a car
                        2. Return a rented car
                        3. My rented car
                        0. Back""");
                int car_id = customerDao.getBrand(customer_id).get().getRentedCarID();
                int option = input.nextInt();
                input.nextLine();
                switch (option) {
                    case 1:
                        if (car_id > 0) {
                            System.out.println("You've already rented a car!");
                        } else {
                            List<Company> companies = companyDAO.getAll();
                            if (companies.size() == 0) {
                                System.out.println("The company list is empty!");
                            } else {
                                chooseComp(companies, customer_id);
                            }
                        }
                        break;
                    case 2:
                        if (car_id == 0) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            customerDao.deleteCar(customer_id);
                            System.out.println("You've returned a rented car!");
                        }
                        break;
                    case 3:
                        if (car_id == 0) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            System.out.println("Your rented car:");
                            carDAO.getBrand(car_id).ifPresent(
                                    car -> {
                                        System.out.println(car.getName());
                                        System.out.println("Company:");
                                        companyDAO.getBrand(car.getCompanyID()).ifPresent(
                                                company -> System.out.println(company.getName())
                                        );
                                    }
                            );
                        }
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Please enter a valid number!");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}
