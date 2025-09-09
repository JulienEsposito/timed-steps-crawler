package com.julien.html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;


public final class HtmlTextExtractor {
    private HtmlTextExtractor() {}

    private static final Pattern BRACKETED_BITS = Pattern.compile("\\[[^\\]]+\\]");
    private static final Pattern MULTISPACE = Pattern.compile("\\s+");

    public static String title(Document doc) {
        if (doc == null) return "";
        return normalize(doc.title() == null ? "" : doc.title());
    }

    public static String headings(Document doc) {
        if (doc == null) return "";
        StringBuilder sb = new StringBuilder();
        for (String tag : new String[]{"h1", "h2", "h3"}) {
            Elements hs = doc.select(tag);
            for (Element h : hs) sb.append(' ').append(h.text());
        }
        return normalize(sb.toString());
    }

    public static String bodyText(Document doc) {
        if (doc == null) return "";
        removeAll(doc, "script, style, noscript, svg, nav, footer, aside, header");
        removeAll(doc, "sup.reference, span.mw-editsection, table.infobox, table.navbox, div#mw-navigation, div.mw-footer, ol.references, div.mw-references-wrap");
        Element body = doc.body();
        if (body == null) return "";
        return normalize(body.text());
    }

    private static void removeAll(Document doc, String cssQuery) {
        for (Element el : doc.select(cssQuery)) el.remove();
    }

    private static String normalize(String s) {
        if (s == null) return "";
        s = BRACKETED_BITS.matcher(s).replaceAll(" ");
        s = MULTISPACE.matcher(s).replaceAll(" ").trim();
        return s;
    }
}
