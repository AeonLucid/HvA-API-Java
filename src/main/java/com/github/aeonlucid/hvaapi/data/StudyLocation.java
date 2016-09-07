package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudyLocation {

    @JsonProperty("Url")
    private String url;

    @JsonProperty("Name")
    private String name;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

}
