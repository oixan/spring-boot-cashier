package com.oixan.stripecashier.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public class StripeProperties {

    private static final String YAML_FILE = "application.yml";
    private static final String PROPERTIES_FILE = "application.properties";
    private static Map<String, Object> config = new HashMap<>();

    static {
        try {
            InputStream yamlStream = StripeProperties.class.getClassLoader().getResourceAsStream(YAML_FILE);
            if (yamlStream != null) {
                Yaml yaml = new Yaml();
                config = yaml.load(yamlStream);
            } else {
                InputStream propertiesStream = StripeProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
                if (propertiesStream != null) {
                    Properties properties = new Properties();
                    properties.load(propertiesStream);
                    for (String key : properties.stringPropertyNames()) {
                        config.put(key, properties.getProperty(key));
                    }
                } else {
                    throw new IOException("Neither " + YAML_FILE + " nor " + PROPERTIES_FILE + " found in classpath");
                }
            }
            //System.out.println("Configuration Loaded: " + config);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }
    
    public static StripeProperties instance() {
    	return new StripeProperties();
    }

    public static String getApiKey() {
        if (config.containsKey("stripe.apiKey")) {
            return (String) config.get("stripe.apiKey");
        } else if (config.containsKey("stripe")) {
            Map<String, Object> stripeConfig = (Map<String, Object>) config.get("stripe");
            return (String) stripeConfig.get("apiKey");
        }
        return null;
    }

    public static String getApiSecret() {
        if (config.containsKey("stripe.apiSecret")) {
            return (String) config.get("stripe.apiSecret");
        } else if (config.containsKey("stripe")) {
            Map<String, Object> stripeConfig = (Map<String, Object>) config.get("stripe");
            return (String) stripeConfig.get("apiSecret");
        }
        return null;
    }
}
