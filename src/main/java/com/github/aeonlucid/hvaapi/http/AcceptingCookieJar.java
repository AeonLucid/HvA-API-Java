package com.github.aeonlucid.hvaapi.http;

import com.github.aeonlucid.hvaapi.HvAClient;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AcceptingCookieJar implements CookieJar {

    private final static Logger logger = LogManager.getLogger(HvAClient.class);

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if(logger.isDebugEnabled()) {
            logger.debug(String.format("New cookies for %s:", url.host()));
            for (Cookie cookie : cookies) {
                logger.debug(String.format(" - %s=%s", cookie.name(), cookie.value()));
            }
        }

        cookieStore.put(url.host(), cookies);
    }

    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.containsKey(url.host()) ? cookieStore.get(url.host()) : new ArrayList<Cookie>();
    }

}
