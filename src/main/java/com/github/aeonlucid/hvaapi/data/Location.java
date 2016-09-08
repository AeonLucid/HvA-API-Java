package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    @JsonProperty("Code")
    private int code;

    @JsonProperty("Naam")
    private String name;

    @JsonProperty("Adres")
    private String address;

    @JsonProperty("Postcode")
    private String postcode;

    @JsonProperty("Plaats")
    private String plaats;

    @JsonProperty("Telefoon")
    private String phone;

    @JsonProperty("Openingstijden")
    private String openingHoursHtml;

    @JsonProperty("AanvullendeInfo")
    private String extraInfoHtml;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getPlaats() {
        return plaats;
    }

    public String getPhone() {
        return phone;
    }

    public String getOpeningHoursHtml() {
        return openingHoursHtml;
    }

    public String getExtraInfoHtml() {
        return extraInfoHtml;
    }

}
