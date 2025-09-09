package com.julien.crawler;

import java.util.*;

public final class CrawlResult {
    private final Map<Integer, List<PageResult>> pagesByDepth = new HashMap<>();

    public void add(PageResult pr) {
        pagesByDepth.computeIfAbsent(pr.depth(), d -> new ArrayList<>()).add(pr);
    }

    public Map<Integer, List<PageResult>> pagesByDepth() {
        return pagesByDepth;
    }

    public Map<String, Integer> keywordCountsByDepth(int depth) {
        Map<String, Integer> counts = new HashMap<>();
        var list = pagesByDepth.getOrDefault(depth, List.of());
        for (var pr : list) {
            for (String kw : pr.keywords()) {
                if (kw == null || kw.isBlank() || "(none)".equals(kw)) continue;
                counts.merge(kw, 1, Integer::sum);
            }
        }
        return counts;
    }

    public int totalPages() {
        int total = 0;
        for (var list : pagesByDepth.values()) total += list.size();
        return total;
    }

    public int uniqueHosts() {
        Set<String> hosts = new HashSet<>();
        for (var list : pagesByDepth.values()) {
            for (var pr : list) hosts.add(pr.host());
        }
        return hosts.size();
    }
}
