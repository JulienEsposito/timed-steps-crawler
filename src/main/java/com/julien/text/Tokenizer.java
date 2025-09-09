package com.julien.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public final class Tokenizer {
    private Tokenizer() {}

    private static final Pattern WORD = Pattern.compile("\\p{L}+"); 

    public static List<String> tokens(String text) {
        var out = new ArrayList<String>();
        if (text == null || text.isEmpty()) return out;

        var m = WORD.matcher(text.toLowerCase(Locale.ROOT));
        while (m.find()) {
            String w = m.group();
            if (Stopwords.EN.contains(w)) continue;
            out.add(w);
        }
        return out;
    }
}
