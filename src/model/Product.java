package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * an object that lives in the inventory that is made up of parts.
 * @author Heaven-Leigh (Michelle) Masters
 */
public class Product {

    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Main constructor for making a product object.
     * @param id is the unique ID of the product.
     * @param name is the displayed name of the product.
     * @param price is the cost of the product.
     * @param stock is the amount in inventory of the product.
     * @param min is the minimum amount for inventory of the product.
     * @param max is the maximum amount for inventory of the product.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * gets all of the associated parts of a product and returns them, returns null if none.
     * @return returns any associated parts of the product.
     */

    public ObservableList<Part> getAllAssociatedParts() {

        if (!associatedParts.contains(null)) {
            return associatedParts;
        }

        else {
            return null;
        }
    }

    /**
     * adds a part to the associated parts list of a product.
     * @param part receives a part in order to add to the list of a product's associated parts.
     */

    public void addAssociatedPart(Part part) {

        System.out.println("received part: " + part);
        associatedParts.add(part);
        System.out.println("added part: " + part + "to product " + getName());
    }

    /**
     * deletes a part from a product's associated parts list.
     * @param selectedAssociatedPart receives a part in order to delete the same part from
     *                               the associated parts list of the product.
     * @return returns true if the part was deleted, otherwise returns false.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        try {
            associatedParts.remove(selectedAssociatedPart);
            System.out.println("Removed part " + selectedAssociatedPart);
        } catch (Exception e) {

            return false;
        }

        return true;
    }

    /**
     * clears all parts from the selected parts associated parts list.
     * This allows a fresh list to be saved from the modify parts list.
     */
    public void clearParts() {

        associatedParts.clear();
    }
}
