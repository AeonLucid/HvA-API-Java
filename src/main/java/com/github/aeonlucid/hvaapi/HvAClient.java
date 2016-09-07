package com.github.aeonlucid.hvaapi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aeonlucid.hvaapi.data.Authentication;
import com.github.aeonlucid.hvaapi.data.Profile;
import com.github.aeonlucid.hvaapi.data.StudyLocation;
import com.github.aeonlucid.hvaapi.data.User;
import com.github.aeonlucid.hvaapi.http.AcceptingCookieJar;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Proxy;

public class HvAClient {

    private final static Logger logger = LogManager.getLogger(HvAClient.class);

    // We are actually an android phone.
    private static final String USER_AGENT = "Dalvik/2.1.0 (Linux; U; Android 5.0; Nexus 5 Build/LPX13D)";
    private static final String API_URL = "https://mnet.hva.nl/app/3.0/services";

    private final String username;
    private final String password;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private User user;
    private Profile profile;

    public HvAClient(String username, String password) {
        this(username, password, null);
    }

    public HvAClient(String username, String password, Proxy proxy) {
        this.username = username;
        this.password = password;

        httpClient = new OkHttpClient.Builder()
                .cookieJar(new AcceptingCookieJar())
                .proxy(proxy)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC);
    }

    /**
     * Sends an authentication request to the Hogeschool van Amsterdam.
     * @return Indicates whether authentication was successful.
     */
    public boolean authenticate() {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Authentication authentication = performRequest(Authentication.class, "/auth/signin", requestBody);

        if (authentication == null || !authentication.isAuthenticated())
            return false;

        user = authentication.getUser();
        profile = authentication.getProfile();

        return true;
    }

    /**
     * Synchronizes the {@link Profile} property to the Hogeschool van Amsterdam.
     * @return Indicates whether updating was successful.
     */
    public boolean updateProfile() {
        if(user == null) return false;

        RequestBody requestBody = new FormBody.Builder()
                .add("Domain", profile.getDomain())
                .add("DomainAZUrl", profile.getDomainAzUrl())
                .add("Language", profile.getLanguage())
                // TODO: Courses
                // TODO: Teachers
                .add("AZProgrammeTitle", profile.getAzProgrammeTitle())
                .add("AZType", profile.getAzType())
                .add("AZPrefix", profile.getAzPrefix())
                .add("IsComplete", String.valueOf(profile.isComplete()))
                .build();

        Profile newProfile = performRequest(Profile.class, "/auth/updateprofile", requestBody);

        if(newProfile == null)
            return false;

        profile = newProfile;

        return true;
    }

    /**
     * Gets an array containing all {@link StudyLocation}s of the Hogeschool van Amsterdam.
     * @return Returns an array containing all {@link StudyLocation}s of the Hogeschool van Amsterdam.
     */
    public StudyLocation[] getStudyLocations(){
        if(user == null) return null;

        return performRequest(StudyLocation[].class, "/api/studylocations");
    }

    /**
     * Gets the {@link Profile} of the currently authenticated user.
     * @return Returns the {@link Profile} of the currently authenticated user.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Gets the {@link User} of the currently authenticated user.
     * @return Returns the {@link User} of the currently authenticated user.
     */
    public User getUser() {
        return user;
    }

    // GET
    private <T> T performRequest(Class<T> responseClass, String urlPath) {
        return performRequest(responseClass, urlPath, null);
    }

    // POST
    private <T> T performRequest(Class<T> responseClass, String urlPath, RequestBody requestBody) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(API_URL + urlPath)
                .header("Accept", "application/json")
                .header("Accept-Language", "en-US")
                .header("User-Agent", USER_AGENT)
                .header("X-Requested-With", "nl.hva.hvapp");

        if(requestBody != null){
            requestBuilder = requestBuilder.post(requestBody);
        } else {
            requestBuilder = requestBuilder.get();
        }

        try {
            Response response = httpClient.newCall(requestBuilder.build()).execute();

            return objectMapper.readValue(response.body().string(), responseClass);
        } catch (IOException e) {
            logger.error("HvAClient performRequest threw an exception.", e);
        }

        return null;
    }

}
