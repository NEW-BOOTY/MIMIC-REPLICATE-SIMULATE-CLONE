/**
 * Copyright © 2024 Devin Benard Royal
 * All rights reserved.
 */

import java.util.ArrayList;
import java.util.List;

class Transaction {
    private String id;
    private double amount;
    private String description;
    private List<String> attachments;

    public Transaction(String id, double amount, String description) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.attachments = new ArrayList<>();
    }

    public void transfer(double amount) {
        if (amount < 0) {
            System.out.println("Invalid transfer amount. Attempting to correct...");
            amount = Math.abs(amount);
        }
        this.amount += amount;
        System.out.println("Transferred amount: " + amount);
    }

    public void addInput(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Invalid input. Attempting to correct...");
            input = "Default input";
        }
        this.description += " " + input;
        System.out.println("Added input: " + input);
    }

    public void attach(String attachment) {
        if (attachment == null || attachment.isEmpty()) {
            System.out.println("Invalid attachment. Attempting to correct...");
            attachment = "DefaultAttachment.pdf";
        }
        this.attachments.add(attachment);
        System.out.println("Attached: " + attachment);
    }

    public void link(String link) {
        if (link == null || link.isEmpty()) {
            System.out.println("Invalid link. Attempting to correct...");
            link = "http://defaultlink.com";
        }
        this.description += " [Link: " + link + "]";
        System.out.println("Linked: " + link);
    }

    public void embed(String embedData) {
        if (embedData == null || embedData.isEmpty()) {
            System.out.println("Invalid embed data. Attempting to correct...");
            embedData = "Default embedded data";
        }
        this.description += " [Embed: " + embedData + "]";
        System.out.println("Embedded: " + embedData);
    }

    public void complete() {
        System.out.println("Transaction completed: " + this.toString());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", attachments=" + attachments +
                '}';
    }

    public static void main(String[] args) {
        try {
            Transaction transaction = new Transaction("TXN12345", 100.0, "Initial transaction");
            transaction.transfer(50.0);
            transaction.addInput("Additional input");
            transaction.attach("Attachment1.pdf");
            transaction.link("http://example.com");
            transaction.embed("Embedded data");
            transaction.complete();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
