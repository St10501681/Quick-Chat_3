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
    static ArrayList<String> sentMessages = new ArrayList<>();
    static int totalMessages = 0;

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
     public static String sendMessage(String message) {
        sentMessages.add(message);
        totalMessages++;
        return "Message successfully sent.";
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }

        String result = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            result += (i + 1) + ". " + sentMessages.get(i) + "\n";
        }
        return result;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public static void messageMenu(Scanner sc) {
        System.out.println("\nWelcome to QuickChat");

        int maxMessages = readMessageLimit(sc);
        int sentCount = 0;
        char choice = ' ';
        do {
            System.out.println("\nChoose an option:");
            System.out.println("a) Send Messages");
            System.out.println("b) Show recently sent messages");
            System.out.println("c) Quit");
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

                    sendNewMessage(sc);
                    sentCount++;
                }
                case 'b' -> System.out.println(printMessages());
                case 'c' -> {
                    System.out.println("Total messages sent: " + returnTotalMessages());
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
            } while (choice != 'c');
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

    private static void sendNewMessage(Scanner sc) {
        System.out.print("Enter Message ID: ");
        String messageID = sc.nextLine().trim();

        if (!checkMessageID(messageID)) {
            System.out.println("Message ID is invalid.");
            return;
        }

        System.out.print("Enter recipient cell number: ");
        String recipient = sc.nextLine().trim();
        String recipientStatus = checkRecipientCell(recipient);
        System.out.println(recipientStatus);

if (!recipientStatus.equals("Cell number successfully captured")) {
            return;
        }

        System.out.print("Enter your message: ");
        String message = sc.nextLine();

        String hash = checkMessageHash(messageID, message);
        System.out.println("Message Hash: " + hash);

        System.out.println(sendMessage(message));
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
        
        Message.messageMenu(sc);
        sc.close();
    }
}