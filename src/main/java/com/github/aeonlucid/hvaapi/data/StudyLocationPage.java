package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudyLocationPage {

    @JsonProperty("HtmlContent")
    private String contentHtml;

    @JsonProperty("PreviousPage")
    private String previousPage;

    public String getContentHtml() {
        return contentHtml;
    }

    public String getPreviousPage() {
        return previousPage;
    }

}
