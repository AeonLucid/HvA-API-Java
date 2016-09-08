package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule {

    @JsonProperty("Description")
    private String description;

    @JsonProperty("HostKey")
    private String hostKey;

    @JsonProperty("Value")
    private String value;

    @JsonProperty("TableType")
    private String tableType;

    @JsonProperty("Key")
    private String key;

    public String getDescription() {
        return description;
    }

    public String getHostKey() {
        return hostKey;
    }

    public String getValue() {
        return value;
    }

    public String getTableType() {
        return tableType;
    }

    public String getKey() {
        return key;
    }

}
