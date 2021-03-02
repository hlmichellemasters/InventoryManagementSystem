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
    public Label LabelMachineIDCompanyName;

    @FXML
    public Label AddModifyPartsLabel;

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
    private RadioButton RadioOutsourced;

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
            System.out.println("This part is an in-house part.");
            TextMachineCompanyPart.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
            RadioInHousePart.setSelected(true);
            LabelMachineIDCompanyName.setText("Machine ID");

        }

        else {
            System.out.println("This part is an outsourced part");
            TextMachineCompanyPart.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
            RadioOutsourced.setSelected(true);
            LabelMachineIDCompanyName.setText("Company Name");
        }
    }

    @FXML
    public void OnPartInHouseRadio(ActionEvent actionEvent) {
        LabelMachineIDCompanyName.setText("Machine ID");
        if (!TextPartID.getText().trim().isEmpty()) {
            System.out.println("The outsourced part was made to be in-house");
            selectedPart = makePartInHouse((Outsourced) selectedPart);
        }
    }

    private InHouse makePartInHouse(Outsourced selectedPart) {
        InHouse inHouseNow = new InHouse(selectedPart.getId(), selectedPart.getName(),
                selectedPart.getPrice(), selectedPart.getStock(), selectedPart.getMin(),
                selectedPart.getMax(), 0);
        return inHouseNow;
    }

    @FXML
    public void OnPartOutsourcedRadio(ActionEvent actionEvent) {
        LabelMachineIDCompanyName.setText("Company Name");
        if (!TextPartID.getText().trim().isEmpty()) {
            System.out.println("The In-house part was made to be outsourced");
            selectedPart = makePartOutsourced((InHouse) selectedPart);
        }
 }

    private Outsourced makePartOutsourced(InHouse selectedPart) {
        Outsourced outsourcedNow = new Outsourced(selectedPart.getId(), selectedPart.getName(),
                selectedPart.getPrice(), selectedPart.getStock(), selectedPart.getMin(),
                selectedPart.getMax(), "");
        return outsourcedNow;
    }

    @FXML
    public void OnPartsSaveButton(ActionEvent actionEvent) throws IOException {

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
        // else if the part is not new, set the properties of the part and check if
        // it is an InHouse or Outsourced part to determine whether to save the
        // machine ID or the Company Name.
        else {
            selectedPart.setName(String.valueOf(TextPartName.getText()));
            selectedPart.setStock(Integer.parseInt(TextInventoryPart.getText()));
            selectedPart.setPrice(Double.parseDouble(TextPriceCostPart.getText()));
            selectedPart.setMin(Integer.parseInt(TextMinPart.getText()));
            selectedPart.setMax(Integer.parseInt(TextMaxPart.getText()));
            if(RadioInHousePart.isSelected()){
                InHouse inHousePart = (InHouse) selectedPart;
                inHousePart.setMachineID(Integer.parseInt(TextMachineCompanyPart.getText()));
                Inventory.updatePart((Integer.parseInt(TextPartID.getText())-1), inHousePart);
            }
            else {
                Outsourced outsourcedPart = (Outsourced) selectedPart;
                outsourcedPart.setCompanyName(TextMachineCompanyPart.getText());
                Inventory.updatePart((Integer.parseInt(TextPartID.getText())-1), outsourcedPart);
            }
        }

        loadMain(actionEvent);
    }

    @FXML
    public void OnCancelButton(ActionEvent actionEvent) throws IOException{
        System.out.println("Cancel button pressed");
        loadMain(actionEvent);
    }

    public void loadMain(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../view/Main.fxml"));
        Stage mainStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        mainStage.setTitle("Inventory Management Main");
        mainStage.setScene(new Scene(root, 1500, 975));
        mainStage.show();
    }
}
