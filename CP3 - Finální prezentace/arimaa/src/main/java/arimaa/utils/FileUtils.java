package arimaa.utils;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONTokener;


public class FileUtils {
    public void saveGame(String filename, Map<String, Object> gameState) throws IOException {
        JSONObject json = new JSONObject(gameState);
        FileWriter writer = new FileWriter("game-saves/" + filename);
        writer.write(json.toString());
        writer.close();
    }
    
    public Map<String, Object> loadGame(String filename) throws IOException {
        FileReader reader = new FileReader("game-saves/" + filename);
        JSONObject json = new JSONObject(new JSONTokener(reader));
        Map<String, Object> gameState = json.toMap();
        reader.close();
        return gameState;
    }
}
