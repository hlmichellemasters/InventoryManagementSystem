package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;

/**
 *
 * @author Heaven-Leigh (Michelle) Masters
 */

public class Product {

    private static ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

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

    public ObservableList<Part> getAllAssociatedParts() {

        if (!associatedParts.contains(null)) {
            return associatedParts;
        }

        else {
            return null;
        }
    }

    public void addAssociatedPart(Part part) {

        System.out.println("received part: " + part);
        associatedParts.add(part);
        System.out.println("added part: " + part + "to product " + getName());
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        try {
            associatedParts.remove(selectedAssociatedPart);
            System.out.println("Removed part " + selectedAssociatedPart);
        } catch (Exception e){
            // add some kind of error statement, is this error even possible?
            return false;
        }

        return true;
    }
}
