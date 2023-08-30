package com.core.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//This class provides methods to load properties from a configuration file

public class ConfigurationManager {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
       // ConfigurationManager.class.getResourceAsStream(CONFIG_FILE);

        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        }
        return properties;
    }
}