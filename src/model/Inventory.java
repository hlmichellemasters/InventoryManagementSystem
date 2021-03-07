package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.Locale;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    static {

        addTestData(); // sets list as the items within the tableview
    }

    public static void addTestData(){

        allParts.add(new InHouse(1, "L. acidophilus", 500.00, 20, 5, 100, 00));
        allParts.add(new InHouse(2, "B. bifidum", 300.00, 30, 10, 200, 001));
        allParts.add(new Outsourced(3, "Corn Maltodextrin", 50.00, 15, 5, 50, "Chemicals Inc"));
        allParts.add(new Outsourced(4, "Inulin", 30.00, 10, 4, 40, "Fiber LLC"));

        allProducts.add(new Product(1, "Test Blend", 1000.00, 5, 0, 10));
        allProducts.add(new Product(2, "New Blend", 800.00, 1, 0, 1));
    }

    public static void addPart(Part part){

        allParts.add(part);
    }

    public static void addProduct(Product product) {

        allProducts.add(product);
    }

    public static ObservableList<Part> getAllParts(){

        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {

        return allProducts;
    }

    public static Part lookupPart(int partID){

        for (Part part: allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String partName){

        ObservableList<Part> matchedPartsList = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                matchedPartsList.add(part);
            }
        }
        return matchedPartsList;
    }

    public static Product lookupProduct(int productID) {

        for (Product product: allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    public static ObservableList<Product> lookupProduct(String productName) {

        ObservableList<Product> matchedProductsList = FXCollections.observableArrayList();

        for (Product product: allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                matchedProductsList.add(product);
            }
        }
        return matchedProductsList;
    }

    public static void updatePart(int index, Part selectedPart){

        allParts.remove(index);
        allParts.add(selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct) {

        allProducts.remove(index);
        allProducts.add(selectedProduct);
    }

    public static boolean deletePart(Part selectedPart){

        allParts.remove(selectedPart);
        return true;
    }

    public static boolean deleteProduct(Product selectedProduct) {

        allProducts.remove(selectedProduct);
        return true;
    }


}
