/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    }

    @Override
    public void updatePlayerList(ArrayList<String> players) {
        playersList.getItems().clear();
        playersList.getItems().addAll(players);
        
    }

}
