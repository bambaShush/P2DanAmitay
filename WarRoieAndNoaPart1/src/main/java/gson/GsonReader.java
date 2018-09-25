package gson;

import com.google.gson.Gson;
import gson.entities.War;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GsonReader implements JsonReaderFacade {
    @Override
    public War readJson() {
        File file = new File("properties.json");
        try {
            FileReader fr = new FileReader(file);
            Gson gson = new Gson();
            War war = gson.fromJson(fr, War.class);

            return war;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
