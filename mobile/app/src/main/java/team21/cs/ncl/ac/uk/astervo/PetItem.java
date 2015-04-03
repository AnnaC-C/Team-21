package team21.cs.ncl.ac.uk.astervo;

/**
 * Created by thebillington on 02/04/2015.
 */

public class PetItem {

    private String name = "";
    private int quantity = 0;
    private int id = 0;
    private String resource = "";
    private boolean consumable = false;

    public PetItem(String name, int quantity, String resource, int id, boolean consumable) {
        this.setName(name);
        this.setQuantity(quantity);
        this.setResource(resource);
        this.setId(id);
        this.setConsumable(consumable);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }
}
