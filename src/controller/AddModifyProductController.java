package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.Optional;

public class AddModifyProductController {

        @FXML
        public Label AddModifyProductLabel;

        @FXML
        public TextField TextProductID;

        @FXML
        public TextField TextProductName;

        @FXML
        public TextField TextInventoryProduct;

        @FXML
        public TextField TextPriceCostProduct;

        @FXML
        public TextField TextMaxProduct;

        @FXML
        public TextField TextMinProduct;

        @FXML
        public TextField OnSearchTextPartAdd;

        @FXML
        private TableView<Part> pickPartsTableView;

        @FXML
        private TableView<Part> currentPartsTableView;

        @FXML
        private TableColumn<Part, String> addPartIDColumn;

        @FXML
        private TableColumn<Part, String> addPartNameColumn;

        @FXML
        private TableColumn<Part, String> addPartInventoryLevelColumn;

        @FXML
        private TableColumn<Part, String> addPartPriceCostPerUnitColumn;

        @FXML
        private TableColumn<Part, String> currentPartIDColumn;

        @FXML
        private TableColumn<Part, String> currentPartNameColumn;

        @FXML
        private TableColumn<Part, String> currentPartInventoryLevelColumn;

        @FXML
        private TableColumn<Part, String> currentPartPriceCostPerUnitColumn;

        private final ObservableList<Part> pickAllParts = FXCollections.observableArrayList();
        private  ObservableList<Part> modifyAssociatedParts = FXCollections.observableArrayList();

        private int productIdCounter = 2;
        Product selectedProduct;

        public void InitializePickPartsTable() {

                System.out.println("Pick Parts Table Initialized");
                pickAllParts.addAll(Inventory.getAllParts());
                pickPartsTableView.setItems(pickAllParts);

                addPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                addPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                addPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                addPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

        public void InitProduct(Product product) {
                selectedProduct = product;
                TextProductID.setText(String.valueOf(selectedProduct.getId()));
                TextProductName.setText(selectedProduct.getName());
                TextInventoryProduct.setText(String.valueOf(selectedProduct.getStock()));
                TextMinProduct.setText(String.valueOf(selectedProduct.getMin()));
                TextMaxProduct.setText(String.valueOf(selectedProduct.getMax()));
                TextPriceCostProduct.setText(String.valueOf(selectedProduct.getPrice()));

//                if (!product.getAllAssociatedParts().contains(null)) {
//                        modifyAssociatedParts.addAll(product.getAllAssociatedParts());
//                        System.out.println("Product " + product + " has parts " + modifyAssociatedParts);
//                        currentPartsTableView.setItems(modifyAssociatedParts);
//
//                }
//
//                else {
//
//                }
                if (!product.getAllAssociatedParts().contains(null)) {
                        modifyAssociatedParts.addAll(product.getAllAssociatedParts());

                }

                System.out.println("Product " + product + " has parts " + modifyAssociatedParts);
                currentPartsTableView.setItems(modifyAssociatedParts);

                currentPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                currentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                currentPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                currentPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

        public void OnSearchStringEnteredAddPart(KeyEvent keyEvent) {

                String searchString = OnSearchTextPartAdd.getText();
                boolean isNumericString = MainController.isNumeric(searchString);
                ObservableList<Part> matchedParts = FXCollections.observableArrayList();

                if (isNumericString) {

                        matchedParts.add(Inventory.lookupPart(Integer.parseInt(searchString)));
                }

                else {

                        matchedParts = Inventory.lookupPart(OnSearchTextPartAdd.getText());
                }

                pickPartsTableView.setItems(matchedParts);
        }

        public void OnPartAddToProductButton(ActionEvent actionEvent) {

                System.out.println("Adding Part to Product");
                Part selectedPart = pickPartsTableView.getSelectionModel().getSelectedItem();
                System.out.println("Got selected Part");
                modifyAssociatedParts.add(selectedPart);
                System.out.println("Added selected Part to list.  Selected part is " + selectedPart
                + "and currentParts is " + modifyAssociatedParts);
                currentPartsTableView.setItems(modifyAssociatedParts);
                currentPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                currentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                currentPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                currentPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                System.out.println("Set Table view to display list");
        }

        public void OnPartRemoveFromProductButton(ActionEvent actionEvent) {

                modifyAssociatedParts.remove(currentPartsTableView.
                        getSelectionModel().getSelectedItem());
        }

        public void OnProductSaveButton(ActionEvent actionEvent) throws IOException {

                // Check if any of the fields are empty and throw an error dialog if so.
                if (TextProductName.getText().isBlank() || TextPriceCostProduct.getText().isBlank() ||
                        TextInventoryProduct.getText().isBlank() || TextMinProduct.getText().isBlank() ||
                        TextMaxProduct.getText().isBlank() || modifyAssociatedParts.isEmpty()){

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Empty Fields Found");
                        alert.setContentText("Please enter appropriate information into every field " +
                                "including at least one part");
                        alert.showAndWait();
                }
                // check if the digit requiring fields are parsable, and show error if not.
                try {
                        Double.parseDouble(TextPriceCostProduct.getText());
                        Integer.parseInt(TextInventoryProduct.getText());
                        Integer.parseInt(TextMinProduct.getText());
                        Integer.parseInt(TextMaxProduct.getText());

                } catch (NumberFormatException nfe) {
                        AddModifyPartsController.showErrorMessage(nfe, "Error in a numeric field", "Enter only numbers in the Price/Cost," +
                                "Inventory/Stock, Min, and Max fields.");
                }

                // set the new fields for the product
                String productName = TextProductName.getText();
                double priceCostProduct = Double.parseDouble(TextPriceCostProduct.getText());
                int inventoryProduct = Integer.parseInt(TextInventoryProduct.getText());
                int minProduct = Integer.parseInt(TextMinProduct.getText());
                int maxProduct = Integer.parseInt(TextMaxProduct.getText());

                // check to make sure that the min is less than inventory and max,
                // and that max is more than inventory.
                if (minProduct >= maxProduct || inventoryProduct < minProduct ||
                        inventoryProduct > maxProduct) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Inventory Error");
                        alert.setContentText("Inventory / Stock must be less than max and more than min, " +
                                "and max must be more than min.");
                        alert.showAndWait();
                }
                // check if this is a new part, if so make new part and assign ID
                if (TextProductID.getText().trim().isEmpty()) {

                        productIdCounter++;
                        Product newProduct;

                        System.out.println("This is a new product");

                        newProduct = new Product(productIdCounter, productName, priceCostProduct, inventoryProduct,
                                minProduct, maxProduct);

                        for (Part part: modifyAssociatedParts) {
                                newProduct.addAssociatedPart(part);
                        }

                        Inventory.addProduct(newProduct);
                }

                // else if the product is not new, set the properties of the product
                // then delete the old associated parts and add the new parts
                else {

                        selectedProduct.setName(productName);
                        selectedProduct.setStock(inventoryProduct);
                        selectedProduct.setPrice(priceCostProduct);
                        selectedProduct.setMin(minProduct);
                        selectedProduct.setMax(maxProduct);

                        ObservableList<Part> associatedParts = selectedProduct.getAllAssociatedParts();

                        for (Part part: modifyAssociatedParts) {
                                selectedProduct.deleteAssociatedPart(part);
                        }

                        for (Part part: modifyAssociatedParts) {
                                selectedProduct.addAssociatedPart(part);
                        }
                }
                modifyAssociatedParts.clear();

                loadMain(actionEvent);
        }

        public void OnProductCancelButton(ActionEvent actionEvent) throws IOException {
            System.out.println("Cancel button pressed");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Cancel your item?");
            alert.setContentText("Confirm you don't want to save your addition or modification");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                modifyAssociatedParts.clear();
                loadMain(actionEvent);
            }
        }



        public void loadMain(ActionEvent actionEvent) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("../view/Main.fxml"));
                Stage mainStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                mainStage.setTitle("Inventory Management Main");
                mainStage.setScene(new Scene(root, 1500, 975));
                mainStage.show();
        }
}
