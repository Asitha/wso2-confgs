import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utils {

    public static Map getReadMap(String path) throws MojoExecutionException {

        if (Paths.get(path).toFile().exists()) {
            Gson gson = new Gson();
            try (FileInputStream fileInputStream = new FileInputStream(path)) {
                Reader input = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                return gson.fromJson(input, Map.class);
            } catch (IOException e) {
                throw new MojoExecutionException("Error while reading json file", e);
            }
        } else {
            return new HashMap();
        }
    }

    public static String convertIntoJson(Map input) {

        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        return gson.toJson(input);
    }

    public static void mergeMaps(Map<String, Object> input, Map<String, Object> output) {

        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object returnedValue = output.get(key);
            if (returnedValue == null) {
                output.put(key, value);
            } else if (returnedValue instanceof Map) {
                mergeMaps((Map<String, Object>) value, (Map<String, Object>) returnedValue);
            } else if (returnedValue instanceof List) {
                mergeLists((List) value, (List) returnedValue);
            }
        }

    }

    private static void mergeLists(List input, List output) {

        for (Object in : input) {
            if (!output.contains(in)) {
                output.add(in);
            }
        }

    }

}
