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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;
import network.Client.LoginUiHandler;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class LoginScreenFXMLController implements Initializable, LoginUiHandler {
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    Client client;
    @FXML
    private TextField user_login;
    @FXML
    private TextField password_login;
    @FXML
    private Button login_btn;
    @FXML
    private Button signup_btn;
    @FXML
    private Label result_L;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        client.setLoginHandler(this);
        client.connectToServer();
    }    
    
    @FXML
    public void login(){
        try {
            client.sendLoginCredientials(user_login.getText(), password_login.getText());
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void switchToSignup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/RegisterScreen.fxml"));
        root = loader.load();
        
        // Get the current stage and set the new scene
        stage = (Stage) login_btn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void loginSuccess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/DashBoardScreen.fxml"));
            root = loader.load();
            // Get the current stage and set the new scene
            stage = (Stage) login_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void LoginFailed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");

        alert.showAndWait();
    }

    
}
