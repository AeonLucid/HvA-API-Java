package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class News {

    @JsonProperty("Id")
    private int id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Text")
    private String text;

    @JsonProperty("CreatedOn") // Date
    private String createdOn;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("DlwoUrl")
    private String dlwoUrl;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getDlwoUrl() {
        return dlwoUrl;
    }

}
