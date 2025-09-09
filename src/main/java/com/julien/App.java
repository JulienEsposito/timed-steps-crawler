package com.julien;

import com.julien.crawler.BfsCrawler;
import com.julien.crawler.CrawlResult;

import java.util.*;

public class App {

    private static final String DEFAULT_URL =
            "https://en.wikipedia.org/wiki/Open-source_intelligence";
    private static final int DEFAULT_DEPTH = 2;
    private static final int DEFAULT_MINUTES = 5;
    private static final int DEFAULT_KEYWORDS = 3;

    private record Config(String url, int depth, int minutes, int keywords, boolean helpRequested) {}

    public static void main(String[] args) {
        Config cfg = parseArgs(args);

        if (cfg.helpRequested) {
            printHelp();
            return;
        }

        System.out.println("=== Parameters ===");
        System.out.printf("Start URL    : %s%n", cfg.url);
        System.out.printf("Max step    : %d%n", cfg.depth);
        System.out.printf("Time limit   : %d minutes%n", cfg.minutes);
        System.out.printf("Num keywords : %d%n", cfg.keywords);
        System.out.println();

        try {
            long t0 = System.nanoTime();

            CrawlResult result = BfsCrawler.run(cfg.url, cfg.depth, cfg.minutes, cfg.keywords);

            long elapsedNanos = System.nanoTime() - t0;
            double elapsedSeconds = elapsedNanos / 1_000_000_000.0;

            System.out.println("\n=== Top keywords per step ===");
            var depths = new ArrayList<>(result.pagesByDepth().keySet());
            Collections.sort(depths);
            for (var depth : depths) {
                var counts = result.keywordCountsByDepth(depth);
                System.out.printf("Step %d:%n", depth);
                counts.entrySet().stream()
                        .sorted(Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry::getKey))
                        .forEach(e -> System.out.printf("  %-20s %d%n", e.getKey(), e.getValue()));
            }

            System.out.printf("%nCrawled %d pages across %d hosts in %.1f seconds.%n",
                    result.totalPages(), result.uniqueHosts(), elapsedSeconds);

        } catch (Exception e) {
            System.err.println("Error during crawl: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printHelp() {
        System.out.println("""
            Usage:
              java -jar target/word-hops-0.1.0.jar [FLAGS]
              java -jar target/word-hops-0.1.0.jar [url] [depth] [minutes] [keywords]
            """);
    }

    private static Config parseArgs(String[] args) {
        String url = DEFAULT_URL;
        int step  = DEFAULT_DEPTH;
        int minutes = DEFAULT_MINUTES;
        int keywords = DEFAULT_KEYWORDS;
        boolean help = false;

        if (args == null || args.length == 0) {
            return new Config(url, step, minutes, keywords, false);
        }

        boolean hasFlag = Arrays.stream(args).anyMatch(a -> a.startsWith("-"));
        if (!hasFlag) {
            url = argOrDefault(args, 0, url);
            step = safeInt(argOrDefault(args, 1, Integer.toString(step)), step);
            minutes = safeInt(argOrDefault(args, 2, Integer.toString(minutes)), minutes);
            keywords = safeInt(argOrDefault(args, 3, Integer.toString(keywords)), keywords);
            return new Config(url, step, minutes, keywords, false);
        }

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (!a.startsWith("-")) continue;

            String key = a;
            String val = null;

            int eq = a.indexOf('=');
            if (eq >= 0) {
                key = a.substring(0, eq);
                val = a.substring(eq + 1);
            }

            switch (key) {
                case "--help", "-h" -> help = true;

                case "--url", "--start", "-u" -> {
                    if (val == null) { val = nextValue(args, ++i, "--url requires a value"); }
                    url = val;
                }

                case "--step", "--max-depth", "-d" -> {
                    if (val == null) { val = nextValue(args, ++i, "--depth requires a value"); }
                    step = safeInt(val, step);
                }

                case "--minutes", "--time", "--time-minutes", "-t" -> {
                    if (val == null) { val = nextValue(args, ++i, "--minutes requires a value"); }
                    minutes = safeInt(val, minutes);
                }

                case "--keywords", "--topk", "-k" -> {
                    if (val == null) { val = nextValue(args, ++i, "--keywords requires a value"); }
                    keywords = Math.max(1, safeInt(val, keywords));
                }

                default -> {
                }
            }
        }

        return new Config(url, step, minutes, keywords, help);
    }

    private static String argOrDefault(String[] args, int idx, String def) {
        return idx < args.length ? args[idx] : def;
    }

    private static String nextValue(String[] args, int idx, String errMsg) {
        if (idx >= args.length) throw new IllegalArgumentException(errMsg);
        String v = args[idx];
        if (v.startsWith("-")) throw new IllegalArgumentException(errMsg);
        return v;
    }

    private static int safeInt(String s, int fallback) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return fallback; }
    }
}
