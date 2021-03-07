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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;


public class AddModifyPartsController {


    private Part selectedPart;
    private int partIdCounter = 4;   // 4 parts are pre-loaded into Inventory

    /**
     * Labels and TextField
     */

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


    /**
     * Buttons
     */
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

    /**
     * Table
     */
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
     *
     * @param part
     */
    public void initData(Part part) {
        selectedPart = part;
        TextPartID.setText(String.valueOf(selectedPart.getId()));
        TextPartName.setText(selectedPart.getName());
        TextInventoryPart.setText(String.valueOf(selectedPart.getStock()));
        TextMinPart.setText(String.valueOf(selectedPart.getMin()));
        TextMaxPart.setText(String.valueOf(selectedPart.getMax()));
        TextPriceCostPart.setText(String.valueOf(selectedPart.getPrice()));

        if (part instanceof InHouse) {  // check if part is an instance of Inhouse
            System.out.println("This part is an in-house part.");
            TextMachineCompanyPart.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
            RadioInHousePart.setSelected(true);
            LabelMachineIDCompanyName.setText("Machine ID");

        } else {  // part is an instance of Outsourced
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

        // Check if any of the fields are empty and throw an error dialog if so.
        if (TextPartName.getText().isBlank() || TextPriceCostPart.getText().isBlank() ||
                TextInventoryPart.getText().isBlank() || TextMinPart.getText().isBlank() ||
                TextMaxPart.getText().isBlank() || TextMachineCompanyPart.getText().isBlank()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields Found");
            alert.setContentText("Please enter appropriate information into every field");

            alert.showAndWait();
        }
        try {
            Double.parseDouble(TextPriceCostPart.getText());
            Integer.parseInt(TextInventoryPart.getText());
            Integer.parseInt(TextMinPart.getText());
            Integer.parseInt(TextMaxPart.getText());

        } catch (NumberFormatException nfe) {
            showErrorMessage(nfe, "Error in a numeric field", "Enter only numbers in the Price/Cost," +
                    "Inventory/Stock, Min, and Max fields.");
        }

        String partName = TextPartName.getText();
        double priceCostPart = Double.parseDouble(TextPriceCostPart.getText());
        int inventoryPart = Integer.parseInt(TextInventoryPart.getText());
        int minPart = Integer.parseInt(TextMinPart.getText());
        int maxPart = Integer.parseInt(TextMaxPart.getText());

        if (minPart >= maxPart || inventoryPart < minPart || inventoryPart > maxPart) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Inventory Error");
            alert.setContentText("Inventory / Stock must be less than max and more than min, " +
                    "and max must be more than min.");
            alert.showAndWait();
        }
            // check if this is a new part, if so make new part and assign ID
            if (TextPartID.getText().trim().isEmpty()) {

                partIdCounter++;
                Part newPart;

                if (RadioInHousePart.isSelected()) {
                    System.out.println("This is a new InHouse Part");

                    try {
                        Integer.parseInt(TextMachineCompanyPart.getText());

                    } catch (NumberFormatException e) {
                        Alert machineIDAlert = new Alert(Alert.AlertType.ERROR);
                        machineIDAlert.setTitle("Error");
                        machineIDAlert.setHeaderText("Machine ID Error");
                        machineIDAlert.setContentText("In-House items need to have numerical Machine IDs.");
                        machineIDAlert.showAndWait();
                    }

                    newPart = new InHouse(partIdCounter, partName, priceCostPart, inventoryPart, minPart,
                            maxPart, Integer.parseInt(TextMachineCompanyPart.getText()));
                } else {
                    System.out.println("This is a new Outsourced Part");

                    newPart = new Outsourced(partIdCounter, partName, priceCostPart, inventoryPart, minPart,
                            maxPart, TextMachineCompanyPart.getText());
                }

                Inventory.addPart(newPart);

            }
            // else if the part is not new, set the properties of the part and check if
            // it is an InHouse or Outsourced part to determine whether to save the
            // machine ID or the Company Name.
            else {
                selectedPart.setName(partName);
                selectedPart.setStock(inventoryPart);
                selectedPart.setPrice(priceCostPart);
                selectedPart.setMin(minPart);
                selectedPart.setMax(maxPart);

                if (RadioInHousePart.isSelected()) {
                    InHouse inHousePart = (InHouse) selectedPart;
                    inHousePart.setMachineID(Integer.parseInt(TextMachineCompanyPart.getText()));
                    Inventory.updatePart((Integer.parseInt(TextPartID.getText()) - 1), inHousePart);
                } else {
                    Outsourced outsourcedPart = (Outsourced) selectedPart;
                    outsourcedPart.setCompanyName(TextMachineCompanyPart.getText());
                    Inventory.updatePart((Integer.parseInt(TextPartID.getText()) - 1), outsourcedPart);
                }
            }

            loadMain(actionEvent);
        }


    @FXML
    public void OnCancelButton(ActionEvent actionEvent) throws IOException {
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

    public static void showErrorMessage(Exception e, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error occured");
        alert.setHeaderText(title);
        alert.setContentText(content);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
