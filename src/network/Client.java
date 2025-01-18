/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import screens.LoginScreenFXMLController;

/**
 *
 * @author zeyad_maamoun
 */
public class Client extends Thread {

    private boolean isFirstPlay = true;
    private Socket soc;
    private DataInputStream ear;
    private DataOutputStream mouth;
    private JSONObject obj;
    private boolean haveAccess = false;
    private LoginUiHandler loginHandler;
    private RegisterUIHandler registerHandler;
    private DashboadrdUiHandler dashboadrdUiHandler;
    private ServerGameHandler serverGameHandler;
    //private instance so no one can accesss it directly
    private static Client instance;
    private String userName;

    // private constructor so no one can make any new instance from this class.
    private Client() {
        try {
            //soc = new Socket("192.168.1.4", 5005);
            soc = new Socket("127.0.0.1", 5005);
            ear = new DataInputStream(soc.getInputStream());
            mouth = new DataOutputStream(soc.getOutputStream());
            start();
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // function to get the single instance of client.
    static public synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void setLoginHandler(LoginUiHandler handler) {
        this.loginHandler = handler;
    }

    public void setRegisterHandler(RegisterUIHandler handler) {
        this.registerHandler = handler;
    }

    public void setDashboradHandler(DashboadrdUiHandler handler) {
        this.dashboadrdUiHandler = handler;
    }

    public void setServerGameHandler(ServerGameHandler handler) {
        this.serverGameHandler = handler;
        System.out.println("created");
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = ear.readUTF();
                obj = new JSONObject(msg);
                String command = obj.getString("command");
                int result = 0;

                switch (command) {
                    case "login_response":
                        result = obj.getInt("status");
                        if (result == 0) {
                            Platform.runLater(() -> {
                                loginHandler.LoginFailed();
                            });
                            System.out.println("user not found");
                        } else {
                            userName = obj.getString("username");
                            if (loginHandler != null) {
                                Platform.runLater(() -> {
                                    loginHandler.loginSuccess();
                                });
                            }
                            System.out.println("login successfull" + userName);
                        }
                        break;
/////////////////////////////////////////////////////////////////////////////////////////////////////
                    case "register_response":
                        result = obj.getInt("status");
                        if (result == 0) {
                            Platform.runLater(() -> {
                                registerHandler.failed();
                            });
                            System.out.println("registeration failed");
                        } else {
                            userName = obj.getString("username");
                            Platform.runLater(() -> {
                                registerHandler.success();
                            });
                            System.out.println("registeration successfull");
                        }
                        break;
/////////////////////////////////////////////////////////////////////////////////////////////////////  
                    case "players_list":
                        playersListHandler();
                        break;
/////////////////////////////////////////////////////////////////////////////////////////////////////
                    case "requestToPlay":
                        System.out.println(obj.getString("player1"));
                        System.out.println(obj.getString("player2"));

                        Platform.runLater(() -> {
                            dashboadrdUiHandler.generateRequestPopup(obj.getString("player1"));
                        });
                        break;
/////////////////////////////////////////////////////////////////////////////////////////////////////
                    case "playerResponse":
                        int response = obj.getInt("response");
                        String fromPlayer = obj.getString("fromplayer");
                        Platform.runLater(() -> {
                            if (response == 1) {
                                // Accepted
                                Platform.runLater(() -> {
                                    dashboadrdUiHandler.generateAcceptancePopup(fromPlayer);
                                    dashboadrdUiHandler.switchToGameBoard();
                                });
                            } else {
                                Platform.runLater(() -> {
                                    dashboadrdUiHandler.generateResponsePopup(obj.getString("fromplayer"));
                                });
                            }
                        });

                        break;
                    case "start":
                        Platform.runLater(() -> {
                            serverGameHandler.startGame();
                        });
                        break;

                    case "win":
                        System.err.println("someone won");
                        if (obj.getString("winner").equals(userName)) {
                            Platform.runLater(() -> {
                                serverGameHandler.winnerAction();
                            });
                        } else {
                            Platform.runLater(() -> {
                                serverGameHandler.loseAction();
                            });
                        }
                        break;
                    case "move":
                        Platform.runLater(() -> {
                            System.out.println("------------->" + obj);
                            serverGameHandler.drawMoveFromServer(obj.getInt("row"), obj.getInt("col"), obj.getString("token"));
                        });
                        break;
                    case "exit_game":
                        Platform.runLater(() -> {
                            serverGameHandler.exitSession();
                        });
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendLoginCredientials(String username, String password) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("command", "login");
        obj.put("username", username);
        obj.put("password", password);
        mouth.writeUTF(obj.toString());
    }

    public void sendRegisterCredientials(String username, String password) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("command", "register");
        obj.put("username", username);
        obj.put("password", password);
        mouth.writeUTF(obj.toString());
    }

    private void playersListHandler() {
        JSONArray jsonArray = obj.getJSONArray("list");
        ArrayList<String> players = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String element = jsonArray.getString(i);
            players.add(element);
        }
        Platform.runLater(() -> {
            dashboadrdUiHandler.updatePlayerList(players);
        });
    }
    
    public void requestPlayersList(){
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "send_list");
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendRequestHandler(String toPlayer) {
        JSONObject obj = new JSONObject();
        obj.put("command", "requestToPlay");
        obj.put("player1", userName);
        obj.put("player2", toPlayer);
        try {
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendRefuseToPlayer(String fromPlayer, String toPlayer) {
        JSONObject obj = new JSONObject();
        obj.put("command", "playerResponse");
        obj.put("response", 0);
        obj.put("fromplayer", fromPlayer);
        obj.put("toplayer", toPlayer);
        try {
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendAcceptToPlayer(String fromPlayer, String toPlayer) {
        JSONObject obj = new JSONObject();
        obj.put("command", "playerResponse");
        obj.put("response", 1);
        obj.put("fromplayer", fromPlayer);
        obj.put("toplayer", toPlayer);
        try {
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized public void sendMoveToServer(int row, int col, String token) {
        JSONObject obj = new JSONObject();
        obj.put("command", "move");
        obj.put("token", token);
        obj.put("row", row);
        obj.put("col", col);
        try {
            mouth.writeUTF(obj.toString());
            if (isFirstPlay) {
                mouth.writeUTF(obj.toString());
                isFirstPlay = false;
            }
            System.out.println("test if client send move" + obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void exitGame() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "exit_game");
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendTestMesssage() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "test");
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/////////////////////////////////////////Ui Interfaces//////////////////////////////////////////////////////////////
    public interface LoginUiHandler {

        void loginSuccess();

        void LoginFailed();
    }

    public interface RegisterUIHandler {

        void success();

        void failed();
    }

    public interface DashboadrdUiHandler {

        void updatePlayerList(ArrayList<String> players);

        void generateRequestPopup(String fromPlayer);

        void generateResponsePopup(String fromPlayer);

        void generateAcceptancePopup(String fromPlayer);

        void switchToGameBoard();
    }

    public interface ServerGameHandler {

        void drawMoveFromServer(int row, int col, String token);

        void startGame();

        void winnerAction();

        void loseAction();

        void exitSession();

    }

}
