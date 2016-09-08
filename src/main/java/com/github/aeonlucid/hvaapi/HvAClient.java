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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HvAClient {

    private final static Logger logger = LogManager.getLogger(HvAClient.class);

    // We are actually an android phone.
    private static final String USER_AGENT = "Dalvik/2.1.0 (Linux; U; Android 5.0; Nexus 5 Build/LPX13D)";
    private static final String API_URL = "https://mnet.hva.nl/app/3.0/services";

    private final String username;
    private final String password;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final SimpleDateFormat timeTableDateFormat;

    private boolean authenticated;

    public HvAClient(String username, String password) {
        this(username, password, null);
    }

    public HvAClient(String username, String password, Proxy proxy) {
        this.username = username;
        this.password = password;
        this.authenticated = false;
        this.timeTableDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
     * Gets the current {@link AuthenticationUser} of the Hogeschool van Amsterdam.
     *
     * @return Returns the current {@link AuthenticationUser} of the Hogeschool van Amsterdam.
     */
    public AuthenticationUser getCurrentUser() {
        if (!authenticated) return null;

        return performRequest(AuthenticationUser.class, "/auth/getCurrentUser");
    }

    /**
     * Updates the profile on the Hogeschool van Amsterdam.
     *
     * @param profile The modified {@link Profile} you want to synchronize.
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
     * Gets an array containing all {@link Domain}s of the Hogeschool van Amsterdam.
     *
     * @return Returns an array containing all {@link Domain}s of the Hogeschool van Amsterdam.
     */
    public Domain[] getDomains() {
        if (!authenticated) return null;

        return performRequest(Domain[].class, "/api/domains");
    }

    /**
     * Gets an array containing all {@link Programme}s of the Hogeschool van Amsterdam.
     *
     * @return Returns an array containing all {@link Programme}s of the Hogeschool van Amsterdam.
     */
    public Programme[] getProgrammes() {
        if (!authenticated) return null;

        return performRequest(Programme[].class, "/api/programmes");
    }

    /**
     * Gets the {@link Absentee}s of the Hogeschool van Amsterdam.
     *
     * @return Returns the {@link Absentee}s of the Hogeschool van Amsterdam.
     */
    public Absentee[] getAbsentees() {
        if (!authenticated) return new Absentee[0];

        return performRequest(Absentee[].class, "/api/absentees");
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
     * Gets an array containing all {@link Location}s of the Hogeschool van Amsterdam.
     *
     * @return Returns an array containing all {@link Location}s of the Hogeschool van Amsterdam.
     */
    public Location[] getLocations() {
        if (!authenticated) return null;

        return performRequest(Location[].class, "/api/locations");
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

    /**
     * Gets the {@link StudyLocationPage} of the specified url of the Hogeschool van Amsterdam.
     *
     * @param url The url property of {@link StudyLocation}.
     * @return Returns the {@link StudyLocationPage} of the specified url of the Hogeschool van Amsterdam.
     */
    public StudyLocationPage getStudyLocationsPage(String url) {
        if (!authenticated) return null;

        return performRequest(StudyLocationPage.class, String.format("/api/studylocations/page?url=%s&main=true&serviceUrl=%s/", url, API_URL));
    }

    /**
     * Gets the {@link TimetableItem}s of the specified week number of the currently signed in user of the Hogeschool van Amsterdam.
     *
     * @param weekNumber The week number to retrieve {@link TimetableItem}s.
     * @return Returns the {@link TimetableItem}s of the specified week number of the currently signed in user of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getMyTimeTable(int weekNumber) {
        return getMyTimeTable(new DateTime().withWeekOfWeekyear(weekNumber).withDayOfWeek(1).toLocalDate());
    }

    /**
     * Gets the {@link TimetableItem}s of the specified start {@link LocalDate} plus 7 days of the currently signed in user of the Hogeschool van Amsterdam.
     *
     * @param startDate The start {@link LocalDate} to retrieve {@link TimetableItem}s from, 7 days are added for the endDate.
     * @return Returns the {@link TimetableItem}s of the specified start {@link LocalDate} of the currently signed in user of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getMyTimeTable(LocalDate startDate) {
        return getMyTimeTable(startDate.toDate(), startDate.plusDays(7).toDate());
    }

    /**
     * Gets the {@link TimetableItem}s of the specified start {@link LocalDate} and end {@link LocalDate} of the currently signed in user of the Hogeschool van Amsterdam.
     *
     * @param startDate The start {@link LocalDate} to retrieve {@link TimetableItem}s from.
     * @param endDate   The end {@link LocalDate} to retrieve {@link TimetableItem}s from.
     * @return Returns the {@link TimetableItem}s of the specified start {@link LocalDate} and end {@link LocalDate} of the currently signed in user of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getMyTimeTable(LocalDate startDate, LocalDate endDate) {
        return getMyTimeTable(startDate.toDate(), endDate.toDate());
    }

    /**
     * Gets the {@link TimetableItem}s of the specified start {@link Date} and end {@link Date} of the currently signed in user of the Hogeschool van Amsterdam.
     *
     * @param startDate The start {@link Date} to retrieve {@link TimetableItem} from.
     * @param endDate   The end {@link Date} to retrieve {@link TimetableItem} from.
     * @return Returns the {@link TimetableItem}s of the specified start {@link Date} and end {@link Date} of the currently signed in user of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getMyTimeTable(Date startDate, Date endDate) {
        if (!authenticated) return null;

        return performRequest(TimetableItem[].class, String.format("/timetable/my?startDate=%s&endDate=%s", timeTableDateFormat.format(startDate), timeTableDateFormat.format(endDate)));
    }

    /**
     * Gets the {@link TimetableItem}s of the specified week number of the specified studentSetId of the Hogeschool van Amsterdam.
     *
     * @param studentSetId StudentSet ID which is retrieve-able from {@link Schedule}.
     * @param weekNumber The week number to retrieve {@link TimetableItem}s.
     * @return Returns the {@link TimetableItem}s of the specified week number of the specified studentSetId of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getOtherTimeTable(String studentSetId, int weekNumber) {
        return getOtherTimeTable(studentSetId, new DateTime().withWeekOfWeekyear(weekNumber).withDayOfWeek(1).toLocalDate());
    }

    /**
     * Gets the {@link TimetableItem}s of the specified start {@link LocalDate} plus 7 days of the specified studentSetId of the Hogeschool van Amsterdam.
     *
     * @param studentSetId StudentSet ID which is retrieve-able from {@link Schedule}.
     * @param startDate The start {@link LocalDate} to retrieve {@link TimetableItem}s from, 7 days are added for the endDate.
     * @return Returns the {@link TimetableItem}s of the specified start {@link LocalDate} plus 7 days of the specified studentSetId of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getOtherTimeTable(String studentSetId, LocalDate startDate) {
        return getOtherTimeTable(studentSetId, startDate.toDate(), startDate.plusDays(7).toDate());
    }

    /**
     * Gets the {@link TimetableItem}s of the specified start {@link LocalDate} and end {@link LocalDate} of the specified studentSetId of the Hogeschool van Amsterdam.
     *
     * @param studentSetId StudentSet ID which is retrieve-able from {@link Schedule}.
     * @param startDate The start {@link LocalDate} to retrieve {@link TimetableItem}s from.
     * @param endDate   The end {@link LocalDate} to retrieve {@link TimetableItem}s from.
     * @return Returns the {@link TimetableItem}s of the specified start {@link LocalDate} and end {@link LocalDate} of the specified studentSetId of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getOtherTimeTable(String studentSetId, LocalDate startDate, LocalDate endDate) {
        return getOtherTimeTable(studentSetId, startDate.toDate(), endDate.toDate());
    }

    /**
     * Gets the {@link TimetableItem}s of the specified start {@link Date} and end {@link Date} of the specified studentSetId of the Hogeschool van Amsterdam.
     *
     * @param studentSetId StudentSet ID which is retrieve-able from {@link Schedule}.
     * @param startDate The start {@link Date} to retrieve {@link TimetableItem} from.
     * @param endDate   The end {@link Date} to retrieve {@link TimetableItem} from.
     * @return Returns the {@link TimetableItem}s of the specified start {@link Date} and end {@link Date} of the specified studentSetId of the Hogeschool van Amsterdam.
     */
    public TimetableItem[] getOtherTimeTable(String studentSetId, Date startDate, Date endDate) {
        if (!authenticated) return null;

        return performRequest(TimetableItem[].class, String.format("/timetable/other?id=%s&startDate=%s&endDate=%s", studentSetId, timeTableDateFormat.format(startDate), timeTableDateFormat.format(endDate)));
    }

    /**
     * Gets an array containing all {@link Schedule}s of the specified filter of the Hogeschool van Amsterdam.
     *
     * @param filter Filter to search for {@link Schedule}s.
     * @return Returns an array containing all {@link Schedule}s of the specified filter of the Hogeschool van Amsterdam.
     */
    public Schedule[] getSchedules(String filter) {
        if (!authenticated) return null;

        return performRequest(Schedule[].class, String.format("/timetable/schedules?filter=%s", filter));
    }

    /**
     * Gets an array containing all {@link AzUrl}s of the Hogeschool van Amsterdam.
     *
     * @return Returns an array containing all {@link AzUrl}s of the Hogeschool van Amsterdam.
     */
    public AzUrl[] getAzUrlsForEmployees() {
        if (!authenticated) return null;

        return performRequest(AzUrl[].class, "/api/az");
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
