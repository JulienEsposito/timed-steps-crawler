package com.julien.text;

import java.util.*;


public final class PageKeyword {
    private PageKeyword() {}

    public static String choose(List<String> titleTokens,
                                List<String> headingTokens,
                                List<String> bodyTokens) {
        List<String> top1 = chooseTopK(titleTokens, headingTokens, bodyTokens, 1);
        return top1.isEmpty() ? "(none)" : top1.get(0);
    }

    public static List<String> chooseTopK(List<String> titleTokens,
                                          List<String> headingTokens,
                                          List<String> bodyTokens,
                                          int k) {

        Map<String, Integer> score = new HashMap<>();

        // TODO: Upgrade scoring with ML/NLP methods

        add(score, titleTokens, 5);
        add(score, headingTokens, 3);
        add(score, bodyTokens, 1);

        score.keySet().removeIf(t -> t.length() < 3);

        if (score.isEmpty() || k <= 0) return List.of("(none)");

        Set<String> titleSet = new HashSet<>();
        if (titleTokens != null) titleSet.addAll(titleTokens);

        List<Map.Entry<String,Integer>> entries = new ArrayList<>(score.entrySet());
        entries.sort(
            Comparator.<Map.Entry<String,Integer>>comparingInt(Map.Entry::getValue).reversed()
                .thenComparing(e -> titleSet.contains(e.getKey()), Comparator.reverseOrder())
                .thenComparing(Map.Entry::getKey)
        );

        List<String> out = new ArrayList<>(Math.min(k, entries.size()));
        for (int i = 0; i < entries.size() && out.size() < k; i++) {
            out.add(entries.get(i).getKey());
        }

        if (out.isEmpty()) return List.of("(none)");
        return out;
    }

    private static void add(Map<String, Integer> score, List<String> tokens, int weight) {
        if (tokens == null || tokens.isEmpty() || weight <= 0) return;
        for (String t : tokens) {
            if (Stopwords.EN.contains(t)) continue; 
            score.merge(t, weight, Integer::sum);
        }
    }
}
