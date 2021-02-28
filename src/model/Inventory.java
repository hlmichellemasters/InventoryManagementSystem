package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

//    private ObservableList<Product> allProducts;



    public static ObservableList addTestData(){
        allParts.add(new InHouse(1, "L. acidophilus", 500.00, 20, 5, 100));
        allParts.add(new InHouse(2, "B. bifidum", 300.00, 30, 10, 200));
        allParts.add(new Outsourced(3, "Corn Maltodextrin", 50.00, 15, 5, 50));
        allParts.add(new Outsourced(4, "Inulin", 30.00, 10, 4, 40));
        return allParts;
    }

    public static void addPart(Part part){

        allParts.add(part);
    }

    public ObservableList<Part> getAllParts(){

        return allParts;
    }

    public Part lookupPart(int partID){

        return null;    // Change to return Part
    }

    public Part lookupPart(String partName){

        return null;    // Change to return ObservableList<Part>
    }

    public void updatePart(int index, Part selectedPart){

    }

    public boolean deletePart(Part selectedPart){


        return true;
    }


//    public Product lookupProduct(int productID){

//        return null;    // Change to return Product
//    }


}
