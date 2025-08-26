package com.example.inventory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "store")
public class StoreProperties {
    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLocal() {
        return "local".equalsIgnoreCase(role);
    }

    public boolean isCentral() {
        return "central".equalsIgnoreCase(role);
    }
}
