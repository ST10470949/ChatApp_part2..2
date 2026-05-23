package com.mycompany.chatapp_part2;

public class Login {

    // These are variables used to store the user's details

    // 'private' means these variables can only be used inside this class (Login class)
    // This helps protect the data from being changed anywhere else

    private String username; 
    // This variable stores the user's username (e.g. ab_1)

    private String password; 
    // This variable stores the user's password (e.g. Abc@1234)

    private String phoneNumber; 
    // This variable stores the user's phone number (e.g. +27831234567)
    
    // USERNAME VALIDATION
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

        // PASSWORD VALIDATION
     public boolean checkPasswordComplexity(String password) {

         // These variables will help us check if the password meets all rules
         boolean hasUpperCase = false;   // checks if there is a capital letter (A-Z)
         boolean hasNumber = false;      // checks if there is a number (0-9)
         boolean hasSpecialChar = false; // checks if there is a special character like @, !, #

         // This loop goes through each character (letter) in the password one by one
         for (char c : password.toCharArray()) {

             // Check if the current character is an uppercase letter
             if (Character.isUpperCase(c)) {
                 hasUpperCase = true; // if yes, we mark it as true
             }

             // Check if the current character is a number
             if (Character.isDigit(c)) {
                 hasNumber = true; // if yes, we mark it as true
             }

             // Check if the character is NOT a letter or a number
             // That means it is a special character like @, %, !
             if (!Character.isLetterOrDigit(c)) {
                 hasSpecialChar = true; // mark it as true
             }
         }

         // Finally, we check ALL conditions:
         // 1. Password must be at least 8 characters long
         // 2. Must have a capital letter
         // 3. Must have a number
         // 4. Must have a special character
         return password.length() >= 8 && hasUpperCase && hasNumber && hasSpecialChar;
     }

        // PHONE NUMBER VALIDATION
    public boolean checkCellPhoneNumber(String phoneNumber) {

        // startsWith("+27") checks if the number begins with South Africa's code
        // length() <= 12 makes sure the number is not too long
        // Both conditions must be TRUE for the number to be valid
        return phoneNumber.startsWith("+27") && phoneNumber.length() <= 12;
    }


    // REGISTER USER
    public String registerUser(String username, String password, String phoneNumber) {

        // Step 1: Check if the username is valid
        // If it is NOT valid, return an error message immediately
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure it contains an underscore and is no more than 5 characters.";
        }

        // Step 2: Check if the password is strong enough
        // If it is NOT valid, return an error message
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; must contain 8 characters, a capital letter, a number, and a special character.";
        }

        // Step 3: Check if the phone number is correct
        // If it is NOT valid, return an error message
        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // Step 4: If ALL checks passed, we save the user's details
        // 'this' means we are saving the values into this object's variables
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;

        // Step 5: Return a success message
        return "User registered successfully.";
    }

    // LOGIN USER
    public boolean loginUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

        // LOGIN STATUS MESSAGE
    public String returnLoginStatus(boolean status) {

        // This method receives a value called "status"
        // "status" can only be true or false (because it is a boolean)

        // If status is TRUE (meaning login was successful)
        if (status) {

            // Return this success message to the user
            return "Login successful. Welcome, it is a pleasure to have you here!!";

        } else {

            // If status is FALSE (meaning login failed)
            // Return this error message
            return "Login failed. Username or password incorrect.";
        }
    }
}