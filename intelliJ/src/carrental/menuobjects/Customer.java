package carrental.menuobjects;

public class Customer extends Company {
    private final int rentedCarID;

    public Customer(String name, int id, int rentedCarID) {
        super(name, id);
        this.rentedCarID = rentedCarID;
    }

    public Customer(String name) {
        super(name);
        rentedCarID = 0;
    }

    public int getRentedCarID() {
        return rentedCarID;
    }
}
