package com.example.inventory.infrastructure.messaging.dto;

import com.example.inventory.domain.event.EventCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class StoreEventMessage extends DomainEventMessage {

    private String name;
    private String address;

    public StoreEventMessage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}