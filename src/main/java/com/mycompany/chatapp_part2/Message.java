package com.mycompany.chatapp_part2;

import java.util.Random;


public class Message {

    
    private String messageId;
    private int    messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(int messageNumber) {
        this.messageNumber = messageNumber;
        this.messageId     = generateMessageId();
    }

    
    private String generateMessageId() {
        Random rand = new Random();
        long number = 1_000_000_000L + (long)(rand.nextDouble() * 9_000_000_000L);
        return Long.toString(number);
    }

    
    public boolean checkMessageID() {
        return messageId != null && messageId.length() <= 10;
    }

    
    public String checkMessageLength(String text) {
        if (text == null) text = "";

        int length = text.length();

        if (length <= 250) {
            return "Message ready to send.";
        }

        int over = length - 250;
        return "Message exceeds 250 characters by " + over + ", please reduce size.";
    }

    
    public String checkRecipientCell(String cellNumber) {
        if (cellNumber != null
                && cellNumber.startsWith("+27")
                && cellNumber.length() == 12) {
            return "Cell phone number successfully added.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international dialling code, please correct the number and try again.";
    }

    
    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) {
            messageHash = "";
            return messageHash;
        }

        String[] words   = messageText.trim().split("\\s+");
        String firstWord = stripPunctuation(words[0]);
        String lastWord  = stripPunctuation(words[words.length - 1]);
        String firstTwo  = messageId.substring(0, 2);

        messageHash = (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    // Removes non-alphabetic characters from a word
    private String stripPunctuation(String word) {
        return word.replaceAll("[^a-zA-Z]", "");
    }

   
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:  return "Message successfully sent.";
            case 2:  return "Press 0 to delete the message.";
            case 3:  return "Message successfully stored.";
            default: return "Invalid option selected.";
        }
    }

    // ─── Getters and Setters ──────────────────────────────────────────────────
    public String getMessageId()     { return messageId;     }
    public int    getMessageNumber() { return messageNumber; }
    public String getRecipient()     { return recipient;     }
    public String getMessageText()   { return messageText;   }
    public String getMessageHash()   { return messageHash;   }

    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }
    public void setRecipient(String recipient)       { this.recipient     = recipient;     }
    public void setMessageText(String messageText)   { this.messageText   = messageText;   }
}
