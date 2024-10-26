/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class DistributedCrawler {
    private static Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private static Map<String, List<String>> index = new ConcurrentHashMap<>();
    private static BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        String startUrl = "https://example.com";
        urlQueue.add(startUrl);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                while (!urlQueue.isEmpty()) {
                    try {
                        String url = urlQueue.poll(1, TimeUnit.SECONDS);
                        if (url != null && !visitedUrls.contains(url)) {
                            crawl(url);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        search("example query");
    }

    private static void crawl(String url) {
        if (visitedUrls.contains(url) || visitedUrls.size() > 100) {
            return;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();
            indexDocument(url, text);
            visitedUrls.add(url);

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String nextUrl = link.absUrl("href");
                if (!visitedUrls.contains(nextUrl)) {
                    urlQueue.add(nextUrl);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to retrieve content from " + url + ": " + e.getMessage());
        }
    }

    private static void indexDocument(String url, String text) {
        String[] words = text.split("\\W+");
        for (String word : words) {
            word = word.toLowerCase();
            index.computeIfAbsent(word, k -> new ArrayList<>()).add(url);
        }
    }

    private static void search(String query) {
        String[] words = query.toLowerCase().split("\\W+");
        Map<String, Integer> results = new HashMap<>();

        for (String word : words) {
            List<String> urls = index.get(word);
            if (urls != null) {
                for (String url : urls) {
                    results.put(url, results.getOrDefault(url, 0) + 1);
                }
            }
        }

        results.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> System.out.println("Found in: " + entry.getKey() + " (Score: " + entry.getValue() + ")"));
    }
}
