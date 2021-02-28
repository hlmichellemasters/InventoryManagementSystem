/**
 * @author Heaven-Leigh (Michelle) Masters
 */

package controller;

import model.Inventory;
import model.Part;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    public TableView partsTableView;

    @FXML
    public Button OnPartAddButton;

    @FXML
    public Button OnPartModifyButton;

    @FXML
    public Button OnPartDeleteButton;

    @FXML
    private TableColumn partIDColumn;

    @FXML
    private TableColumn partNameColumn;

    @FXML
    private TableColumn partInventoryLevelColumn;

    @FXML
    private TableColumn partPriceCostPerUnitColumn;

    @FXML
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    // insert Product Observable List like above.



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set table and column within view

        partsTableView.setItems(Inventory.getAllParts());

        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        System.out.println("Initialized!");

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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/AddModifyPartScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1500, 875);

        AddModifyPartsController controller = loader.getController();
        controller.initData((Part) partsTableView.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Modify Part");
        stage.setScene(scene);

        stage.show();

        // add method to have pop-up saying to select the item to modify.

    }

    public void OnPartDeleteButton(ActionEvent actionEvent) {
    }
}
