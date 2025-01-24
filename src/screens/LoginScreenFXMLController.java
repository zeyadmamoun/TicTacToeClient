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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.Client;
import network.Client.LoginUiHandler;

public class LoginScreenFXMLController implements Initializable, LoginUiHandler {

    private Stage stage;
    private Parent root;

    Client client;
    @FXML
    private TextField user_login;
    @FXML
    private TextField password_login;
    @FXML
    private Button login_btn1;
    @FXML
    private Button signup_btn;
    @FXML
    private Label result_L;
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        client.setLoginHandler(this);
        client.connectToServer();

        // Apply the stylesheet
        anchorPane.getStylesheets().add(getClass().getResource("loginscreenfxml.css").toExternalForm());

        // Set cursors for buttons
        login_btn1.setCursor(Cursor.HAND);
        signup_btn.setCursor(Cursor.HAND);
    }

    @FXML
    public void login() {
        try {
            client.sendLoginCredientials(user_login.getText(), password_login.getText());
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void switchToSignup() {
        try {
            root = FXMLLoader.load(getClass().getResource("/screens/RegisterScreen.fxml"));
            stage = (Stage) signup_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            showAlert("Navigation Error", "Unable to load the signup screen.");
            ex.printStackTrace();
        }
    }

    @Override
    public void loginSuccess() {
        try {
            root = FXMLLoader.load(getClass().getResource("/screens/DashBoardScreen.fxml"));
            stage = (Stage) login_btn1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
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
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
