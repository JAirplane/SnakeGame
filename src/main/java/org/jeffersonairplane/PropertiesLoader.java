package org.jeffersonairplane;

import java.io.*;
import java.util.*;

public class PropertiesLoader {

    public static Properties getProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
          .getClassLoader()
          .getResourceAsStream("application.properties");
        configuration.load(inputStream);
        if(inputStream != null) {
            inputStream.close();
        }
        return configuration;
    }
}