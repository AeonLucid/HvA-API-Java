package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("IsStudent")
    private boolean student;

    @JsonProperty("IsEmployee")
    private boolean employee;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public boolean isStudent() {
        return student;
    }

    public boolean isEmployee() {
        return employee;
    }

}
