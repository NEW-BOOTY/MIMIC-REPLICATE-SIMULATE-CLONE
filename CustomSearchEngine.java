/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class CustomSearchEngine {
    private static Set<String> visitedUrls = new HashSet<>();
    private static Map<String, List<String>> index = new HashMap<>();
    private static Map<String, Integer> urlRanks = new HashMap<>();

    public static void main(String[] args) {
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            String startUrl = reader.readLine("Enter start URL: ");
            crawl(startUrl);

            while (true) {
                String query = reader.readLine("Enter search query (or 'exit' to quit): ");
                if (query.equalsIgnoreCase("exit")) {
                    break;
                }
                search(query, terminal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                crawl(nextUrl);
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
            urlRanks.put(url, urlRanks.getOrDefault(url, 0) + 1);
        }
    }

    private static void search(String query, Terminal terminal) {
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

        terminal.writer().println("Search results for: " + query);
        results.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> terminal.writer().println("Found in: " + entry.getKey() + " (Score: " + entry.getValue() + ")"));
    }
}
