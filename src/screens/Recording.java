package screens;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

public class Recording {

    public static void recordMove(int row, int column, char character, String userName, String gameId) {
        JSONArray gameList = readFromFile();

        JSONObject gameRecord = findGameById(gameList, gameId);
        if (gameRecord == null) {
            gameRecord = new JSONObject();
            gameRecord.put("gameId", gameId);
            gameRecord.put("moves", new JSONArray());
            gameRecord.put("date", new Date());

            gameList.put(gameRecord);
        }

        JSONArray moves = gameRecord.getJSONArray("moves");
        JSONObject move = new JSONObject();
        move.put("userName", userName);
        move.put("row", row);
        move.put("column", column);
        move.put("character", String.valueOf(character));
        moves.put(move);

        saveToFile(gameList);
    }

    private static void saveToFile(JSONArray gameList) {
        try (FileWriter file = new FileWriter("games.json")) {
            file.write(gameList.toString(4));
        } catch (IOException e) {
            System.out.println("Error in recording: " + e.getMessage());
        }
    }

    public static JSONArray readFromFile() {
        try (FileReader reader = new FileReader("games.json");
                Scanner scanner = new Scanner(reader)) {

            StringBuilder jsonData = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonData.append(scanner.nextLine());
            }

            return new JSONArray(jsonData.toString());
        } catch (IOException e) {
            System.out.println("No existing file for games. Creating a new one: " + e.getMessage());
            return new JSONArray();
        } catch (Exception e) {
            System.out.println("Error in parsing JSON for games: " + e.getMessage());
            return new JSONArray();
        }
    }

    public static JSONArray getMovesForGame(String gameId) {
        JSONArray gameList = readFromFile();
        JSONObject gameRecord = findGameById(gameList, gameId);
        if (gameRecord != null) {
            return gameRecord.getJSONArray("moves");
        }
        return new JSONArray(); 
    }

    private static JSONObject findGameById(JSONArray gameList, String gameId) {
        for (int i = 0; i < gameList.length(); i++) {
            JSONObject gameRecord = gameList.getJSONObject(i);
            if (gameRecord.optString("gameId").equals(gameId)) {
                return gameRecord;
            }
        }
        return null;
    }

    public static void replayMoves(String gameId, RecordsController controller) {
        new Thread(() -> {
            try {
                JSONArray gameList = readFromFile();
                JSONObject gameRecord = findGameById(gameList, gameId);

                if (gameRecord != null) {
                    JSONArray moves = gameRecord.getJSONArray("moves");

                    // Clear the board first
                    Platform.runLater(() -> {
                        controller.buttonOne.setText("");
                        controller.buttonTwo.setText("");
                        controller.buttonThree.setText("");
                        controller.buttonFour.setText("");
                        controller.buttonFive.setText("");
                        controller.buttonSix.setText("");
                        controller.buttonSeven.setText("");
                        controller.buttonEight.setText("");
                        controller.buttonNine.setText("");
                    });

                    // Play each move with delay
                    for (int i = 0; i < moves.length(); i++) {
                        JSONObject move = moves.getJSONObject(i);
                        final int row = move.getInt("row");
                        final int column = move.getInt("column");
                        final String character = move.getString("character");

                        Thread.sleep(1000); // 2 second delay

                        Platform.runLater(() -> {
                            // Convert row/column to button number (0-based to 1-based)
                            int buttonNum = (row * 3) + column;
                            switch (buttonNum) {
                                case 0:
                                    controller.buttonOne.setText(character);
                                    break;
                                case 1:
                                    controller.buttonTwo.setText(character);
                                    break;
                                case 2:
                                    controller.buttonThree.setText(character);
                                    break;
                                case 3:
                                    controller.buttonFour.setText(character);
                                    break;
                                case 4:
                                    controller.buttonFive.setText(character);
                                    break;
                                case 5:
                                    controller.buttonSix.setText(character);
                                    break;
                                case 6:
                                    controller.buttonSeven.setText(character);
                                    break;
                                case 7:
                                    controller.buttonEight.setText(character);
                                    break;
                                case 8:
                                    controller.buttonNine.setText(character);
                                    break;
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Replay interrupted: " + e.getMessage());
            }
        }).start();
    }
}
