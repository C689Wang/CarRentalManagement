package carrental.menuobjects;

public class Car extends Company {
    private final int comp_id;

    public Car(String name, int car_id, int comp_id) {
        super(name, car_id);
        this.comp_id = comp_id;
    }

    public Car(String name, int comp_id) {
        super(name);
        this.comp_id = comp_id;
    }

    public int getCompanyID() {
        return comp_id;
    }
}
