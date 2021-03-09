/**
 * @author Heaven-Leigh (Michelle) Masters
 */

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


public class MainController implements Initializable {

    @FXML
    public TableView<Part> partsTableView;

    @FXML
    public TableView<Product> productsTableView;

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

    /**
     * Called to initialize a controller after its root element has been completely processed,
     *                  and sets up the tables for parts and products.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
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
     * @param actionEvent triggered by pressing the exit button.
     */

    public void OnExitProgramButton(ActionEvent actionEvent) {

        Stage stage = (Stage) OnExitProgramButton.getScene().getWindow();
        stage.close();
    }


/** Parts (Left side) *************************************************************************/

    /**
     * Searches for a substring of a part name or ID to locate any matching parts.
     * @param keyEvent triggered from key entered in search text field
     */

    public void OnSearchStringEntered(KeyEvent keyEvent) {

        String searchString = OnSearchText.getText();
        boolean isNumericString = isNumeric(searchString);
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();

        if (isNumericString) {

            matchedParts.add(Inventory.lookupPart(Integer.parseInt(searchString)));
        }

        else {

            matchedParts = Inventory.lookupPart(OnSearchText.getText());
        }

        partsTableView.setItems(matchedParts);
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
     * This method opens the scene for adding a new part.
     *
     * @param actionEvent triggered from pressing the part's add button.
     * @throws IOException triggered if the scene cannot be loaded.
     */
    public void OnPartAddButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Pressed Part Add Button");

        Parent root = FXMLLoader.load(getClass().getResource("../view/AddModifyPartScene.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1500, 875);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Passes a selected part to the modify part screen for modification.
     *
     * @param actionEvent triggered by pressing the part's modify button
     * @throws Exception caught when a part is not selected
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
     * @param actionEvent triggered from the part's delete button.
     * @exception Exception triggered when a part is not selected.
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
     * Changes the scene for the addition of a new product.
     * @param actionEvent triggered from pressing the product's add button.
     * @throws IOException triggered if the scene cannot be loaded.
     */

    public void OnProductAddButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/AddModifyProductScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1500, 875);

        AddModifyProductController controller = loader.getController();
        controller.AddModifyProductLabel.setText("Add Product");
        controller.InitializePickPartsTable();

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Product");
        stage.setScene(scene);

        stage.show();
    }

    /**
     * Searches for a substring of a product name or ID to locate any matching parts.
     * @param keyEvent triggered from key entered in search text field
     */

    public void OnSearchStringEnteredProduct(KeyEvent keyEvent) {
        String searchString = OnSearchTextProduct.getText();
        boolean isNumericString = isNumeric(searchString);
        ObservableList<Product> matchedProducts = FXCollections.observableArrayList();

        if (isNumericString) {

            matchedProducts.add(Inventory.lookupProduct(Integer.parseInt(searchString)));
        } else {

            matchedProducts = Inventory.lookupProduct(OnSearchTextProduct.getText());
        }

        productsTableView.setItems(matchedProducts);
    }

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
            controller.InitProduct((Product) productsTableView.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {

            ErrorException("Product not found", "Please select a product to modify a product.");
        }
    }



    public void OnProductDeleteButton(ActionEvent actionEvent) {
        try {

            Product selectedProduct = (Product) productsTableView.getSelectionModel().getSelectedItem();

            if (DeleteConfirmation("Confirm you want to delete" +
                    selectedProduct.getName())) {

                Inventory.deleteProduct(selectedProduct);
            }

        } catch (Exception e) {

            ErrorException("Part not found",
                    "Please select a product in order to delete a product.");
        }
    }

    public boolean DeleteConfirmation(String contentText) {
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

    public void ErrorException (String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
