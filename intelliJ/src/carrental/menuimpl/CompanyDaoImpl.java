package carrental.menuimpl;

import carrental.daointerface.ObjectDAO;
import carrental.menuobjects.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class CompanyDaoImpl implements ObjectDAO<Company> {
    Statement stmt;
    PreparedStatement insertQuery;
    PreparedStatement selectParticularQuery;
    Connection conn;
    String insertCompany = "INSERT INTO COMPANY (NAME) VALUES (?)";
    String selectAll = "SELECT * FROM COMPANY";
    String selectOne = "SELECT * FROM COMPANY WHERE ID = ?";

    public CompanyDaoImpl(Connection conn){
        this.conn = conn;
        try {
            this.stmt = conn.createStatement();
            String delCustomer = "DROP TABLE CUSTOMER IF EXISTS";
            stmt.execute(delCustomer);
            String delCar = "DROP TABLE CAR IF EXISTS";
            stmt.execute(delCar);
            String delCompany = "DROP TABLE COMPANY IF EXISTS";
            stmt.execute(delCompany);
            String sql =  "CREATE TABLE COMPANY " +
                    "(ID INT AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL, " +
                    "PRIMARY KEY (ID)," +
                    "UNIQUE (NAME))";
            stmt.execute(sql);
            this.insertQuery = conn.prepareStatement(insertCompany);
            this.selectParticularQuery = conn.prepareStatement(selectOne);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //retrieve list of students from the database
    @Override
    public List<Company> getAll() {
        List<Company> companies = new ArrayList<>();
        try {
            ResultSet companyRecords = stmt.executeQuery(selectAll);
            while (companyRecords.next()) {
                int id = companyRecords.getInt("ID");
                String name = companyRecords.getString("NAME");
                companies.add(new Company(name, id));
            }
            companyRecords.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public Optional<Company> getBrand(int id) {
        try {
            selectParticularQuery.setInt(1, id);
            ResultSet selectedCompany = selectParticularQuery.executeQuery();
            if (selectedCompany.next()) {
                String name = selectedCompany.getString("NAME");
                return Optional.of(new Company(name, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void addBrand(Company company) {
        try {
            insertQuery.setString(1, company.getName());
            insertQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
