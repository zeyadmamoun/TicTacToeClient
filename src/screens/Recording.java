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

    public static void saveToFile(JSONArray gameList) {
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

    public static JSONObject findGameById(JSONArray gameList, String gameId) {
        for (int i = 0; i < gameList.length(); i++) {
            JSONObject gameRecord = gameList.getJSONObject(i);
            if (gameRecord.optString("gameId").equals(gameId)) {
                return gameRecord;
            }
        }
        return null;
    }

    
}
