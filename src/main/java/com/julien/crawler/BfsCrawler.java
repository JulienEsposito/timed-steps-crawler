package com.julien.crawler;

import com.julien.http.HttpFetcher;
import com.julien.html.HtmlTextExtractor;
import com.julien.text.PageKeyword;
import com.julien.text.Tokenizer;
import com.julien.util.Deadline;
import com.julien.util.UrlUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.Duration;
import java.util.*;


public final class BfsCrawler {

    private record Page(String url, int depth) {}

    private BfsCrawler() {}

    public static CrawlResult run(String startUrl,
                                  int maxDepth,
                                  int timeMinutes,
                                  int numKeywords) {

        Deadline deadline = new Deadline(Duration.ofMinutes(timeMinutes));

        Queue<Page> q = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        CrawlResult result = new CrawlResult();

        q.add(new Page(startUrl, 0));

        while (!q.isEmpty()) {
            if (deadline.isExpired()) {
                System.err.println("Time limit reached; stopping crawl.");
                break;
            }

            Page cur = q.poll();
            if (cur.depth > maxDepth) continue;

            String normUrl = UrlUtils.normalize(cur.url);
            if (!visited.add(normUrl)) continue;

            try {
                Document doc = HttpFetcher.fetch(cur.url);

                String title = HtmlTextExtractor.title(doc);
                String heads = HtmlTextExtractor.headings(doc);
                String body  = HtmlTextExtractor.bodyText(doc);

                var tTitle = Tokenizer.tokens(title);
                var tHeads = Tokenizer.tokens(heads);
                var tBody  = Tokenizer.tokens(body);

                java.util.List<String> keywords = PageKeyword.chooseTopK(tTitle, tHeads, tBody, Math.max(1, numKeywords));

                System.out.printf("[step %d] hostname: %s, keywords: %s%n",
                        cur.depth, cur.url, String.join(", ", keywords));

                String host = UrlUtils.hostOf(cur.url);
                if (host == null) host = "(unknown-host)";
                result.add(new PageResult(cur.depth, cur.url, host, keywords));

                if (cur.depth < maxDepth) {
                    for (Element a : doc.select("a[href]")) {
                        String abs = a.attr("abs:href");
                        if (!UrlUtils.shouldFollow(abs)) continue; 
                        q.add(new Page(abs, cur.depth + 1));
                    }
                }

            } catch (Exception e) {

            }
        }

        return result;
    }
}
