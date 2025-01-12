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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import network.Client;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class DashBoardScreenController implements Initializable, Client.DashboadrdUiHandler {

    Client client;

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
                String selectedPlayer = playersList.getSelectionModel().getSelectedItem();
                client.sendRequestHandler(selectedPlayer);
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
        System.out.println(fromPlayer + " wants to play with you");
        Alert requestAlert = new Alert(AlertType.CONFIRMATION);
        requestAlert.setContentText(fromPlayer + " wants to play with you. Do you accept?");
        requestAlert.setTitle("Game Request");
        requestAlert.setHeaderText(null);

        Optional<ButtonType> result = requestAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            client.sendAcceptToPlayer(fromPlayer, client.getUserName());

            Alert acceptAlert = new Alert(AlertType.INFORMATION);
            acceptAlert.setContentText("Your request has been accepted ");
            acceptAlert.setTitle("Game Request Accepted");
            acceptAlert.setHeaderText(null);
            acceptAlert.show();
        } else {

        }
    }

    @Override
    public void generateAcceptancePopup(String fromPlayer) {
        Alert a = new Alert(AlertType.NONE);
        a.setAlertType(AlertType.INFORMATION);
        a.show();

    }

}
