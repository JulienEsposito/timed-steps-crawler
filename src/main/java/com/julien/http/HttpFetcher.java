package com.julien.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public final class HttpFetcher {

    private static final String USER_AGENT = "WordHopsBot/0.1 (+https://example.com)";
    private static final int TIMEOUT_MILLIS = 10_000; 

    private HttpFetcher() {}

    public static Document fetch(String url) throws Exception {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MILLIS)
                .followRedirects(true)
                .get();
    }
}
