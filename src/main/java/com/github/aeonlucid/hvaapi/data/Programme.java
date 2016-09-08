package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Programme {

    @JsonProperty("Naam")
    private String name;

    @JsonProperty("AZUrl")
    private String azUrl;

    public String getName() {
        return name;
    }

    public String getAzUrl() {
        return azUrl;
    }

}
