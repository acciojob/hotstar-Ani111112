package com.driver.EntryDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductionHouseEntryDto {

    private String name;

    @JsonCreator
    public ProductionHouseEntryDto(@JsonProperty("name") String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
