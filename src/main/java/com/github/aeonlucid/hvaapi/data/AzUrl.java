package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AzUrl {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Url")
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}
