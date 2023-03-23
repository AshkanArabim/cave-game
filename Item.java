public class Item {
    private String name;
    private String type;
    private int power;

    // constructor
    public Item(String name, String type, int power) {
        this.name = name;
        this.type = type;
        this.power = power;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getPower() {
        return this.power;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
