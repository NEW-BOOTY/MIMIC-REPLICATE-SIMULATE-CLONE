/** Copyright © 2024 Devin B. Royal. All Rights reserved. */
package com.devinroyal;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TerminalWebBrowser {
    public static void main(String[] args) {
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            while (true) {
                String url = reader.readLine("Enter URL: ");
                if (url.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    Document doc = Jsoup.connect(url).get();
                    String title = doc.title();
                    String body = doc.body().text();

                    terminal.writer().println("Title: " + title);
                    terminal.writer().println("Content: " + body);
                } catch (IOException e) {
                    terminal.writer().println("Failed to retrieve content from the URL.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/** Copyright © 2024 Devin B. Royal. All Rights reserved. */
