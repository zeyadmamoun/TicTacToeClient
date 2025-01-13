/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.Client;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class DashBoardScreenController implements Initializable, Client.DashboadrdUiHandler {

    Client client;
    String toPlayer;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ListView<String> playersList;
    @FXML
    private Text mainHeader;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        client.setDashboradHandler(this);
        mainHeader.setText(client.getUserName());
        playersList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                toPlayer = playersList.getSelectionModel().getSelectedItem();
                client.sendRequestHandler(toPlayer);

            }

        });
    }

    @Override
    public void updatePlayerList(ArrayList<String> players) {
        playersList.getItems().clear();
        players.remove(client.getUserName());
        playersList.getItems().addAll(players);

    }

    @Override
    public void generateRequestPopup(String fromPlayer) {

        System.out.println(fromPlayer + "want's to play with you");
        Alert a = new Alert(AlertType.NONE);
        a.setTitle(client.getUserName());
        a.setAlertType(AlertType.CONFIRMATION);
        a.setContentText(fromPlayer + "want's to play with you");
        //a.show();
        Optional<ButtonType> result = a.showAndWait();
        // Handle the user's response
        if (result.isPresent() && result.get() == ButtonType.OK) {
           //handle accept
            client.sendAcceptToPlayer(fromPlayer,client.getUserName());
            //switchToServerGameBoard();
        } else {
            //handle refuse
            client.sendRefuseToPlayer(fromPlayer,toPlayer);
           
        }
    }
    
    @Override
    public void generateResponsePopup(String fromPlayer) {

        Alert a = new Alert(AlertType.NONE);
        a.setTitle(client.getUserName());
        a.setAlertType(AlertType.INFORMATION);
        a.setContentText("sadly " + toPlayer + " refused pls don't cry");
        a.show();
    }

    @Override
    public void generateAcceptancePopup(String fromPlayer) {
        Alert a = new Alert(AlertType.NONE);
        a.setTitle(client.getUserName());
        a.setAlertType(AlertType.INFORMATION);
        a.setContentText("" + toPlayer + " accept your request");
        a.show();
    }

    public void switchToServerGameBoard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ServerGameBoard.fxml"));
            root = loader.load();
            // Get the current stage and set the new scene
            stage = (Stage) mainHeader.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
