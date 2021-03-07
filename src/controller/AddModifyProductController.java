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
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
        private Button OnPartRemoveFromProductButton;

        @FXML
        private Button OnPartAddToProductButton;

        @FXML
        private Button OnProductSaveButton;

        @FXML
        private Button OnProductCancelButton;

        @FXML
        private TableView<Part> pickPartsTableView;

        @FXML
        private TableView<Part> currentPartsTableView;

        @FXML
        private TableColumn partIDColumn;

        @FXML
        private TableColumn partNameColumn;

        @FXML
        private TableColumn partInventoryLevelColumn;

        @FXML
        private TableColumn partPriceCostPerUnitColumn;

        @FXML
        private TableView addPartsTableView;

        @FXML
        private TableColumn addPartIDColumn;

        @FXML
        private TableColumn addPartNameColumn;

        @FXML
        private TableColumn addPartInventoryLevelColumn;

        @FXML
        private TableColumn addPartPriceCostPerUnitColumn;

        @FXML
        private TableColumn currentPartIDColumn;

        @FXML
        private TableColumn currentPartNameColumn;

        @FXML
        private TableColumn currentPartInventoryLevelColumn;

        @FXML
        private TableColumn currentPartPriceCostPerUnitColumn;

        private ObservableList<Part> pickAllParts = FXCollections.observableArrayList();
        private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
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

                if (!product.getAllAssociatedParts().contains(null)) {
                        associatedParts.addAll(product.getAllAssociatedParts());
                        currentPartsTableView.setItems(associatedParts);
                        currentPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                        currentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                        currentPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                        currentPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                }
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
                Part selectedPart = ((Part) pickPartsTableView.getSelectionModel().getSelectedItem());
                System.out.println("Got selected Part");
                associatedParts.add(selectedPart);
                System.out.println("Added selected Part to list.  Selected part is " + selectedPart
                + "and currentParts is " + associatedParts);
                currentPartsTableView.setItems(associatedParts);
                currentPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                currentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                currentPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                currentPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                System.out.println("Set Table view to display list");
        }

        public void OnPartRemoveFromProductButton(ActionEvent actionEvent) {
                selectedProduct.deleteAssociatedPart((Part)currentPartsTableView.
                        getSelectionModel().getSelectedItem());
                currentPartsTableView.setItems(selectedProduct.getAllAssociatedParts());

        }

        public void OnProductSaveButton(ActionEvent actionEvent) throws IOException {
                System.out.println("This will commit the data to the observable list(view)");

                // Check if any of the fields are empty and throw an error dialog if so.
                if (TextProductName.getText().isBlank() || TextPriceCostProduct.getText().isBlank() ||
                        TextInventoryProduct.getText().isBlank() || TextMinProduct.getText().isBlank() ||
                        TextMaxProduct.getText().isBlank()) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Empty Fields Found");
                        alert.setContentText("Please enter appropriate information into every field");

                        alert.showAndWait();
                }
                try {
                        Double.parseDouble(TextPriceCostProduct.getText());
                        Integer.parseInt(TextInventoryProduct.getText());
                        Integer.parseInt(TextMinProduct.getText());
                        Integer.parseInt(TextMaxProduct.getText());

                } catch (NumberFormatException nfe) {
                        AddModifyPartsController.showErrorMessage(nfe, "Error in a numeric field", "Enter only numbers in the Price/Cost," +
                                "Inventory/Stock, Min, and Max fields.");
                }

                String productName = TextProductName.getText();
                double priceCostProduct = Double.parseDouble(TextPriceCostProduct.getText());
                int inventoryProduct = Integer.parseInt(TextInventoryProduct.getText());
                int minProduct = Integer.parseInt(TextMinProduct.getText());
                int maxProduct = Integer.parseInt(TextMaxProduct.getText());

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

                        for (Part part : associatedParts) {
                                newProduct.addAssociatedPart(part);
                        }

                        Inventory.addProduct(newProduct);

                }
                // else if the product is not new, set the properties of the product
                else {
                        selectedProduct.setName(productName);
                        selectedProduct.setStock(inventoryProduct);
                        selectedProduct.setPrice(priceCostProduct);
                        selectedProduct.setMin(minProduct);
                        selectedProduct.setMax(maxProduct);


                        for (Part part : associatedParts) {
                                selectedProduct.deleteAssociatedPart(part);
                        }
                        for (Part part : associatedParts) {
                                selectedProduct.addAssociatedPart(part);
                        }
                }


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
