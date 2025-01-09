/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class RegisterScreenController implements Initializable {
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    Client client;
    
    @FXML
    private Label passssw;
    @FXML
    private TextField user_signup_f;
    @FXML
    private TextField password_signup_f;
    @FXML
    private TextField c_password_signup_f;
    @FXML
    private Label result_signup;
    @FXML
    private Label result_signup1;
    @FXML
    private Button login_btn_s;
    @FXML
    private Button signup_btn_s;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
    }   
    
    @FXML
    public void signUp(){
        try {
            client.sendRegisterCredientials(user_signup_f.getText(),password_signup_f.getText());
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
    
}
