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
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class ModesFXMLController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button online_btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    private void navigateToLoginPage(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LoginScreenFXML.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToVScomputerMode(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LevelsScreen.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToLocalMode(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LocalMode.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToRecording(javafx.event.ActionEvent event) throws IOException {
        // Load the second FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LocalRecordController.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
