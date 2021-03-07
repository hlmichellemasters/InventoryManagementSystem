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
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    public TableView partsTableView;

    @FXML
    public TableView productsTableView;

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
    private TableColumn partIDColumn;

    @FXML
    private TableColumn partNameColumn;

    @FXML
    private TableColumn partInventoryLevelColumn;

    @FXML
    private TableColumn partPriceCostPerUnitColumn;

    @FXML
    private TableColumn productIDColumn;

    @FXML
    private TableColumn productNameColumn;

    @FXML
    private TableColumn productInventoryLevelColumn;

    @FXML
    private TableColumn productPriceCostPerUnitColumn;

    @FXML
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    @FXML
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();



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

        System.out.println("Initialized!");
    }

    public void OnExitProgramButton(ActionEvent actionEvent) {
        Stage stage = (Stage) OnExitProgramButton.getScene().getWindow();
        stage.close();
    }



/** Parts (Left side) *************************************************************************/

    /**
     * This method searches for a substring of a part name to locate any matching parts.
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



    public static boolean isNumeric(String numOrString) {

        if (numOrString == null) {

            return false;
        }

        try {

            Integer.parseInt(numOrString);
        }

        catch (NumberFormatException nfe) {

            return false;
        }

        return true;
    }

    /**
     * This method opens the scene for adding a new part.
     * @param actionEvent
     * @throws IOException
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
     * When this method is called it passes the selected part to the modify scene.
     * @param actionEvent
     * @throws IOException
     */
    public void OnPartModifyButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Pressed Part Modify Button");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddModifyPartScene.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1500, 875);

            AddModifyPartsController controller = loader.getController();
            controller.AddModifyPartsLabel.setText("Modify Part");
            controller.initData((Part) partsTableView.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("Please select a product in order to modify a product");

            alert.showAndWait();
        }
    }

    /**
     * This method will delete a selected part after getting confirmation from a dialog box.
     * @param actionEvent
     */
    public void OnPartDeleteButton(ActionEvent actionEvent) {
        try {
            Part selectedPart = (Part) partsTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete item?");
            alert.setContentText("Confirm you want to delete " + selectedPart.getName());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("Please select a part in order to delete a part");

            alert.showAndWait();
            }
    }

    public void OnSearchStringEnteredProduct(KeyEvent keyEvent) {
        String searchString = OnSearchTextProduct.getText();
        boolean isNumericString = isNumeric(searchString);
        ObservableList<Product> matchedProducts = FXCollections.observableArrayList();

        if (isNumericString) {

            matchedProducts.add(Inventory.lookupProduct(Integer.parseInt(searchString)));
        }

        else {

            matchedProducts = Inventory.lookupProduct(OnSearchTextProduct.getText());
        }

        productsTableView.setItems(matchedProducts);
    }

    public void OnProductAddButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Pressed Product Add Button");

        Parent root = FXMLLoader.load(getClass().getResource("../view/AddModifyProductScene.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1500, 875);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    public void OnProductModifyButton(ActionEvent actionEvent) {
    }

    public void OnProductDeleteButton(ActionEvent actionEvent) {
    }



    /** Products (Right side) *************************************************************************/

//    public void OnProductAddButton(ActionEvent actionEvent) throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("../view/AddModifyProductScene.fxml"));
//        Parent root = loader.load();
//
//        Scene scene = new Scene(root, 1500, 875);
//
//        AddModifyProductController controller = loader.getController();
//        controller.AddModifyProductLabel.setText("Add Product");
//        controller.InitPickPartData(Inventory.getAllParts());
//
//        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
//        stage.setTitle("Add Product");
//        stage.setScene(scene);
//
//        stage.show();
//    }
}
