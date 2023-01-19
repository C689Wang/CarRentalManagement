package carrental.menuimpl;

import carrental.daointerface.ObjectDAO;
import carrental.menuobjects.Car;
import carrental.menuobjects.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class CustomerDaoImpl implements ObjectDAO<Customer> {
    Statement stmt;
    Connection conn;
    PreparedStatement insertQuery;
    PreparedStatement selectParticularQuery;
    PreparedStatement updateQuery;
    PreparedStatement deleteQuery;
    String insertCustomer = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
    String selectAll = "SELECT * FROM CUSTOMER";
    String selectOne = "SELECT * FROM CUSTOMER WHERE ID = ?";
    String update = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
    String delete = "UPDATE CUSTOMER SET RENTED_CAR_ID = DEFAULT WHERE ID = ?";

    public CustomerDaoImpl(Connection conn) {
        this.conn = conn;
        try {
            this.stmt = conn.createStatement();
            String sql =  "CREATE TABLE CUSTOMER" +
                    "(ID INT AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL, " +
                    "RENTED_CAR_ID INT DEFAULT NULL," +
                    "CONSTRAINT FK_CUSTOMER FOREIGN KEY (RENTED_CAR_ID)" +
                    "REFERENCES CAR (ID)," +
                    "PRIMARY KEY (ID)," +
                    "UNIQUE (NAME))";
            stmt.execute(sql);
            updateQuery = conn.prepareStatement(update);
            deleteQuery = conn.prepareStatement(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCar(int id) {
        try {
            deleteQuery.setInt(1, id);
            deleteQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //retrieve list of students from the database
    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        try {
            ResultSet customerRecords = stmt.executeQuery(selectAll);
            while (customerRecords.next()) {
                int customer_id = customerRecords.getInt("ID");
                int rented_car_id = customerRecords.getInt("RENTED_CAR_ID");
                String name = customerRecords.getString("NAME");
                customers.add(new Customer(name, customer_id, rented_car_id));
            }
            customerRecords.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Optional<Customer> getBrand(int id) {
        try {
            selectParticularQuery = conn.prepareStatement(selectOne);
            selectParticularQuery.setInt(1, id);
            ResultSet selectedCustomer = selectParticularQuery.executeQuery();
            if (selectedCustomer.next()) {
                String name = selectedCustomer.getString("NAME");
                int rented_car_id = selectedCustomer.getInt("RENTED_CAR_ID");
                return Optional.of(new Customer(name, id, rented_car_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void updateCar(Car car, int id) {
        try {
            updateQuery.setInt(1, car.getID());
            updateQuery.setInt(2, id);
            updateQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBrand(Customer customer) {
        try {
            insertQuery = conn.prepareStatement(insertCustomer);
            insertQuery.setString(1, customer.getName());
            insertQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
