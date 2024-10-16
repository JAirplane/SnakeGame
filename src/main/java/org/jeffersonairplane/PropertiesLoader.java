package org.jeffersonairplane;

import java.io.*;
import java.util.*;

/**
* Returns project properties.
*/
public class PropertiesLoader {

	/**
	* Returns {@link Properties} from application.properties file.
	* @return Properties to work with.
	*/
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
	
	/**
	* Returns {@link Properties} from file.
	* @param fileName is a name of file with properties.
	* @return Properties to work with.
	*/
    public static Properties getProperties(String fileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
          .getClassLoader()
          .getResourceAsStream(fileName);
        configuration.load(inputStream);
        if(inputStream != null) {
            inputStream.close();
        }
        return configuration;
    }
}