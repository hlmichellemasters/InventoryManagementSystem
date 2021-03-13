package controller;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import model.Inventory;
import model.Part;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controller for the main screen of the application.
 * Shows table of parts and products in Inventory.
 * Allows searching and deleting of both parts and products.
 * @author Heaven-Leigh (Michelle) Masters
 */
public class MainController implements Initializable {

    @FXML
    public TableView<Part> partsTableView;

    @FXML
    public TableView<Product> productsTableView;

    @FXML
    public Label OnNoPartsFound;

    @FXML
    public Label OnNoProductsFound;

    @FXML
    public Button OnPartAddButton;

    @FXML
    public Button OnPartModifyButton;

    @FXML
    public Button OnPartDeleteButton;

    @FXML
    private Button OnExitProgramButton;

    @FXML
    private TextField OnSearchText;

    @FXML
    private TextField OnSearchTextProduct;

    @FXML
    private TableColumn<Part, String> partIDColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, String> partInventoryLevelColumn;

    @FXML
    private TableColumn<Part, String> partPriceCostPerUnitColumn;

    @FXML
    private TableColumn<Part, String> productIDColumn;

    @FXML
    private TableColumn<Part, String> productNameColumn;

    @FXML
    private TableColumn<Part, String> productInventoryLevelColumn;

    @FXML
    private TableColumn<Part, String> productPriceCostPerUnitColumn;

    public static int partIdCounter = 4;   // 4 parts are pre-loaded into Inventory

    public static int productIdCounter = 2; // 2 parts are pre-loaded into Inventory

    /**
     * Called to initialize a controller after its root element has been completely processed,
     *                  and sets up the tables for parts and products.
     * @param location used to resolve relative paths for the root object,
     *                 or null if the location is not known
     * @param resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set table and column within view

        partsTableView.setItems(Inventory.getAllParts());

        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTableView.setItems(Inventory.getAllProducts());

        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Exits/Closes the application.
     * @param actionEvent triggered by pressing the exit button
     */
    public void OnExitProgramButton(ActionEvent actionEvent) {

        Stage stage = (Stage) OnExitProgramButton.getScene().getWindow();
        stage.close();
    }


/** Parts (Left side) *************************************************************************/

    /**
     * searches the search string entered to look for any matches to part ID or part name.
     * Uses FindMatchedParts method to search for any matching parts.
     * Fills a matchedParts list with parts that match the search, and sets the parts table with them.
     * @param keyEvent triggered by a key press within the part search text field
     */
    public void OnSearchStringEntered(KeyEvent keyEvent) {

        OnNoPartsFound.setVisible(false);
        String searchString = OnSearchText.getText();
        ObservableList<Part> matchedParts = FindMatchedParts(searchString);
        if (matchedParts.isEmpty()) {
            OnNoPartsFound.setVisible(true);
        }
        partsTableView.setItems(matchedParts);
    }

    /**
     * Opens the scene for adding a new part.
     *
     * @param actionEvent triggered from pressing the part's add button.
     * @throws IOException triggered if the scene cannot be loaded.
     */
    public void OnPartAddButton(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/AddModifyPartScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1500, 875);

        AddModifyPartsController controller = loader.getController();
        controller.newPartFlag = true;
        controller.TextPartID.setText(String.valueOf(++partIdCounter));

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Passes a selected part to the modify part screen for modification.
     * Catches the exception and shows error dialog if a part is not selected.
     * @param actionEvent triggered by pressing the part's modify button
     */
    public void OnPartModifyButton(ActionEvent actionEvent) {
        System.out.println("Pressed Part Modify Button");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddModifyPartScene.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1500, 875);

            AddModifyPartsController controller = loader.getController();
            controller.AddModifyPartsLabel.setText("Modify Part");
            controller.initData(partsTableView.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {
            ErrorException("Part not selected", "Please select a part in order to modify a part.");
        }
    }

    /**
     * This method will delete a selected part from inventory upon confirmation.
     * Catches the exception and shows an error dialog if a part is not selected.
     * @param actionEvent triggered from the part's delete button
     */
    public void OnPartDeleteButton(ActionEvent actionEvent) {
        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

            if (DeleteConfirmation("Confirm you want to delete " +
                    selectedPart.getName())) {

                Inventory.deletePart(selectedPart);
            }

        } catch (Exception e) {

            ErrorException("Part not found", "Please select a part to delete a part.");
        }
    }

    /**
     * Products (Right side)
     *************************************************************************/

    /**
     * Searches for a substring of a product name or ID to locate any matching parts.
     * Uses same strategy as searching for a matchedParts,
     * but with lookupProduct instead of lookupPart from Inventory.
     * @param keyEvent triggered from key entered in search text field
     */
    public void OnSearchStringEnteredProduct(KeyEvent keyEvent) {

        OnNoProductsFound.setVisible(false);
        String searchString = OnSearchTextProduct.getText();
        ObservableList<Product> matchedProducts = FindMatchedProducts(searchString);

        if (matchedProducts.isEmpty()) {
            OnNoProductsFound.setVisible(true);
        }

        productsTableView.setItems(matchedProducts);
    }

    /**
     * Changes the scene for the addition of a new product.
     * @param actionEvent triggered from pressing the product's add button.
     * @throws IOException if the scene cannot be loaded.
     */
    public void OnProductAddButton(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/AddModifyProductScene.fxml"));
        Parent root = loader.load();

        AddModifyProductController controller = loader.getController();
        controller.AddModifyProductLabel.setText("Add Product");
        controller.InitializePickPartsTable();
        controller.newProductFlag = true;
        controller.TextProductID.setText(String.valueOf(++productIdCounter));

        Scene scene = new Scene(root, 1500, 875);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Product");
        stage.setScene(scene);

        stage.show();
    }

    /**
     * changes the scene to the modify product screen.
     * Calls to initialize the selected products information on the modify screen.
     * Catches the error that a product was not selected if one is not selected.
     * @param actionEvent triggered by pressing the modify button for products
     */
    public void OnProductModifyButton(ActionEvent actionEvent) {
        System.out.println("Pressed Product Modify Button");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddModifyProductScene.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1500, 875);

            AddModifyProductController controller = loader.getController();
            controller.AddModifyProductLabel.setText("Modify Product");
            controller.InitializePickPartsTable();
            controller.InitProduct(productsTableView.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {

            ErrorException("Product not found", "Please select a product to modify a product.");
        }
    }

    /**
     * deletes a product from the inventory upon confirmation.
     * Checks to make sure the product has no parts in it, otherwise throws error.
     * @param actionEvent triggered from delete button under product's table on main screen
     */
    public void OnProductDeleteButton(ActionEvent actionEvent) {

        try {

            Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

            if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
                ErrorException("Cannot Delete Product", "Remove all parts before " +
                        selectedProduct.getName() + " can be deleted");
                return;
            }

            if (DeleteConfirmation("Confirm you want to delete" +
                    selectedProduct.getName())) {

                Inventory.deleteProduct(selectedProduct);
            }

        } catch (Exception e) {

            ErrorException("Product not found",
                    "Please select a product in order to delete a product.");
        }
    }

    /**
     * finds matched parts given a search string.
     * First checks if it is numeric and if so matches the part ID number.
     * Else checks for matches within the part name.
     * @param searchString uses the search text that has been entered into the field
     * @return any matched parts, or empty list which causes all parts to display
     */
    public static ObservableList<Part> FindMatchedParts(String searchString) {

        boolean isNumericString = isNumeric(searchString);
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();

        if (isNumericString) {

            matchedParts.add(Inventory.lookupPart(Integer.parseInt(searchString)));
        }

        else {

            matchedParts = Inventory.lookupPart(searchString);
        }

        return matchedParts;
    }

    /**
     * finds matched products given a search string.
     * First checks if it is numeric and if so matches the product ID number.
     * Else checks for matches within the product name.
     * @param searchString uses the search text that has been entered into the field
     * @return any matched products, or empty list which causes all products to display
     */
    public static ObservableList<Product> FindMatchedProducts(String searchString) {

        boolean isNumericString = isNumeric(searchString);
        ObservableList<Product> matchedProducts = FXCollections.observableArrayList();

        if (isNumericString) {

            matchedProducts.add(Inventory.lookupProduct(Integer.parseInt(searchString)));
        }

        else {

            matchedProducts = Inventory.lookupProduct(searchString);
        }

        return matchedProducts;
    }

    /**
     * Checks whether the string provided is made up of at least one digit.
     * @param numOrString receives a String to check whether it is numeric.
     * @return true if the String is numeric, otherwise false.
     */
    public static boolean isNumeric(String numOrString) {

        if (numOrString == null) {

            return false;
        }

        try {

            Integer.parseInt(numOrString);
        } catch (NumberFormatException nfe) {

            return false;
        }

        return true;
    }

    /**
     * displays a delete confirmation dialog box and displays a method's context text.
     * @param contentText is the text from the method with more information regarding details of delete
     * @return boolean true if the okay button is pressed for deleting, and false if cancelled.
     */
    public static boolean DeleteConfirmation(String contentText) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete item?");
        alert.setContentText(contentText);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    /**
     * displays an error dialog box with the header text and content text details from calling method.
     * @param headerText is the heading or category of the error
     * @param contentText is the details passed from calling method regarding the error.
     */
    public static void ErrorException (String headerText, String contentText) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
