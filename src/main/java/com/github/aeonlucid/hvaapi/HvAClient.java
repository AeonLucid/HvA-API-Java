package com.github.aeonlucid.hvaapi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aeonlucid.hvaapi.data.*;
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

    private boolean authenticated;

    public HvAClient(String username, String password) {
        this(username, password, null);
    }

    public HvAClient(String username, String password, Proxy proxy) {
        this.username = username;
        this.password = password;
        this.authenticated = false;

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
     *
     * @return Determines whether authentication was successful.
     */
    public boolean signIn() {
        final RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        final AuthenticationUser authentication = performRequest(AuthenticationUser.class, "/auth/signin", requestBody);

        return authenticated = !(authentication == null || !authentication.isAuthenticated());

    }

    /**
     * Sends a sign out request to the Hogeschool van Amsterdam.
     *
     * @return Determines whether signing out was successful.
     */
    public boolean signOut() {
        return !authenticated || (authenticated = performRequest("/auth/signout"));
    }

    /**
     * Synchronizes the {@link Profile} property to the Hogeschool van Amsterdam.
     *
     * @return Determines whether updating was successful.
     */
    public boolean updateProfile(Profile profile) {
        if (!authenticated) return false;

        final RequestBody requestBody = new FormBody.Builder()
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

        return performRequest(Profile.class, "/auth/updateprofile", requestBody) != null;

    }

    /**
     * Gets the {@link Domain}s of the Hogeschool van Amsterdam.
     *
     * @return Returns the {@link Domain}s of the Hogeschool van Amsterdam.
     */
    public Domain[] getDomains() {
        if (!authenticated) return null;

        return performRequest(Domain[].class, "/api/domains");
    }

    /**
     * Gets the {@link Programme}s of the Hogeschool van Amsterdam.
     *
     * @return Returns the {@link Programme}s of the Hogeschool van Amsterdam.
     */
    public Programme[] getProgrammes() {
        if (!authenticated) return null;

        return performRequest(Programme[].class, "/api/programmes");
    }

    /**
     * Gets the current {@link AuthenticationUser} of the Hogeschool van Amsterdam.
     *
     * @return Returns the current {@link AuthenticationUser} of the Hogeschool van Amsterdam.
     */
    public AuthenticationUser getCurrentUser() {
        if (!authenticated) return null;

        return performRequest(AuthenticationUser.class, "/auth/getCurrentUser");
    }

    /**
     * Gets an array containing all {@link News} of the Hogeschool van Amsterdam.
     *
     * @return Returns an array containing all {@link News} of the Hogeschool van Amsterdam.
     */
    public News[] getNews() {
        if (!authenticated) return null;

        return performRequest(News[].class, "/api/news");
    }

    /**
     * Gets an array containing all {@link StudyLocation}s of the Hogeschool van Amsterdam.
     *
     * @return Returns an array containing all {@link StudyLocation}s of the Hogeschool van Amsterdam.
     */
    public StudyLocation[] getStudyLocations() {
        if (!authenticated) return null;

        return performRequest(StudyLocation[].class, "/api/studylocations");
    }

    // GET
    private boolean performRequest(String urlPath) {
        Request.Builder requestBuilder = getRequestBuilder(urlPath).get();

        try {
            final Response response = httpClient.newCall(requestBuilder.build()).execute();

            if (logger.isDebugEnabled())
                logger.debug(response.body().string());

            return response.isSuccessful();
        } catch (IOException e) {
            logger.error("HvAClient performRequest threw an exception.", e);
        }

        return false;
    }

    // GET
    private <T> T performRequest(Class<T> responseClass, String urlPath) {
        return performRequest(responseClass, urlPath, null);
    }

    // POST
    private <T> T performRequest(Class<T> responseClass, String urlPath, RequestBody requestBody) {
        Request.Builder requestBuilder = getRequestBuilder(urlPath);

        if (requestBody != null) {
            requestBuilder = requestBuilder.post(requestBody);
        } else {
            requestBuilder = requestBuilder.get();
        }

        try {
            final Response response = httpClient.newCall(requestBuilder.build()).execute();
            final String responseStr = response.body().string();

            if (logger.isDebugEnabled())
                logger.debug(responseStr);

            return objectMapper.readValue(responseStr, responseClass);
        } catch (IOException e) {
            logger.error("HvAClient performRequest threw an exception.", e);
        }

        return null;
    }

    private Request.Builder getRequestBuilder(String urlPath) {
        return new Request.Builder()
                .url(API_URL + urlPath)
                .header("Accept", "application/json")
                .header("Accept-Language", "en-US")
                .header("User-Agent", USER_AGENT)
                .header("X-Requested-With", "nl.hva.hvapp");
    }

}
