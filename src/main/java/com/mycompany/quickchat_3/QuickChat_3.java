/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchat_3;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author User
 */

class Login {

    String storedUsername;
    String storedPassword;
    String storedCell;

    boolean checkUsername(String username) {
        return username.contains("_") && username.length() >= 5;
    }

    boolean checkPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[^a-zA-Z0-9].*");
    }

    boolean checkCell(String cell) {
        return cell.matches("\\+27\\d{9}");
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

    boolean login(String username, String password) {
        return username.equals(storedUsername) &&
               password.equals(storedPassword);
    }

    String loginStatus(String username, String password) {
        if (login(username, password)) {
            return "Login successful. Welcome " + username;
        } else {
            return "Login failed.";
        }
    }
}

public class QuickChat_3 {

    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
        Login user = new Login();

        // ===== REGISTER =====
        System.out.println("=== SIGN UP ===");
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Cellphone: ");
        String cell = sc.nextLine();

        System.out.println(user.register(username, password, cell));

        // ===== LOGIN =====
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        String loginResult = user.loginStatus(u, p);
        System.out.println(loginResult);

        // Stop if login fails
        if (!loginResult.contains("successful")) {
            sc.close();
            return;
        }

        // ===== MESSAGE SECTION =====
        System.out.println("\n=== MESSAGE SYSTEM ===");

        System.out.print("Enter Message ID: ");
        String messageID = sc.nextLine();

        if (!Message.checkMessageID(messageID)) {
            System.out.println("Message ID is invalid.");
            sc.close();
            return;
        }

        System.out.print("Enter recipient cell number: ");
        String recipient = sc.nextLine();
        System.out.println(Message.checkRecipientCell(recipient));

        System.out.print("Enter your message: ");
        String message = sc.nextLine();

        String hash = Message.checkMessageHash(messageID, message);
        System.out.println("Message Hash: " + hash);

        System.out.println("\nChoose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Store Message");
        System.out.println("3. Disregard Message");

        int choice = sc.nextInt();

        System.out.println(Message.sendMessage(choice, message));

        System.out.println("\n--- Sent Messages ---");
        System.out.println(Message.printMessages());

        System.out.println("Total messages sent: " + Message.returnTotalMessages());

        sc.close();
    }
}
    

