package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authentication {

    @JsonProperty("IsAuthenticated")
    private boolean authenticated;

    @JsonProperty("User")
    private User user;

    @JsonProperty("Profile")
    private Profile profile;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public User getUser() {
        return user;
    }

    public Profile getProfile() {
        return profile;
    }

}
