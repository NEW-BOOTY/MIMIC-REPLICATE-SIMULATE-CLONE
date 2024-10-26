/** Copyright © 2024 Devin B. Royal. All Rights reserved. */

package com.devinbroyal;

import org.jline.terminal.Terminal;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BrowserUtils {

    public static void displayPageContent(Document doc, Terminal terminal, String filterArgument) {

        String title = doc.title();
        terminal.writer().println("\nTitle: " + title + "\n");

        Elements bodyElements = doc.body().children();

        // Filter content if argument is provided
        if (!filterArgument.isEmpty()) {
            Pattern filterPattern = Pattern.compile(filterArgument, Pattern.CASE_INSENSITIVE);
            bodyElements = bodyElements.stream()
                    .filter(element -> {
                        Matcher m = filterPattern.matcher(element.text());
                        return m.find();
                    })
                    .collect(Elements::new, Elements::add, Elements::addAll);
        }

        // Display content
        for (Element element : bodyElements) {
            // Handle different element types (headings, paragraphs, lists, etc.)
            if (element.tagName().matches("h[1-6]")) {
                // Headings
                terminal.writer().println(element.text());
            } else if (element.tagName().equals("p")) {
                // Paragraphs
                terminal.writer().println(element.text() + "\n");
            } else if (element.tagName().matches("ul|ol")) {
                // Lists
                for (Element listItem : element.children()) {
                    terminal.writer().println("  * " + listItem.text());
                }
                terminal.writer().println(); 
            } 
            // ... (add more cases for other HTML elements as needed)
        }
    }
}

/** Copyright © 2024 Devin B. Royal. All Rights reserved. */

