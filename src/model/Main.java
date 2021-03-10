/**
 * @author Heaven-Leigh (Michelle) Masters
 */

package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * creates an Inventory Management System app that adds and modifies parts and products.
 */
public class Main extends Application {

    /**
     * this method starts the application. It sets the main scene and starts the show.
     * @param primaryStage passed to set the primary stage for the application
     * @throws Exception in case the scene cannot be set
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/Main.fxml"));
        primaryStage.setTitle("Inventory Management Main");
        primaryStage.setScene(new Scene(root, 1500, 975));
        primaryStage.show();
    }

    /**
     * this method begins the program
     * @param args the arguments passed to begin the program
     */
    public static void main(String[] args) {

        launch(args);
    }
}
