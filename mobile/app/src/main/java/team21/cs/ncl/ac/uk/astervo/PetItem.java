package team21.cs.ncl.ac.uk.astervo;

/**
 * Created by thebillington on 02/04/2015.
 */

public class PetItem {

    private String name = "";
    private int quantity = 0;
    private String resource = "";

    public PetItem(String name, int quantity, String resource) {
        this.setName(name);
        this.setQuantity(quantity);
        this.setResource(resource);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
