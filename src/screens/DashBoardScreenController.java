/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    ArrayList<String> playersArrayList = new ArrayList<>();
    @FXML
    private ListView<String> playersList;
    @FXML
    private Text mainHeader;
    @FXML
    private Text playerScore;
    @FXML
    private Text footer;
    @FXML
    private Button recordBtn;
    @FXML
    private ImageView crownImage;
    @FXML
    private Text kingScore;
    @FXML
    private Text kingName;
    private int highestScore = 0;
    private String highestScorePlayer;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button logout_btn;
    @FXML
    private Pane Pane;


    /**
     * Initializes the controller class.
     */
    @FXML
    private void navigateToRecording(javafx.event.ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/Records.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage currentStage = (Stage) mainHeader.getScene().getWindow();

        // Set the new scene to the current stage
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("Recording"); // Optional: Change the stage title if needed
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Pane.getStylesheets().add(getClass().getResource("dashboardscreen.css").toExternalForm());
        ImageView imageView;
        Image myImage = new Image(getClass().getResourceAsStream("/assets/crown.png"));
        crownImage.setImage(myImage);
        setupListView();
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
    private Map<String, Integer> playerScores = new HashMap<>();

    private void setupListView() {
        playersList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    int index = getIndex() + 1;
                    int score = playerScores.getOrDefault(item, 0);
                    // Format with index, player name, and score aligned
                    setText(String.format("%-3d %-20s %8d", index, item, score));
                }
            }
        });
    }

    @Override
    public void updatePlayerList(Map<String, Integer> map) {
        playersArrayList.clear();
        playerScores.clear();

        // Reset highest score tracking
        highestScore = 0;
        highestScorePlayer = "";

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String playerName = entry.getKey();
            Integer score = entry.getValue();

            playerScores.put(playerName, score);

            // Update highest score if current score is higher
            if (score > highestScore) {
                highestScore = score;
                highestScorePlayer = playerName;
            }

            if (!client.getUserName().equals(playerName)) {
                playersArrayList.add(playerName);
            }
        }
        kingScore.setText(Integer.toString(highestScore));
        kingName.setText(highestScorePlayer);
        playersList.getItems().clear();
        playersList.getItems().addAll(playersArrayList);

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
        a.setContentText(fromPlayer + " Wants To Play With You");
        ImageView icon = new ImageView(new Image("file:C:\\Users\\zeyad_maamoun\\Downloads"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        a.setGraphic(icon);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());

//         DialogPane dialogPane = a.getDialogPane();
//        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
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
        a.setContentText("Sadly " + toPlayer + " Refused. Please don't cry.");
        ImageView icon = new ImageView(new Image("file:D:/downloads/strategic-plan.png"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        a.setGraphic(icon);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
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
        a.setContentText(toPlayer + " Accepted your request.");
        ImageView icon = new ImageView(new Image("file:D:/downloads/strategic-plan.png"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        a.setGraphic(icon);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
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

    public void switchToModesScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ModesFXML.fxml"));
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
    public void switchToMainScreen() {
        switchToModesScreen();
    }

    @Override
    public void logoutSuccess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ModesFXML.fxml"));
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
    public void logoutFailed() {
        Alert a = new Alert(AlertType.NONE);
        a.initOwner(mainHeader.getScene().getWindow());
        a.setAlertType(AlertType.ERROR);
        a.setContentText("LogOut Failed");
        ImageView icon = new ImageView(new Image("file:D:/downloads/strategic-plan.png"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        a.setGraphic(icon);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
        a.show();
    }

    @FXML
    void logoutButtonHandler() {
        client.sendLogoutRequest();
    }

}
