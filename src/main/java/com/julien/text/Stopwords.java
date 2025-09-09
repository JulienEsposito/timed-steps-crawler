package com.julien.text;

import java.util.HashSet;
import java.util.Set;

public final class Stopwords {
    private Stopwords() {}

    public static final Set<String> EN = new HashSet<>();

    // TODO: Enrich this list (per-language sets, domain-specific).


    static {
        String[] words = {
            "a","an","the","and","or","but","if","then","else","for","to","of","in","on","at","by",
            "with","from","as","is","are","was","were","be","been","being","it","its","this","that",
            "these","those","i","you","he","she","we","they","them","his","her","our","their","my",
            "me","your","yours","ours","theirs","not","no","yes","do","does","did","done","can","could",
            "should","would","will","just","than","so","such","into","about","over","under","between",
            "also","more","most","many","much","some","any","each","few","other","another","same",
            "very","one","two","three","which","have","has","had"
        };
        for (String w : words) EN.add(w);

        String[] fragments = {"s","t","ll","re","ve","m","d"};
        for (String w : fragments) EN.add(w);

        String[] months = {
            "january","february","march","april","may","june","july","august","september","october","november","december"
        };
        for (String w : months) EN.add(w);

        String[] days = {"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
        for (String w : days) EN.add(w);

        String[] wiki = {
            "retrieved","archived","original","citation","references","reference","edit","page","article",
            "privacy","policy","navigation","category","help","talk","login","tools"
        };
        for (String w : wiki) EN.add(w);
    }
}
