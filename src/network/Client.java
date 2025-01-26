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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.json.JSONObject;

/**
 *
 * @author zeyad_maamoun
 */
public class Client extends Thread {

    //private instance so no one can accesss it directly
    private static Client instance;
    private Socket soc;
    private DataInputStream ear;
    private DataOutputStream mouth;
    private JSONObject obj;
    private LoginUiHandler loginHandler;
    private RegisterUIHandler registerHandler;
    private DashboadrdUiHandler dashboadrdUiHandler;
    private ServerGameHandler serverGameHandler;
    private String userName;
    private int score;
    private boolean isServerAccept = false;
    private boolean clientThread = true;
    private boolean isInGameBoard = false;

    // private constructor so no one can make any new instance from this class.
    private Client() {
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
            while (clientThread) {
                if (isServerAccept) {

                    closingConnection();
                    break;
                }
                String msg = ear.readUTF();
                obj = new JSONObject(msg);
                String command = obj.getString("command");
                int result = 0;
                System.out.println(msg);
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
                                System.out.println("score = " + obj.getInt("score"));
                                score = obj.getInt("score");
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
                    case "players_score":
                        playerScoreHandler();
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
                        isInGameBoard = true;
                        if (obj.getString("playerturn").equals(userName)) {
                            Platform.runLater(() -> {
                                serverGameHandler.startGame();
                                serverGameHandler.playersInfo(obj.getString("playertwoname"), obj.getInt("playeronescore"), obj.getInt("playertwoscore"));
                            });
                        } else {
                            Platform.runLater(() -> {
                                serverGameHandler.playersInfo(obj.getString("playeronename"), obj.getInt("playertwoscore"), obj.getInt("playeronescore"));
                            });

                        }

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
                    case "draw":  //by Mohammed
                        Platform.runLater(() -> {
                            serverGameHandler.drawAction();
                        });
                        break;
                    case "move":
                        Platform.runLater(() -> {
                            System.out.println("------------->" + obj);
                            serverGameHandler.drawMoveFromServer(obj.getInt("row"), obj.getInt("col"), obj.getString("token"));
                        });
                        break;
                    case "exit_game":
                        isInGameBoard = false;
                        Platform.runLater(() -> {
                            serverGameHandler.exitSession();
                        });
                        break;
                    case "acceptclosing": // by mohamed
                        isServerAccept = true;
                        closeConnectionWithServer();
                        break;
                    case "logout_response":
                        System.out.println("logout_response" + "hello ");
                        int status = obj.getInt("status");
                        if (status == 1) {
                            Platform.runLater(() -> {
                                dashboadrdUiHandler.logoutSuccess();
                            });
                        } else {
                            Platform.runLater(() -> {
                                dashboadrdUiHandler.logoutFailed();
                            });
                        }
                        break;

                }
            }
        } catch (IOException ex) {
            try {
                System.out.println("this because server disconnect");
                clientThread = false;
                // closeConnectionWithServer();
                instance = null;
                mouth.close();
                ear.close();
//                soc.shutdownInput();
//                soc.shutdownOutput();
                soc.close();
                System.out.println("socket state + " + soc.isClosed());
                Platform.runLater(() -> {
                    if (isInGameBoard) {
                        serverGameHandler.switchToMainScreen();
                    } else {
                        dashboadrdUiHandler.switchToMainScreen();
                    }

                });

                //Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void connectToServer() throws IOException {
        
        if (soc != null) {
            return;
        }
        //soc = new Socket("192.168.1.4", 5005);
        soc = new Socket("127.0.0.1", 5005);
        ear = new DataInputStream(soc.getInputStream());
        mouth = new DataOutputStream(soc.getOutputStream());
        start();

    }

    public void sendLoginCredientials(String username, String password) {
        try {
            connectToServer();
            JSONObject obj = new JSONObject();
            obj.put("command", "login");
            obj.put("username", username);
            obj.put("password", password);
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Platform.runLater(() -> {
                loginHandler.notifyUserServerIsNotAvailable();
            });
        }
    }

    public void sendRegisterCredientials(String username, String password) {
        try {
            connectToServer();
            JSONObject obj = new JSONObject();
            obj.put("command", "register");
            obj.put("username", username);
            obj.put("password", password);
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Platform.runLater(() -> {
                registerHandler.notifyUserServerIsNotAvailable();
            });
        }
    }

    private void playersListHandler() {

        JSONObject jsonObject = obj.getJSONObject("list");
        Map<String, Integer> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getInt(key));
        }
        // Printing the map
        System.out.println("hi " + map);
        Platform.runLater(() -> {
            dashboadrdUiHandler.updatePlayerList(map);
        });

    }

    private void playerScoreHandler() {
        int score = obj.getInt("score");

        Platform.runLater(() -> {
            dashboadrdUiHandler.updatePlayerScore(score);
        });
    }

    public void requestPlayersList() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "send_list");
            mouth.writeUTF(obj.toString());

        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void requestPlayerScore() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "send_player_score");
            mouth.writeUTF(obj.toString());

        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("test if client send move" + obj.toString());

        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public Socket getSocket() {
        return soc;
    }

    public void exitGame() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "exit_game");
            mouth.writeUTF(obj.toString());

        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendTestMesssage() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "test");
            mouth.writeUTF(obj.toString());

        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void sendRequestClose() { ///////by mohamed
        try {
            JSONObject objClose = new JSONObject();
            objClose.put("command", "closetoleave");
            mouth.writeUTF(objClose.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendLogoutRequest() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "logout_request");
            mouth.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnectionWithServer() {      //by mohamed
        try {
            JSONObject objCloseConnection = new JSONObject();
            objCloseConnection.put("command", "I'm_gone");
            mouth.writeUTF(objCloseConnection.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closingConnection() {
        try {
            mouth.close();
            ear.close();
            soc.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/////////////////////////////////////////Ui Interfaces//////////////////////////////////////////////////////////////
    public interface LoginUiHandler {

        void loginSuccess();

        void LoginFailed();

        void notifyUserServerIsNotAvailable();
    }

    public interface RegisterUIHandler {

        void success();

        void failed();
        
        void notifyUserServerIsNotAvailable();
    }

    public interface DashboadrdUiHandler {

        void updatePlayerList(Map<String, Integer> map);

        void generateRequestPopup(String fromPlayer);

        void updatePlayerScore(int score);

        void generateResponsePopup(String fromPlayer);

        void generateAcceptancePopup(String fromPlayer);

        void switchToGameBoard();

        void switchToMainScreen();

        void logoutSuccess();

        void logoutFailed();
    }

    public interface ServerGameHandler {

        void drawMoveFromServer(int row, int col, String token);

        void startGame();

        void playersInfo(String playerTwoName, int playerOneScore, int playerTwoScore);

        void winnerAction();

        void loseAction();

        void drawAction();

        void exitSession();

        void switchToMainScreen();
    }

}
