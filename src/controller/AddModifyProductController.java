package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;

import java.io.IOException;
import java.util.Optional;

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
        private TableColumn addPartInventoryLevelColumn1;

        @FXML
        private TableColumn addPartPriceCostPerUnitColumn1;

        private ObservableList<Part> pickAllParts;

        public void InitPickPartData(ObservableList<Part> allParts) {
            pickAllParts = FXCollections.observableArrayList();
            pickAllParts.addAll(Inventory.getAllParts());
            pickPartsTableView.setItems(pickAllParts);
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

        public void OnPartRemoveFromProductButton(ActionEvent actionEvent) {

        }

        public void OnPartAddToProductButton(ActionEvent actionEvent) {

        }

        public void OnProductSaveButton(ActionEvent actionEvent) {

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
