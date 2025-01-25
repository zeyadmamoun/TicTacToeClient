/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.Client;
import network.Client.RegisterUIHandler;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class RegisterScreenController implements Initializable, RegisterUIHandler {

    private Stage stage;
    private Scene scene;
    private Parent root;

    Client client;

    @FXML
    private Label passssw;
    @FXML
    private TextField user_signup_f;
    @FXML
    private PasswordField password_signup_f;
    @FXML
    private PasswordField c_password_signup_f;
    @FXML
    private Label result_signup;
    @FXML
    private Label result_signup1;
    @FXML
    private Button login_btn_s;
    @FXML
    private Button signup_btn_s;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        client.setRegisterHandler(this);
        anchorPane.getStylesheets().add(getClass().getResource("registerscreenstyle.css").toExternalForm());
        login_btn_s.setCursor(Cursor.HAND);
        signup_btn_s.setCursor(Cursor.HAND);
    }

    @FXML
    public void signUp() {

        if (!password_signup_f.getText().equals(c_password_signup_f.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Signup Failed");
            alert.setHeaderText("check your credientials");
            alert.setContentText("Your Password Or Confirm Password Wrong !");
            alert.showAndWait();
            return;

        } else if (user_signup_f.getText().isEmpty() || c_password_signup_f.getText().isEmpty() || password_signup_f.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Signup Failed");
            alert.setHeaderText("check your credientials");
            alert.setContentText("You Have To Enter Your Information !");
            alert.showAndWait();
            return;
        }
        try {
            client.sendRegisterCredientials(user_signup_f.getText(), password_signup_f.getText());
        } catch (IOException ex) {
            Logger.getLogger(RegisterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void switchToLogin(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LoginScreenFXML.fxml"));
        root = loader.load();
        // Get the current stage and set the new scene
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void success() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/DashBoardScreen.fxml"));
            root = loader.load();
            // Get the current stage and set the new scene
            stage = (Stage) signup_btn_s.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void failed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Signup Failed");
        alert.setHeaderText("check your credientials");
        alert.setContentText("something wrong happend !");

        alert.showAndWait();
    }

}