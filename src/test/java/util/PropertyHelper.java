package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by grago on 25.10.17.
 */
public class PropertyHelper {

    private static Properties properties = null;
    private static String[] fileList = {"src/test/resources/credentials.properties",
            "src/test/resources/config.properties"};

    private static void loadProperties() {

        properties = new Properties();

        for (String file : fileList) {
            try {
                FileInputStream in = new FileInputStream(file);
                properties.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String propertyKey) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(propertyKey);
    }

}
