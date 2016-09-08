package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Domain {

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Naam")
    private String name;

    @JsonProperty("Active")
    private boolean active;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

}
