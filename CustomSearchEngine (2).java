/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomSearchEngine {
    private static Set<String> visitedUrls = new HashSet<>();
    private static Map<String, String> index = new HashMap<>();

    public static void main(String[] args) {
        String startUrl = "https://example.com";
        crawl(startUrl);
        search("example query");
    }

    private static void crawl(String url) {
        if (visitedUrls.contains(url) || visitedUrls.size() > 100) {
            return;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();
            index.put(url, text);
            visitedUrls.add(url);

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String nextUrl = link.absUrl("href");
                crawl(nextUrl);
            }
        } catch (IOException e) {
            System.err.println("Failed to retrieve content from " + url + ": " + e.getMessage());
        }
    }

    private static void search(String query) {
        System.out.println("Search results for: " + query);
        for (Map.Entry<String, String> entry : index.entrySet()) {
            if (entry.getValue().contains(query)) {
                System.out.println("Found in: " + entry.getKey());
            }
        }
    }
}
