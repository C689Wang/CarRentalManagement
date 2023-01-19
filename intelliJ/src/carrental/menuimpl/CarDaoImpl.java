package carrental.menuimpl;

import carrental.daointerface.ObjectDAO;
import carrental.menuobjects.Car;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDaoImpl implements ObjectDAO<Car> {
    Statement stmt;
    Connection conn;
    PreparedStatement insertQuery;
    PreparedStatement selectParticularQuery;
    PreparedStatement selectSpecificQuery;
    String insertCar = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
    String selectAll = "SELECT * FROM CAR";
    String selectSpecific = "SELECT * FROM CAR LEFT JOIN CUSTOMER " +
            "ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
            "WHERE CUSTOMER.ID IS NULL AND CAR.COMPANY_ID = ?";
    String selectOne = "SELECT * FROM CAR WHERE ID = ?";

    public CarDaoImpl(Connection conn) {
        this.conn = conn;
        try {
            this.stmt = conn.createStatement();
            String sql =  "CREATE TABLE CAR" +
                    "(ID INT AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL, " +
                    "COMPANY_ID INT NOT NULL," +
                    "CONSTRAINT FK_CAR FOREIGN KEY (COMPANY_ID)" +
                    "REFERENCES COMPANY (ID)," +
                    "PRIMARY KEY (ID)," +
                    "UNIQUE (NAME))";
            stmt.execute(sql);
            String reset = "ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1";
            stmt.execute(reset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //retrieve list of students from the database
    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        try {
            ResultSet carRecords = stmt.executeQuery(selectAll);
            while (carRecords.next()) {
                int car_id = carRecords.getInt("ID");
                int comp_id = carRecords.getInt("COMPANY_ID");
                String name = carRecords.getString("NAME");
                cars.add(new Car(name, car_id, comp_id));
            }
            carRecords.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> getCompanySpecific(int comp_id) {
        List<Car> specificCars = new ArrayList<>();
        try {
            selectSpecificQuery = conn.prepareStatement(selectSpecific);
            selectSpecificQuery.setInt(1, comp_id);
            ResultSet specificRecords = selectSpecificQuery.executeQuery();
            while (specificRecords.next()) {
                int car_id = specificRecords.getInt("ID");
                String name = specificRecords.getString("NAME");
                specificCars.add(new Car(name, car_id, comp_id));
            }
            specificRecords.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specificCars;
    }

    @Override
    public Optional<Car> getBrand(int id) {
        try {
            selectParticularQuery = conn.prepareStatement(selectOne);
            selectParticularQuery.setInt(1, id);
            ResultSet selectedCar = selectParticularQuery.executeQuery();
            if (selectedCar.next()) {
                String name = selectedCar.getString("NAME");
                int comp_id = selectedCar.getInt("COMPANY_ID");
                return Optional.of(new Car(name, id, comp_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void addBrand(Car car) {
        try {
            insertQuery = conn.prepareStatement(insertCar);
            insertQuery.setString(1, car.getName());
            insertQuery.setInt(2, car.getCompanyID());
            insertQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
