package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class ModesFXMLController implements Initializable {

    private Stage stage;
    private Parent root;
    private MediaPlayer mediaPlayer; 

    // Set the MediaPlayer instance
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play the media when this page is initialized
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void navigateToLoginPage(javafx.event.ActionEvent event) throws IOException {
        stopMusic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LoginScreenFXML.fxml"));
        root = loader.load();
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToVScomputerMode(javafx.event.ActionEvent event) throws IOException {
        stopMusic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LevelsScreen.fxml"));
        root = loader.load();
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToLocalMode(javafx.event.ActionEvent event) throws IOException {
        stopMusic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LocalMode.fxml"));
        root = loader.load();
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToRecording(javafx.event.ActionEvent event) throws IOException {
        stopMusic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LocalRecordController.fxml"));
        root = loader.load();
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
