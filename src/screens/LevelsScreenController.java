/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class LevelsScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void navigateToVScomputerHard(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/VSComputerScreen.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToVScomputerEasy(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/VSComputerEasy.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToVScomputerIntermediate(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/VSComputerIntermediate.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goBack(javafx.event.ActionEvent event) throws IOException {
        // Load the previous screen (e.g., MainMenu.fxml or another screen)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ModesFXML.fxml"));  // Adjust the path as needed
        root = loader.load();

        // Get the current stage (window) and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
