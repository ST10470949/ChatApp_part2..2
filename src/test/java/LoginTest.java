import com.mycompany.chatapp_part2.Login;
// This imports the Login class so we can test its methods

import org.junit.jupiter.api.Test;
// This imports the @Test annotation used for unit testing

import static org.junit.jupiter.api.Assertions.*;
// This imports testing tools like assertTrue and assertFalse

public class LoginTest {

    Login login = new Login();
    // We create an object of the Login class so we can call its methods

    // ================= USERNAME TESTS =================

    @Test
    public void testValidUsername() {
        // This test checks a correct username
        // Expected result: TRUE (valid username)
        // and remember here our username can't be more than 5 characters(A-Z)
        assertTrue(login.checkUserName("ab_1"));
    }

    @Test
    public void testInvalidUsername_NoUnderscore() {
        // This test checks a username without an underscore
        // Expected result: FALSE (invalid username)
        // reason why this is incorrect FALSE it is bcz (no "_") which makes it false
        assertFalse(login.checkUserName("abcd"));
    }

    @Test
    public void testInvalidUsername_TooLong() {
        // This test checks a username that is too long
        // Expected result: FALSE (invalid username)
        // reason for this to be FALSE also is that here we have more than 5 characters here, yes we have the "_" but the problem it have more than 5 characters.
        assertFalse(login.checkUserName("abc_123"));
    }

    // ================= PASSWORD TESTS =================

    @Test
    public void testValidPassword() {
        // This test checks a strong password
        // Expected result: TRUE (valid password)
        // reason for this test to be TRUE is that we have uppercase 1st, characters are no more than 8 and we have the special character.
        assertTrue(login.checkPasswordComplexity("Abc@1234"));
    }

    @Test
    public void testInvalidPassword_NoCapital() {
        // This test checks password with no uppercase letter
        // Expected result: FALSE (invalid password)
        // Here we have no uppercase reason for the test to be FALSE
        assertFalse(login.checkPasswordComplexity("abc@1234"));
    }

    @Test
    public void testInvalidPassword_NoNumber() {
        // This test checks password with no number
        // Expected result: FALSE (invalid password)
        // Here the tests that our password doesn't have the number
        
        assertFalse(login.checkPasswordComplexity("Abc@abcd"));
    }

    // ================= PHONE NUMBER TESTS =================

    @Test
    public void testValidPhoneNumber() {
        // This test checks a correct South African phone number
        // Expected result: TRUE (valid phone number)
        assertTrue(login.checkCellPhoneNumber("+27831234567"));
    }

    @Test
    public void testInvalidPhoneNumber() {
        // This test checks a phone number without +27
        // Expected result: FALSE (invalid phone number)
        assertFalse(login.checkCellPhoneNumber("0831234567"));
    }

    // ================= LOGIN TESTS =================

    @Test
    public void testLoginSuccess() {
        // First we register a user with correct details
        login.registerUser("ab_1", "Abc@1234", "+27831234567");

        // Then we test logging in with correct details
        // Expected result: TRUE (login successful)
        assertTrue(login.loginUser("ab_1", "Abc@1234"));
    }

    @Test
    public void testLoginFail() {
        // First we register a user
        login.registerUser("ab_1", "Abc@1234", "+27831234567");

        // Then we try login with WRONG password
        // Expected result: FALSE (login failed)
        assertFalse(login.loginUser("ab_1", "wrongPass"));
    }
}