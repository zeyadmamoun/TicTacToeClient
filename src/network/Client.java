/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.awt.event.ActionEvent;
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

    Socket soc;
    DataInputStream ear;
    DataOutputStream mouth;
    JSONObject obj;
    private boolean haveAccess = false;
    private LoginUiHandler loginHandler;
    private DashboadrdUiHandler dashboadrdUiHandler;
    //private instance so no one can accesss it directly
    private static Client instance;

    // private constructor so no one can make any new instance from this class.
    private Client() {
        try {
            soc = new Socket("192.168.1.4", 5005);
            //soc = new Socket("127.0.0.1", 5005);
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

    public void setDashboradHandler(DashboadrdUiHandler handler) {
        this.dashboadrdUiHandler = handler;
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
                            System.out.println("user not found");
                        } else {
                            System.out.println("login successfull");
                        }
                        if (loginHandler != null) {
                            Platform.runLater(() -> {
                                loginHandler.loginSuccess();
                            });
                        }
                        break;
                    case "register_response":
                        result = obj.getInt("status");
                        if (result == 0) {
                            System.out.println("registeration failed");
                        } else {
                            System.out.println("registeration successfull");
                        }
                        break;
                    case "players_list":
                        if (dashboadrdUiHandler != null) {
                            System.out.println(obj);
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

    public interface LoginUiHandler {

        void loginSuccess();
    }

    interface RegisterUIHandler {
        
    }

    public interface DashboadrdUiHandler {

        void updatePlayerList(ArrayList<String> players);
    }
}
