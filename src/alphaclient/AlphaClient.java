/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alphaclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import network.Client;

/**
 *
 * @author zeyad_maamoun
 */
public class AlphaClient extends Application {

    private Client client;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/screens/ModesFXML.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe");
        stage.setHeight(711);
        stage.setWidth(1000);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            client = Client.getInstance();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Tic Tac Toe Game");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("choose ok to exit and cancel if you want to continue");
            ImageView icon = new ImageView(new Image("file:D:/downloads/strategic-plan.png"));
            icon.setFitWidth(50);
            icon.setFitHeight(50);
            alert.setGraphic(icon);
            alert.initOwner(stage.getScene().getWindow());

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/screens/alert.css").toExternalForm());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                if (client.getSocket() != null) {
                    client.sendRequestClose();
                }
                stage.close();
            } else {
                event.consume();
            }
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
