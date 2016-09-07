package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {

    @JsonProperty("Domain")
    private String domain;

    @JsonProperty("DomainAZUrl")
    private String domainAzUrl;

    @JsonProperty("Language")
    private String language;

//  TODO: Add unknown properties.
//    @JsonProperty("Courses")
//
//    @JsonProperty("Teachers")

    @JsonProperty("AZProgrammeTitle")
    private String azProgrammeTitle;

    @JsonProperty("AZType")
    private String azType;

    @JsonProperty("AZPrefix")
    private String azPrefix;

    @JsonProperty("IsComplete")
    private boolean complete;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainAzUrl() {
        return domainAzUrl;
    }

    public void setDomainAzUrl(String domainAzUrl) {
        this.domainAzUrl = domainAzUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAzProgrammeTitle() {
        return azProgrammeTitle;
    }

    public void setAzProgrammeTitle(String azProgrammeTitle) {
        this.azProgrammeTitle = azProgrammeTitle;
    }

    public String getAzType() {
        return azType;
    }

    public void setAzType(String azType) {
        this.azType = azType;
    }

    public String getAzPrefix() {
        return azPrefix;
    }

    public void setAzPrefix(String azPrefix) {
        this.azPrefix = azPrefix;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
