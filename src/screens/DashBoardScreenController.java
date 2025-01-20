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
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;
import network.Client;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class DashBoardScreenController implements Initializable, Client.DashboadrdUiHandler {

    Client client;
    String toPlayer;
    private String requestingPlayer;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int score;
    @FXML
    private ListView<String> playersList;
    @FXML
    private Text mainHeader;
    @FXML
    private Text playerScore;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        client.setDashboradHandler(this);
        score = client.getScore();
        //playerScore.setText(Integer.toString(score));
        mainHeader.setText(client.getUserName());
        client.requestPlayersList();
        client.requestPlayerScore();

        playersList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    toPlayer = newValue;
                    client.sendRequestHandler(toPlayer);
                    Platform.runLater(() -> {
                        playersList.getSelectionModel().clearSelection();
                    });
                }
            }
        });

    }

    @FXML
    void onTestButtonClicked() {
        System.err.println("test button clicked");
        client.sendTestMesssage();
    }

    @Override
    public void updatePlayerList(ArrayList<String> players) {
        playersList.getItems().clear();
        players.remove(client.getUserName());
        playersList.getItems().addAll(players);

    }

    @Override
    public void updatePlayerScore(int score) {
        this.score = score;
        System.out.println("player score = " + this.score);
        playerScore.setText(Integer.toString(score));

    }

    @Override
    public void generateRequestPopup(String fromPlayer) {
        System.out.println(fromPlayer + " wants to play with you");
        this.requestingPlayer = fromPlayer;
        Alert a = new Alert(AlertType.NONE);
        a.initOwner(mainHeader.getScene().getWindow());
        a.setAlertType(AlertType.CONFIRMATION);
        a.setContentText(fromPlayer + " wants to play with you");
        final boolean[] autoClosed = {false};
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            if (a.isShowing()) {
                autoClosed[0] = true;
                a.close(); 
                client.sendRefuseToPlayer(client.getUserName(), requestingPlayer);
                System.out.println("from player " + fromPlayer);
                System.out.println("to player " + toPlayer);
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
        Optional<ButtonType> result = a.showAndWait();
        // Stop the timeline if the user responds before timeout
        timeline.stop();

        // Handle the user's response only if it wasn't auto-closed
        if (!autoClosed[0]) {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Handle accept
                client.sendAcceptToPlayer(client.getUserName(), requestingPlayer);
                switchToServerGameBoard();
            } else {
                // Handle refuse
                client.sendRefuseToPlayer(client.getUserName(), requestingPlayer);
                System.out.println("from player " + fromPlayer);
                System.out.println("to player " + toPlayer);
            }
        }
    }

    @Override
    public void generateResponsePopup(String fromPlayer) {
        Alert a = new Alert(AlertType.NONE);
        a.initOwner(mainHeader.getScene().getWindow());
        a.setAlertType(AlertType.INFORMATION);
        a.setContentText("Sadly " + toPlayer + " refused. Please don't cry.");
        a.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            if (a.isShowing()) {
                a.close();
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @Override
    public void generateAcceptancePopup(String fromPlayer) {
        Alert a = new Alert(AlertType.NONE);
        a.initOwner(mainHeader.getScene().getWindow());
        a.setAlertType(AlertType.INFORMATION);
        a.setContentText(toPlayer + " accepted your request.");
        a.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (a.isShowing()) {
                a.close();
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
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

    @Override
    public void switchToGameBoard() {
        switchToServerGameBoard();
    }
}
