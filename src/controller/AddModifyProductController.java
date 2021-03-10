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
import java.util.Iterator;
import java.util.Optional;

/**
 * this class provides the control for the Add Product and Modify Product Screens.
 * @author Heaven-Leigh (Michelle) Masters
 */
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

        /**
         * initializes the table for picking parts to add to a product.
         */
        public void InitializePickPartsTable() {

                System.out.println("Pick Parts Table Initialized");
                pickAllParts.addAll(Inventory.getAllParts());
                pickPartsTableView.setItems(pickAllParts);

                addPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                addPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                addPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                addPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

        /**
         * initializes the part table that displays the parts that are in the selected product.
         * @param product is the selected product that the table will display the parts for
         */
        public void InitProduct(Product product) {

                selectedProduct = product;
                TextProductID.setText(String.valueOf(selectedProduct.getId()));
                TextProductName.setText(selectedProduct.getName());
                TextInventoryProduct.setText(String.valueOf(selectedProduct.getStock()));
                TextMinProduct.setText(String.valueOf(selectedProduct.getMin()));
                TextMaxProduct.setText(String.valueOf(selectedProduct.getMax()));
                TextPriceCostProduct.setText(String.valueOf(selectedProduct.getPrice()));

                if (!product.getAllAssociatedParts().contains(null)) {
                        modifyAssociatedParts.addAll(product.getAllAssociatedParts());

                }

                currentPartsTableView.setItems(modifyAssociatedParts);

                currentPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                currentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                currentPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                currentPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

        /**
         * searches the search string entered to look for any matches to part ID or part name.
         * Uses isNumericString method in order to determine if the search is numeric or alphabetical.
         * Fills a matchedParts list with parts that match the search, if none all parts are shown.
         * @param keyEvent triggered by a key press within the part search text field
         */
        public void OnSearchStringEnteredAddPart(KeyEvent keyEvent) {

                String searchString = OnSearchTextPartAdd.getText();
                ObservableList<Part> matchedParts = MainController.FindMatchedParts(searchString);
                pickPartsTableView.setItems(matchedParts);
        }

        /**
         * adds selected part to the product's associated parts list and table.
         * @param actionEvent triggered from pressing the add button on the add/modify product screen.
         */
        public void OnPartAddToProductButton(ActionEvent actionEvent) {

                Part selectedPart = pickPartsTableView.getSelectionModel().getSelectedItem();
                modifyAssociatedParts.add(selectedPart);

                currentPartsTableView.setItems(modifyAssociatedParts);

                currentPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                currentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                currentPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                currentPartPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

        /**
         * removes the selected part from the product's associated parts list and table.
         * Includes confirmation that the user would like to delete the part from the product.
         * @param actionEvent triggered from pressing
         */
        public void OnPartRemoveFromProductButton(ActionEvent actionEvent) {

                Part part = currentPartsTableView.getSelectionModel().getSelectedItem();

                System.out.println("selected item to delete is " + part.getName());

                if (MainController.DeleteConfirmation("Confirm you want to delete " +
                        part.getName())) {

                        modifyAssociatedParts.remove(part);
                        System.out.println("deleted " + part.getName() + " from modifyAssociatedParts list");
                }
        }

        /**
         * saves the product information that has been added or modified.
         * Includes data verification that fields are not blank and that numerical fields are numerical.
         * Also verifies that the inventory set is less than max and more than min.
         * It throws an error dialog and gives relevant instructions for any erroneous data.
         * @param actionEvent triggered from pressing the save button on the add or modify product screens
         * @throws IOException if the main screen cannot be reloaded
         */
        public void OnProductSaveButton(ActionEvent actionEvent) throws IOException {

                // Check if any of the fields are empty and throw an error dialog if so.
                if (TextProductName.getText().isBlank() || TextPriceCostProduct.getText().isBlank() ||
                        TextInventoryProduct.getText().isBlank() || TextMinProduct.getText().isBlank() ||
                        TextMaxProduct.getText().isBlank()){

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Empty Fields Found");
                        alert.setContentText("No blank fields are allowed, please enter info for each");
                        alert.showAndWait();
                }
                // check if the digit requiring fields are parsable, and show error if not.
                try {
                        Double.parseDouble(TextPriceCostProduct.getText());
                        Integer.parseInt(TextInventoryProduct.getText());
                        Integer.parseInt(TextMinProduct.getText());
                        Integer.parseInt(TextMaxProduct.getText());

                } catch (NumberFormatException nfe) {
                        AddModifyPartsController.showErrorMessage(nfe, "Error in a numeric field",
                                "Enter only numbers in the Price/Cost, Inventory/Stock, Min, and Max fields.");
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

                        MainController.ErrorException("Inventory Error", "Inventory / Stock must " +
                                "be less than max and more than min, and max must be more than min.");
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

                        selectedProduct.clearParts();

                        for (Part part: modifyAssociatedParts) {

                                selectedProduct.addAssociatedPart(part);
                        }
                }

                modifyAssociatedParts.clear();

                loadMain(actionEvent);
        }

        /**
         * cancels the addition or modification of the product currently being editted.
         * Includes a confirmation dialog to confirm they want to not save.
         * @param actionEvent triggered from the cancel button on the add or modify product screens
         * @throws IOException if the main screen cannot be reloaded.
         */
        public void OnProductCancelButton(ActionEvent actionEvent) throws IOException {

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

        /**
         * loads the main screen back into the stage.
         * @param actionEvent triggered from an action event from other buttons
         * @throws IOException if the main screen cannot be reloaded
         */
        public void loadMain(ActionEvent actionEvent) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("../view/Main.fxml"));
                Stage mainStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                mainStage.setTitle("Inventory Management Main");
                mainStage.setScene(new Scene(root, 1500, 975));
                mainStage.show();
        }
}
