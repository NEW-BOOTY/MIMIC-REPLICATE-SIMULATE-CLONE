/** Copyright © 2024 Devin B. Royal. All Rights reserved. */
package com.devinroyal;

import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BrowserUtils {

    private static Deque<String> history = new ArrayDeque<>();
    private static Set<String> visitedLinks = new HashSet<>();

    public static void displayPageContent(Document doc, Terminal terminal, String filterArgument) {
        String title = doc.title();
        terminal.writer().println(Ansi.ansi().bold().fg(Ansi.Color.BLUE).a(title).reset());

        Elements bodyElements = doc.body().children();
        Elements links = doc.select("a[href]");

        // Filter content (if argument provided) and display
        Pattern filterPattern = filterArgument.isEmpty() ? null : Pattern.compile(filterArgument, Pattern.CASE_INSENSITIVE);
        for (Element element : bodyElements) {
            displayElement(element, terminal, filterPattern);
        }

        // Display links with numbers for navigation
        if (!links.isEmpty()) {
            terminal.writer().println("\nLinks:");
            for (int i = 0; i < links.size(); i++) {
                String linkText = links.get(i).text();
                String linkUrl = links.get(i).absUrl("href");
                terminal.writer().println(Ansi.ansi().fg(Ansi.Color.GREEN).a((i + 1) + ". " + linkText).reset() + " (" + linkUrl + ")");
            }
        }
    }

    private static void displayElement(Element element, Terminal terminal, Pattern filterPattern) {
        if (filterPattern != null && !filterPattern.matcher(element.text()).find()) {
            return; // Skip if doesn't match filter
        }

        String tagName = element.tagName().toLowerCase();
        switch (tagName) {
            case "h1", "h2", "h3", "h4", "h5", "h6":
                terminal.writer().println(Ansi.ansi().bold().a(element.text()).reset());
                break;
            case "p":
                terminal.writer().println(element.text() + "\n");
                break;
            case "ul", "ol":
                for (Element listItem : element.children()) {
                    terminal.writer().println("  * " + listItem.text());
                }
                terminal.writer().println();
                break;
            case "a":
                // Store link in history and mark as visited
                String linkUrl = element.absUrl("href");
                history.push(linkUrl);
                visitedLinks.add(linkUrl);
                break; 
            // ... (add cases for other elements as needed)
            default:
                // Default handling for other elements
                terminal.writer().println(element.text());
        }
    }

    // Additional methods for navigation, history, etc. (you can add these)
    
    public static boolean hasBackHistory() {
        return history.size() > 1;
    }
}
/** Copyright © 2024 Devin B. Royal. All Rights reserved. */

