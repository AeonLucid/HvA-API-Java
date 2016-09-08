package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Absentee {

    @JsonProperty("DisplayName")
    private String displayName;

    @JsonProperty("AbsentVanaf")
    private String from;

    @JsonProperty("AbsentTot")
    private String to;

    @JsonProperty("AbsentComment")
    private String comment;

    public String getDisplayName() {
        return displayName;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getComment() {
        return comment;
    }

}
