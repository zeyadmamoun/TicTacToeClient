/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private AnchorPane APane;
    @FXML
    private Button record_btn;
    @FXML
    private Button vscomputer_btn;
    @FXML
    private Button local_btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        APane.getStylesheets().add(getClass().getResource("/screens/modesfxml.css").toExternalForm());

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/Records.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void handleMouseExited(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-border-color: #00ffff; "
                + "-fx-background-color: #01002a; "
                + "-fx-border-radius: 10px; "
                + "-fx-text-fill: #00ffff; "
                + "-fx-effect: dropshadow(gaussian, #00ffff, 10, 0, 0, 0);");
    }

    private void handleMouseEntered(javafx.scene.input.MouseEvent event) {
         Button btn = (Button) event.getSource();
        btn.setStyle("-fx-border-color: #ffffff; "
                + "-fx-background-color: #01002a; "
                + "-fx-border-radius: 10px; "
                + "-fx-text-fill: white;");
    }
}
