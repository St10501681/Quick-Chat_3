/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.quickchat_3;

import java.util.ArrayList;
import java.util.Scanner;

class Login {
    private String storedUsername;
    private String storedPassword;
    private String storedCell;

    boolean checkUsername(String username) {
        return username != null && username.contains("_") && username.length() >= 5;
    }

    boolean checkPassword(String password) {
        return password != null
            && password.length() >= 8
            && password.matches(".*[A-Z].*")
            && password.matches(".*[0-9].*")
            && password.matches(".*[^a-zA-Z0-9].*");
    }

    boolean checkCell(String cell) {
        return cell != null && cell.matches("\\+27\\d{9}");
    }

    String register(String username, String password, String cell) {
        String message = "";

        if (!checkUsername(username)) {
            message += "Username must contain '_' and be at least 5 characters.\n";
        }

        if (!checkPassword(password)) {
            message += "Password must be 8+ chars, include capital letter, number, and special character.\n";
        }

        if (!checkCell(cell)) {
            message += "Cellphone must start with +27 and have 9 digits.\n";
        }

        if (!message.isEmpty()) {
            return "Registration failed:\n" + message;
        }

        storedUsername = username;
        storedPassword = password;
        storedCell = cell;

        return "Registration successful!";
    }

    boolean isRegistered() {
        return storedUsername != null && storedPassword != null && storedCell != null;
    }

    boolean login(String username, String password) {
        return isRegistered()
            && username.equals(storedUsername)
            && password.equals(storedPassword);
    }

    String loginStatus(String username, String password) {
        if (login(username, password)) {
            return "Login successful. Welcome " + username;
        }

        return "Login failed.";
    }
}

class Message {
    static ArrayList<MessageDetails> sentMessages = new ArrayList<>();
    static ArrayList<MessageDetails> disregardedMessages = new ArrayList<>();
    static ArrayList<MessageDetails> storedMessages = new ArrayList<>();
    static ArrayList<String> messageHashes = new ArrayList<>();
    static ArrayList<String> messageIDs = new ArrayList<>();
    static int totalMessages = 0;

    static class MessageDetails {
        String messageID;
        String messageHash;
        String sender;
        String recipient;
        String message;

        MessageDetails(String messageID, String messageHash, String sender, String recipient, String message) {
            this.messageID = messageID;
            this.messageHash = messageHash;
            this.sender = sender;
            this.recipient = recipient;
            this.message = message;
        }

        String displayDetails() {
            return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + message + "\n";
        }

        String displayFullReport() {
            return "Sender: " + sender + "\n" + displayDetails();
        }
    }

    public static boolean checkMessageID(String messageID) {
        return messageID != null && !messageID.isBlank() && messageID.length() <= 10;
    }

    public static String checkRecipientCell(String cell) {
        if (cell != null && (cell.matches("0\\d{9}") || cell.matches("\\+27\\d{9}"))) {
            return "Cell number successfully captured";
        }

        return "Cell number is incorrectly formatted";
    }

    public static String checkMessageHash(String messageID, String message) {
        if (message == null || message.length() < 2) {
            return messageID + ":INVALID";
        }

        String firstTwo = message.substring(0, 2).toUpperCase();
        String lastTwo = message.substring(message.length() - 2).toUpperCase();

        return messageID + ":" + firstTwo + lastTwo;
    }

    public static String sendMessage(MessageDetails details) {
        sentMessages.add(details);
        messageIDs.add(details.messageID);
        messageHashes.add(details.messageHash);
        totalMessages++;
        return "Message successfully sent.";
    }

    public static String storeMessage(MessageDetails details) {
        storedMessages.add(details);
        messageIDs.add(details.messageID);
        messageHashes.add(details.messageHash);
        return "Message successfully stored.";
    }

    public static String disregardMessage(MessageDetails details) {
        disregardedMessages.add(details);
        return "Message disregarded.";
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }

        String result = "";
        for (MessageDetails details : sentMessages) {
            result += details.displayDetails() + "\n";
        }

        return result;
    }

    public static String printDisregardedMessages() {
        if (disregardedMessages.isEmpty()) {
            return "No disregarded messages.";
        }

        String result = "";
        for (MessageDetails details : disregardedMessages) {
            result += details.displayDetails() + "\n";
        }

        return result;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public static void messageMenu(Scanner sc, String sender) {
        System.out.println("\nWelcome to QuickChat");

        int maxMessages = readMessageLimit(sc);
        int sentCount = 0;
        char choice = ' ';

        do {
            System.out.println("\nChoose an option:");
            System.out.println("a) Create a Message");
            System.out.println("b) Show recently sent messages");
            System.out.println("c) Show disregarded messages");
            System.out.println("d) Stored Messages");
            System.out.println("e) Quit");
            System.out.print("Enter choice: ");

            String input = sc.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                System.out.println("Please enter a menu option.");
                continue;
            }

            choice = input.charAt(0);

            switch (choice) {
                case 'a' -> {
                    if (sentCount >= maxMessages) {
                        System.out.println("You have reached your message limit.");
                        break;
                    }

                    if (createMessage(sc, sender)) {
                        sentCount++;
                    }

                    if (sentCount == maxMessages) {
                        System.out.println("\n=== All Messages Sent ===");
                        System.out.println(printMessages());
                        System.out.println("Total messages sent: " + returnTotalMessages());
                    }
                }
                case 'b' -> System.out.println(printMessages());
                case 'c' -> {
                    System.out.println("\n=== Disregarded Messages ===");
                    System.out.println(printDisregardedMessages());
                }
                case 'd' -> storedMessagesMenu(sc);
                case 'e' -> {
                    System.out.println("\n=== Sent Message Details ===");
                    System.out.println(printMessages());
                    System.out.println("Total messages sent: " + returnTotalMessages());
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 'e');
    }

    private static int readMessageLimit(Scanner sc) {
        while (true) {
            System.out.print("How many messages would you like to send? ");
            String input = sc.nextLine().trim();

            try {
                int limit = Integer.parseInt(input);

                if (limit > 0) {
                    return limit;
                }

                System.out.println("Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static boolean createMessage(Scanner sc, String sender) {
        System.out.print("Enter Message ID: ");
        String messageID = sc.nextLine().trim();

        if (!checkMessageID(messageID)) {
            System.out.println("Message ID is invalid.");
            return false;
        }

        System.out.print("Enter recipient cell number: ");
        String recipient = sc.nextLine().trim();
        String recipientStatus = checkRecipientCell(recipient);
        System.out.println(recipientStatus);

        if (!recipientStatus.equals("Cell number successfully captured")) {
            return false;
        }

        System.out.print("Enter your message: ");
        String message = sc.nextLine();
        String hash = checkMessageHash(messageID, message);

        MessageDetails details = new MessageDetails(messageID, hash, sender, recipient, message);
        System.out.println("Message Hash: " + hash);

        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1. Send Message");
        System.out.println("2. Store Message");
        System.out.println("3. Disregard Message");
        System.out.print("Enter choice: ");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.println(sendMessage(details));
                return true;
            }
            case "2" -> {
                System.out.println(storeMessage(details));
                return false;
            }
            case "3" -> {
                System.out.println(disregardMessage(details));
                return false;
            }
            default -> {
                System.out.println("Invalid option. Message disregarded.");
                disregardMessage(details);
                return false;
            }
        }
    }

    private static void storedMessagesMenu(Scanner sc) {
        char option = ' ';

        do {
            System.out.println("\n=== Stored Messages Menu ===");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search messages for a recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display full report of stored messages");
            System.out.println("g) Return to main menu");
            System.out.print("Enter choice: ");

            String input = sc.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                System.out.println("Please enter a menu option.");
                continue;
            }

            option = input.charAt(0);

            switch (option) {
                case 'a' -> displayStoredSendersAndRecipients();
                case 'b' -> displayLongestStoredMessage();
                case 'c' -> searchByMessageID(sc);
                case 'd' -> searchByRecipient(sc);
                case 'e' -> deleteByHash(sc);
                case 'f' -> displayStoredMessagesReport();
                case 'g' -> System.out.println("Returning to main menu.");
                default -> System.out.println("Invalid option.");
            }
        } while (option != 'g');
    }

    private static void displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }

        for (MessageDetails details : storedMessages) {
            System.out.println("Sender: " + details.sender);
            System.out.println("Recipient: " + details.recipient);
            System.out.println();
        }
    }

    private static void displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }

        MessageDetails longest = storedMessages.get(0);

        for (MessageDetails details : storedMessages) {
            if (details.message.length() > longest.message.length()) {
                longest = details;
            }
        }

        System.out.println("Longest stored message:");
        System.out.println(longest.displayDetails());
    }

    private static void searchByMessageID(Scanner sc) {
        System.out.print("Enter message ID to search: ");
        String searchID = sc.nextLine().trim();

        for (MessageDetails details : storedMessages) {
            if (details.messageID.equals(searchID)) {
                System.out.println("Recipient: " + details.recipient);
                System.out.println("Message: " + details.message);
                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    private static void searchByRecipient(Scanner sc) {
        System.out.print("Enter recipient to search: ");
        String searchRecipient = sc.nextLine().trim();
        boolean found = false;

        for (MessageDetails details : storedMessages) {
            if (details.recipient.equals(searchRecipient)) {
                System.out.println(details.displayDetails());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for that recipient.");
        }
    }

    private static void deleteByHash(Scanner sc) {
        System.out.print("Enter message hash to delete: ");
        String searchHash = sc.nextLine().trim();

        for (int i = 0; i < storedMessages.size(); i++) {
            MessageDetails details = storedMessages.get(i);

            if (details.messageHash.equals(searchHash)) {
                storedMessages.remove(i);
                messageIDs.remove(details.messageID);
                messageHashes.remove(details.messageHash);
                System.out.println("Message successfully deleted.");
                return;
            }
        }

        System.out.println("Message hash not found.");
    }

    private static void displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }

        System.out.println("=== Stored Messages Full Report ===");
        for (MessageDetails details : storedMessages) {
            System.out.println(details.displayFullReport());
        }
    }
}

public class QuickChat_3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Login user = new Login();

        System.out.println("=== SIGN UP ===");
        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Cellphone: ");
        String cell = sc.nextLine().trim();

        System.out.println(user.register(username, password, cell));

        if (!user.isRegistered()) {
            sc.close();
            return;
        }

        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String loginUsername = sc.nextLine().trim();

        System.out.print("Password: ");
        String loginPassword = sc.nextLine();

        String loginResult = user.loginStatus(loginUsername, loginPassword);
        System.out.println(loginResult);

        if (!user.login(loginUsername, loginPassword)) {
            sc.close();
            return;
        }

        Message.messageMenu(sc, loginUsername);
        sc.close();
    }
}
