package carrental.menuobjects;

public class Company {
    private String name;
    private int id;

    public Company(String name, int id){
        this.name = name;
        this.id = id;
    }

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
}
