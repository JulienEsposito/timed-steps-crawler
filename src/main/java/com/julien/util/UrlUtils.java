package com.julien.util;

import java.net.URI;


public final class UrlUtils {
    private UrlUtils() {}

    public static String hostOf(String url) {
        try { return URI.create(url).getHost(); }
        catch (Exception e) { return null; }
    }

    public static String normalize(String url) {
        try {
            URI u = URI.create(url);
            u = new URI(u.getScheme(), u.getUserInfo(), u.getHost(), u.getPort(), u.getPath(), u.getQuery(), null);
            return u.toString();
        } catch (Exception e) {
            return url;
        }
    }

    public static boolean shouldFollow(String url) {
        if (url == null || url.isEmpty()) return false;
        String lower = url.toLowerCase();
        if (lower.startsWith("mailto:") || lower.startsWith("javascript:")) return false;
        if (!(lower.startsWith("http://") || lower.startsWith("https://"))) return false;
        return true;
    }
}
