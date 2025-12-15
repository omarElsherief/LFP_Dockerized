package com.zanta.lfp.cloud.config;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;

@Component
public class ConfigServer {

    private Map<String, Map<String, String>> configurations = new HashMap<>();

    public Map<String, String> getConfiguration(String serviceName, String profile) {
        String key = serviceName + "-" + profile;
        System.out.println("Config Server: Retrieving configuration for " + key);
        return configurations.getOrDefault(key, new HashMap<>());
    }

    public void updateConfiguration(String serviceName, String profile, Map<String, String> config) {
        String key = serviceName + "-" + profile;
        configurations.put(key, config);
        System.out.println("Config Server: Updated configuration for " + key);
    }

    public void refreshAll() {
        System.out.println("Config Server: Refreshing all service configurations");
    }

    public String getProperty(String serviceName, String profile, String key) {
        Map<String, String> config = getConfiguration(serviceName, profile);
        return config.get(key);
    }
}
