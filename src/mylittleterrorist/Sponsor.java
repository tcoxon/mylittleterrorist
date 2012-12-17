package mylittleterrorist;

public class Sponsor {

    protected String name;
    protected int value;
    
    public Sponsor(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
