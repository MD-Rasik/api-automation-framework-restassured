package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;


public class JsonReaderUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads any JSON file and returns it as a 2D Object Array for TestNG DataProviders.
     */
    public static <T> Object[][] getJsonData(String jsonFilePath, Class<T[]> clazz) {
        try {
            // 1. Read the JSON file directly into a temporary generic array
            T[] rawData = mapper.readValue(new File(jsonFilePath), clazz);
            
            // 2. Initialize the 2D Object grid for TestNG
            Object[][] dataMatrix = new Object[rawData.length][];
            
            // 3. Map each generic object row into the TestNG grid
            for (int i = 0; i < rawData.length; i++) {
                dataMatrix[i] = new Object[]{rawData[i]};
            }
            
            // 4. Return the ready-to-use matrix
            return dataMatrix;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data from file: " + jsonFilePath, e);
        }
    }
}
