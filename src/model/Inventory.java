package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * holds the lists of part and products and performs the manipulative methods on the inventory.
 * @author Heaven-Leigh (Michelle) Masters
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * adds the test data only once when starting the application.
     */
    static {

        addTestData(); // sets list as the items within the tableview
    }

    /**
     * provides the test data to add when starting the application.
     */
    public static void addTestData(){

        Part acidophilus = new InHouse(1, "L. acidophilus", 500.00, 20, 5, 100, 00);
        Part bifidum = new InHouse(2, "B. bifidum", 300.00, 30, 10, 200, 001);
        Part cornMalto = new Outsourced(3, "Corn Maltodextrin", 50.00, 15, 5, 50, "Chemicals Inc");
        Part inulin = new Outsourced(4, "Inulin", 30.00, 10, 4, 40, "Fiber LLC");

        allParts.add(acidophilus);
        allParts.add(bifidum);
        allParts.add(cornMalto);
        allParts.add(inulin);

        Product testBlend = new Product(1, "Test Blend", 1000.00, 5, 0, 10);
        Product newBlend = new Product(2, "New Blend", 800.00, 1, 0, 1);

        testBlend.addAssociatedPart(bifidum);
        testBlend.addAssociatedPart(inulin);
        newBlend.addAssociatedPart(acidophilus);
        newBlend.addAssociatedPart(cornMalto);

        allProducts.add(testBlend);
        allProducts.add(newBlend);
    }

    /**
     * adds a part to inventory.
     * @param part is a part to add to the inventory
     */
    public static void addPart(Part part){

        allParts.add(part);
    }

    /**
     * adds a product to the inventory.
     * @param product is a product to add to inventory
     */
    public static void addProduct(Product product) {

        allProducts.add(product);
    }

    /**
     * gets all parts as a list from the inventory.
     * @return the list of all parts
     */
    public static ObservableList<Part> getAllParts(){

        return allParts;
    }

    /**
     * gets all products as a list from the inventory.
     * @return the list of all products
     */
    public static ObservableList<Product> getAllProducts() {

        return allProducts;
    }

    /**
     * finds and returns a part when given a part ID.
     * @param partID the part ID of the part to find
     * @return the part that is found, other returns null
     */
    public static Part lookupPart(int partID){

        for (Part part: allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    /**
     * searches for any matching parts with a name that matches the string passed.
     * @param partName a string that is passed to match a part name to
     * @return a list of matching parts or null if no matches
     */
    public static ObservableList<Part> lookupPart(String partName){

        ObservableList<Part> matchedPartsList = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                matchedPartsList.add(part);
            }
        }
        return matchedPartsList;
    }

    /**
     * searches for any matching products with an ID that matches the number passed.
     * @param productID is the number that is passed for finding a product by ID
     * @return the product that matches or null if no matches
     */
    public static Product lookupProduct(int productID) {

        for (Product product: allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    /**
     * searches for any matching products with a name that matches the string passed.
     * @param productName a string that is passed ot match a product name to
     * @return a list of matching products or null if no matches
     */
    public static ObservableList<Product> lookupProduct(String productName) {

        ObservableList<Product> matchedProductsList = FXCollections.observableArrayList();

        for (Product product: allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                matchedProductsList.add(product);
            }
        }
        return matchedProductsList;
    }

    /**
     * updates a part by removing the old part and adding the new part.
     * @param index the index location of the part in the allParts list to delete
     * @param selectedPart the part that replaces the old part in the list
     */
    public static void updatePart(int index, Part selectedPart){

        allParts.remove(index);
        allParts.add(selectedPart);
    }

    /**
     * updates a product by removing the old product and adding the new product.
     * @param index the index location of the old product in the allProducts list to delete
     * @param selectedProduct the product that replaces the old product in the list
     */
    public static void updateProduct(int index, Product selectedProduct) {

        allProducts.remove(index);
        allProducts.add(selectedProduct);
    }

    /**
     * deletes a part from the allParts list in Inventory.
     * @param selectedPart is the part to delete
     * @return boolean is true if the part is deleted and false if part is not found
     */
    public static boolean deletePart(Part selectedPart) {
        try {

            allParts.remove(selectedPart);
            return true;

        } catch (Exception e) {

            return false;
        }
    }

    /**
     * deletes a product from the allProducts list in Inventory.
     * @param selectedProduct is the product to delete
     * @return boolean is true if the product is deleted and false if product is not found
     */
    public static boolean deleteProduct(Product selectedProduct) {

        try {

            allParts.remove(selectedProduct);
            return true;

        } catch (Exception e) {

            return false;
        }
    }


}
