/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
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
       
            System.out.println(fromPlayer + "want's to play with you");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.CONFIRMATION);
            a.setContentText(fromPlayer+"want's to play with you");
            a.show();
    }

}
