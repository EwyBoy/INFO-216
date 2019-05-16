package json;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileManager {

    //Handle JSON file though this class

    public FileManager(String fileName) {

        try {
            FileReader file = new FileReader(fileName + ".json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
