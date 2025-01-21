package screens;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecordsController implements Initializable {

    @FXML
    public Button buttonOne;
    @FXML
    public Button buttonTwo;
    @FXML
    public Button buttonThree;
    @FXML
    public Button buttonFour;
    @FXML
    public Button buttonFive;
    @FXML
    public Button buttonSix;
    @FXML
    public Button buttonSeven;
    @FXML
    public Button buttonEight;
    @FXML
    public Button buttonNine;
    
    @FXML
    private ListView<String> recordsList;
    
    @FXML
    private void buttonOneHandler(ActionEvent event) {
    }

    @FXML
    private void buttonTwoHandler(ActionEvent event) {
    }

    @FXML
    private void buttonThreeHandler(ActionEvent event) {
    }

    @FXML
    private void buttonFourHandler(ActionEvent event) {
    }

    @FXML
    private void buttonFiveHandler(ActionEvent event) {
    }

    @FXML
    private void buttonSixHandler(ActionEvent event) {
    }

    @FXML
    private void buttonSevenHandler(ActionEvent event) {
    }

    @FXML
    private void buttonEightHandler(ActionEvent event) {
    }

    @FXML
    private void buttonNineHandler(ActionEvent event) {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadGameRecords();
            
            if (recordsList != null) {
                recordsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        String gameId = newVal.split(" - ")[0];
                        Recording.replayMoves(gameId, this);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error initializing records: " + e.getMessage());
        }
    }    
    
    private void loadGameRecords() {
        try {
            if (recordsList != null) {
                JSONArray games = Recording.readFromFile();
                for (int i = 0; i < games.length(); i++) {
                    JSONObject game = games.getJSONObject(i);
                    String gameId = game.getString("gameId");
                    recordsList.getItems().add(gameId +" - Game " + (i + 1));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading game records: " + e.getMessage());
        }
    }
}

