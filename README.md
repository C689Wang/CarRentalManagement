# CarRentalManagement

Simple car rental management system using Java.

The project connects to a database using a JDBC driver and the java.sql class. The database might not be initially available, so a gradle dependency was used.

The system is a console-based system which tracks car rental companies and their cars available for rent. It also tracks customers and the cars they have rented from the car rental companies.

Note: On each run of the program, the previously saved database disappears. As seen in the SQL queries, the database deletes the tables if they already exist. 
