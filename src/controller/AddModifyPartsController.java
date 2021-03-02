/**
 * @author Heaven-Leigh (Michelle) Masters
 */

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;


public class AddModifyPartsController {


    private Part selectedPart;
    private int partIdCounter = 4;   // 4 parts are pre-loaded into Inventory

/** Labels and TextField */

    @FXML
    private Label AddModifyPartsLabel;

    @FXML
    public Label LabelMachineIDCompanyName;

    @FXML
    public TextField TextMinPart;

    @FXML
    public TextField TextMaxPart;

    @FXML
    public TextField TextPriceCostPart;

    @FXML
    public TextField TextInventoryPart;

    @FXML
    public TextField TextPartName;

    @FXML
    public TextField TextPartID;

    @FXML
    private TextField TextMachineCompanyPart;



/** Buttons */
    @FXML
    public Button ButtonSavePart;

    @FXML
    public Button ButtonCancelPart;

    @FXML
    private RadioButton RadioInHousePart;

    @FXML
    private ToggleGroup InHouseOrOutsourced;

    @FXML
    private Button OnPartAddButton;

    @FXML
    private Button OnPartModifyButton;

    @FXML
    private Button OnPartDeleteButton;

/** Table */
    @FXML
    private TableView<?> partsTableView;

    @FXML
    private TableColumn<?, ?> partIDColumn;

    @FXML
    private TableColumn<?, ?> partNameColumn;

    @FXML
    private TableColumn<?, ?> partInventoryLevelColumn;

    @FXML
    private TableColumn<?, ?> partPriceCostPerUnitColumn;

/** Methods */

    /**
     * This method accepts a part from the list to modify and initializes the view of the modify scene.
     * @param part
     */
    public void initData(Part part){
        selectedPart = part;
        TextPartID.setText(String.valueOf(selectedPart.getId()));
        TextPartName.setText(selectedPart.getName());
        TextInventoryPart.setText(String.valueOf(selectedPart.getStock()));
        TextMinPart.setText(String.valueOf(selectedPart.getMin()));
        TextMaxPart.setText(String.valueOf(selectedPart.getMax()));
        TextPriceCostPart.setText(String.valueOf(selectedPart.getPrice()));

        boolean isInHouseInstance = part instanceof InHouse;

        if (isInHouseInstance) {
            TextMachineCompanyPart.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
        }

        else {
            TextMachineCompanyPart.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
        }
    }

    @FXML
    public void OnPartInHouseRadio(ActionEvent actionEvent) {
        LabelMachineIDCompanyName.setText("Machine ID");
    }

    @FXML
    public void OnPartOutsourcedRadio(ActionEvent actionEvent) {
        LabelMachineIDCompanyName.setText("Company Name");
 }

    @FXML
    public void OnPartsSaveButton(ActionEvent actionEvent) {
        System.out.println("This will commit the data to the observable list(view)");
        // check if this is a new part, if so make new part and assign ID
        if (TextPartID.getText().trim().isEmpty()) {
            partIdCounter++;
            Part newPart;
            if (RadioInHousePart.isSelected()) {
                System.out.println("This is a new InHouse Part");
                newPart = new InHouse(partIdCounter, TextPartName.getText(),
                        Integer.parseInt(TextPriceCostPart.getText()),
                        Integer.parseInt(TextInventoryPart.getText()), Integer.parseInt(TextMinPart.getText()),
                        Integer.parseInt(TextMaxPart.getText()),
                        Integer.parseInt(TextMachineCompanyPart.getText()));
            }
            else {
                System.out.println("This is a new Outsourced Part");
                newPart = new Outsourced(partIdCounter, TextPartName.getText(),
                        Integer.parseInt(TextPriceCostPart.getText()),
                        Integer.parseInt(TextInventoryPart.getText()), Integer.parseInt(TextMinPart.getText()),
                        Integer.parseInt(TextMaxPart.getText()), TextMachineCompanyPart.getText());
            }
            Inventory.addPart(newPart);

        }
        else {
            selectedPart.setName(String.valueOf(TextPartName.getText()));
            selectedPart.setStock(Integer.parseInt(TextInventoryPart.getText()));
            selectedPart.setPrice(Double.parseDouble(TextPriceCostPart.getText()));
            selectedPart.setMin(Integer.parseInt(TextMinPart.getText()));
            selectedPart.setMax(Integer.parseInt(TextMaxPart.getText()));
            if(RadioInHousePart.isSelected()){
                InHouse inHousePart = (InHouse)selectedPart;
                inHousePart.setMachineID(Integer.parseInt(TextMachineCompanyPart.getText()));
            }
            else {
                Outsourced outsourcedPart = (Outsourced)selectedPart;
                outsourcedPart.setCompanyName(TextMachineCompanyPart.getText());
            }
        }


       //? partsTableView.getItem()
    }

    @FXML
    public void OnCancelButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Cancel button pressed");

        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage mainStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        mainStage.setTitle("Inventory Management Main");
        mainStage.setScene(new Scene(root, 1500, 975));
        mainStage.show();
    }
}
