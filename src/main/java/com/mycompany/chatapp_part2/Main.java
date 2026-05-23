package com.mycompany.chatapp_part2;

import com.mycompany.chatapp_part2.Login;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main — Part 2 entry point.
 *
 * Flow:
 *   1. Register a user (using existing Login class from Part 1)
 *   2. Log in
 *   3. Ask how many messages to send
 *   4. Loop that many times — each iteration creates a Message object,
 *      validates recipient and length, generates a hash, and asks
 *      what to do with the message:
 *        1 = Send      → recorded in the final report
 *        2 = Disregard → message is discarded
 *        3 = Store     → saved to newstored_messages.json
 *   5. Print a final report of all sent messages
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    // Path to the JSON file — sits in the root project folder next to pom.xml
    private static final String JSON_FILE = "newstored_messages.json";

    public static void main(String[] args) {

        printWelcomeBanner();

        Login login = new Login();

        // ── REGISTRATION ──────────────────────────────────────────────────────
        System.out.println("=== REGISTRATION ===");

        String username = promptValidUsername(login);
        String password = promptValidPassword(login);
        String phone    = promptValidPhone(login);

        String registerResult = login.registerUser(username, password, phone);
        System.out.println(registerResult + "\n");

        // ── LOGIN ─────────────────────────────────────────────────────────────
        System.out.println("=== LOGIN ===");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String loginUser = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String loginPass = scanner.nextLine().trim();

            boolean status = login.loginUser(loginUser, loginPass);
            System.out.println(login.returnLoginStatus(status));

            if (status) {
                loggedIn = true;
            } else {
                System.out.println("Please try again.\n");
            }
        }

        // ── HOW MANY MESSAGES ─────────────────────────────────────────────────
        System.out.println("\n=== QUICKCHAT ===");
        int messageCount = 0;

        while (messageCount <= 0) {
            System.out.print("How many messages would you like to send? ");
            try {
                messageCount = Integer.parseInt(scanner.nextLine().trim());
                if (messageCount <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        // Arrays to store sent message details for the final report
        String[] sentRecipients = new String[messageCount];
        String[] sentMessages   = new String[messageCount];
        String[] sentHashes     = new String[messageCount];
        String[] sentIds        = new String[messageCount];
        int      sentCount      = 0;

        // ── MESSAGE LOOP ──────────────────────────────────────────────────────
        for (int i = 0; i < messageCount; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + messageCount + " ---");

            // Create a new Message object for this message number
            Message message = new Message(i + 1);

            // Recipient
            String recipient = "";
            while (true) {
                System.out.print("Enter recipient number (+27XXXXXXXXX): ");
                recipient = scanner.nextLine().trim();
                String recipientCheck = message.checkRecipientCell(recipient);
                System.out.println(recipientCheck);
                if (recipientCheck.equals("Cell phone number successfully added.")) {
                    message.setRecipient(recipient);
                    break;
                }
            }

            // Message text
            String messageText = "";
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = scanner.nextLine().trim();
                String lengthCheck = message.checkMessageLength(messageText);
                System.out.println(lengthCheck);
                if (lengthCheck.equals("Message ready to send.")) {
                    message.setMessageText(messageText);
                    break;
                }
            }

            // Generate hash and show details
            String hash      = message.createMessageHash();
            String messageId = message.getMessageId();

            System.out.println("\nMessage ID   : " + messageId);
            System.out.println("Message Hash : " + hash);

            // Action menu
            System.out.println("\nWhat would you like to do?");
            System.out.println("  1. Send");
            System.out.println("  2. Disregard");
            System.out.println("  3. Store for later");
            System.out.print("Choose (1/2/3): ");

            int choice = 0;
            while (choice < 1 || choice > 3) {
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice < 1 || choice > 3) {
                        System.out.print("Please enter 1, 2, or 3: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Please enter 1, 2, or 3: ");
                }
            }

            // Call sentMessage() on the Message object and display result
            String actionResult = message.sentMessage(choice);
            System.out.println(actionResult);

            // Handle each choice
            if (choice == 1) {
                // SEND — record for the final report
                sentRecipients[sentCount] = recipient;
                sentMessages[sentCount]   = messageText;
                sentHashes[sentCount]     = hash;
                sentIds[sentCount]        = messageId;
                sentCount++;

            } else if (choice == 2) {
                // DISREGARD — do nothing, message is dropped
                System.out.println("Message has been disregarded.");

            } else if (choice == 3) {
                // STORE — save to newstored_messages.json
                String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new java.util.Date());
                saveMessageToJson(recipient, messageText, messageId, hash, timestamp);
                System.out.println("Message saved to " + JSON_FILE);
            }
        }

        // ── FINAL REPORT ──────────────────────────────────────────────────────
        System.out.println("\n=== SENT MESSAGES REPORT ===");

        if (sentCount == 0) {
            System.out.println("No messages were sent.");
        } else {
            for (int i = 0; i < sentCount; i++) {
                System.out.println("\nMessage " + (i + 1));
                System.out.println("  ID        : " + sentIds[i]);
                System.out.println("  Recipient : " + sentRecipients[i]);
                System.out.println("  Hash      : " + sentHashes[i]);
                System.out.println("  Message   : " + sentMessages[i]);
            }
        }

        System.out.println("\nThank you for using QuickChat. Goodbye!");
        scanner.close();
    }

    // ─── Save message to JSON file ────────────────────────────────────────────
    /**
     * Saves a stored message to newstored_messages.json in the project root.
     *
     * The file is a JSON array. If it does not exist yet it is created.
     * Each new message is appended as a new object inside the array.
     *
     * Example entry:
     * {
     *   "recipient": "+27718693002",
     *   "message": "Hi Mike, can you join us for dinner tonight?",
     *   "message_id": "1234567890",
     *   "message_hash": "12:1:HITONIGHT",
     *   "timestamp": "2026-05-23 08:30:00"
     * }
     */
    private static void saveMessageToJson(String recipient, String message,
                                          String messageId, String messageHash,
                                          String timestamp) {
        // Build the JSON object string for this one message
        String jsonEntry = "  {\n"
                + "    \"recipient\": \""    + recipient                          + "\",\n"
                + "    \"message\": \""      + message.replace("\"", "\\\"")      + "\",\n"
                + "    \"message_id\": \""   + messageId                          + "\",\n"
                + "    \"message_hash\": \"" + messageHash                        + "\",\n"
                + "    \"timestamp\": \""    + timestamp                          + "\"\n"
                + "  }";

        try {
            java.io.File file = new java.io.File(JSON_FILE);

            // If the file does not exist yet, create it with the first entry
            if (!file.exists() || file.length() == 0) {
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write("[\n" + jsonEntry + "\n]");
                }
            } else {
                // File already exists — read it, remove the closing ]
                // and append the new entry before adding ] back
                String content = new String(Files.readAllBytes(Paths.get(JSON_FILE))).trim();

                if (content.endsWith("]")) {
                    content = content.substring(0, content.length() - 1).trim();
                    // If array already has entries, add a comma before the new one
                    if (!content.endsWith("[")) {
                        content += ",\n";
                    }
                }
                content += jsonEntry + "\n]";

                Files.write(Paths.get(JSON_FILE), content.getBytes());
            }

        } catch (IOException e) {
            System.out.println("Error saving to JSON file: " + e.getMessage());
        }
    }

    // ─── Welcome banner ───────────────────────────────────────────────────────
    private static void printWelcomeBanner() {
        System.out.println("===========================================");
        System.out.println("        Welcome to QuickChat");
        System.out.println("        PROG5121 - Programming 1A");
        System.out.println("===========================================\n");
    }

    // ─── Prompt helpers ───────────────────────────────────────────────────────
    private static String promptValidUsername(Login login) {
        while (true) {
            System.out.print("Enter username (contains _ and max 5 characters): ");
            String input = scanner.nextLine().trim();
            if (login.checkUserName(input)) {
                System.out.println("Username successfully captured.");
                return input;
            }
            System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
        }
    }

    private static String promptValidPassword(Login login) {
        while (true) {
            System.out.print("Enter password (min 8 chars, 1 capital, 1 number, 1 special): ");
            String input = scanner.nextLine().trim();
            if (login.checkPasswordComplexity(input)) {
                System.out.println("Password successfully captured.");
                return input;
            }
            System.out.println("Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }
    }

    private static String promptValidPhone(Login login) {
        while (true) {
            System.out.print("Enter phone number (+27XXXXXXXXX): ");
            String input = scanner.nextLine().trim();
            if (login.checkCellPhoneNumber(input)) {
                System.out.println("Cell phone number successfully captured.");
                return input;
            }
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }
    }
}
