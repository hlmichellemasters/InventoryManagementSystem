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
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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

    @FXML TextField OnSearchText;

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
    public void OnPartAddButton(@NotNull ActionEvent actionEvent) throws IOException {
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
            alert.setHeaderText("Part not found");
            alert.setContentText("Please select a part in order to modify a part");

            alert.showAndWait();
        }
    }

    /**
     * This method will delete a selected part after getting confirmation from a dialog box.
     * @param actionEvent
     */
    public void OnPartDeleteButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) partsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete item?");
        alert.setContentText("Confirm you want to delete " + selectedPart.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }
}
