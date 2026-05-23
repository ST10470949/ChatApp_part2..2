/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.chatapp_part2.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
 
    // ─── Declare objects available to every @Test ─────────────────────────────
    private Message message1;
    private Message message2;
 
    // ─── @Before: fresh objects created before EVERY single test ─────────────
    @BeforeEach
    public void setUp() {
        // Message 1 — POE test data (valid recipient)
        message1 = new Message(1);
        message1.setRecipient("+27718693002");
        message1.setMessageText("Hi Mike, can you join us for dinner tonight?");
 
        // Message 2 — POE test data (invalid recipient on purpose)
        message2 = new Message(2);
        message2.setRecipient("08575975889");
        message2.setMessageText("Hi Keegan, did you receive the payment?");
    }
 
    
    @Test
    public void testCheckMessageLength_validMessage_returnsSuccess() {
        // ARRANGE
        String text = "Hi Mike, can you join us for dinner tonight?";
        // ACT
        String result = message1.checkMessageLength(text);
        // ASSERT
        assertEquals("Message ready to send.", result);
    }
 
    @Test
    public void testCheckMessageLength_over250chars_returnsFailureWithCount() {
        // ARRANGE — 260 characters = 10 over the limit
        String text = "A".repeat(260);
        // ACT
        String result = message1.checkMessageLength(text);
        // ASSERT
        assertEquals("Message exceeds 250 characters by 10, please reduce size.", result);
    }
 
    @Test
    public void testCheckMessageLength_exactlyAtLimit_returnsSuccess() {
        // ARRANGE — exactly 250 characters
        String text = "B".repeat(250);
        // ACT
        String result = message1.checkMessageLength(text);
        // ASSERT
        assertEquals("Message ready to send.", result);
    }
 
    @Test
    public void testCheckMessageLength_oneOver_returnsFailureWithCountOf1() {
        // ARRANGE — exactly 251 characters
        String text = "C".repeat(251);
        // ACT
        String result = message1.checkMessageLength(text);
        // ASSERT
        assertEquals("Message exceeds 250 characters by 1, please reduce size.", result);
    }
 
    // =========================================================================
    // RECIPIENT CELL NUMBER TESTS
    // =========================================================================
 
    @Test
    public void testCheckRecipientCell_validNumber_returnsSuccess() {
        // ARRANGE — POE test data valid number
        String validNumber = "+27718693002";
        // ACT
        String result = message1.checkRecipientCell(validNumber);
        // ASSERT
        assertEquals("Cell phone number successfully added.", result);
    }
 
    @Test
    public void testCheckRecipientCell_invalidNumber_returnsFailure() {
        // ARRANGE — POE test data invalid number (no +27 prefix)
        String invalidNumber = "08575975889";
        // ACT
        String result = message2.checkRecipientCell(invalidNumber);
        // ASSERT
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international dialling code, please correct the number and try again.",
            result
        );
    }
 
    // =========================================================================
    // MESSAGE HASH TESTS
    // =========================================================================
 
    @Test
    public void testCreateMessageHash_correctFormat_endsWithExpectedWords() {
        // ARRANGE
        // message1 text: "Hi Mike, can you join us for dinner tonight?"
        // First word stripped: HI
        // Last word stripped : TONIGHT
        // messageNumber = 1 so hash must end with :1:HITONIGHT
 
        // ACT
        String hash = message1.createMessageHash();
 
        // ASSERT — endsWith because the first 2 digits of the random ID vary
        assertTrue("Hash should end with :1:HITONIGHT", hash.endsWith(":1:HITONIGHT"));
    }
 
    @Test
    public void testCreateMessageHash_isUppercase() {
        // ACT
        String hash = message1.createMessageHash();
        // ASSERT — the full hash must equal its own uppercase version
        assertEquals("Hash must be entirely uppercase", hash.toUpperCase(), hash);
    }
 
    @Test
    public void testCreateMessageHash_multipleMessages_loopTest() {
        // ARRANGE — both POE messages and their expected hash endings
        String[] texts = {
            "Hi Mike, can you join us for dinner tonight?",   // 1 → HITONIGHT
            "Hi Keegan, did you receive the payment?"          // 2 → HIPAYMENT
        };
        String[] expectedEndings = {
            ":1:HITONIGHT",
            ":2:HIPAYMENT"
        };
 
        // ACT + ASSERT in a loop (POE specifically requires a loop test)
        for (int i = 0; i < texts.length; i++) {
            Message msg = new Message(i + 1);
            msg.setMessageText(texts[i]);
            String hash = msg.createMessageHash();
 
            assertTrue(
                "Hash for message " + (i + 1) + " should end with " + expectedEndings[i],
                hash.endsWith(expectedEndings[i])
            );
        }
    }
 
    // =========================================================================
    // MESSAGE ID TESTS
    // =========================================================================
 
    @Test
    public void testCheckMessageID_generatedID_isNotNull() {
        // ASSERT — a new Message must always have a non-null ID
        assertNotNull("Message ID should not be null", message1.getMessageId());
    }
 
    @Test
    public void testCheckMessageID_generatedID_isExactly10Chars() {
        // ASSERT — checkMessageID() returns true because ID is 10 chars or fewer
        assertTrue("Message ID must be 10 characters or fewer", message1.checkMessageID());
    }

    private void assertTrue(String message_ID_must_be_10_characters_or_fewer, boolean checkMessageID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 
    // =========================================================================
    // SENT MESSAGE TESTS
    // Uses TestableMessage inner class — no console/Scanner needed
    // =========================================================================
 
    /**
     * Inner helper class that overrides sentMessage() to use a fixed choice
     * instead of reading from the console.
     * This is the pattern recommended in the Part 2 Unit Tests Guide.
     */
    private static class TestableMessage extends Message {
        private final int simulatedChoice;
 
        public TestableMessage(int messageNumber, int simulatedChoice) {
            super(messageNumber);
            this.simulatedChoice = simulatedChoice;
        }
 
        @Override
        public String sentMessage(int choice) {
            return super.sentMessage(simulatedChoice);
        }
    }
 
    @Test
    public void testSentMessage_userSelectsSend_returnsCorrectString() {
        // ARRANGE — simulate user picking option 1 (Send)
        TestableMessage testMsg = new TestableMessage(1, 1);
        // ACT
        String result = testMsg.sentMessage(1);
        // ASSERT
        assertEquals("Message successfully sent.", result);
    }
 
    @Test
    public void testSentMessage_userSelectsDisregard_returnsCorrectString() {
        // ARRANGE — simulate user picking option 2 (Disregard)
        TestableMessage testMsg = new TestableMessage(1, 2);
        // ACT
        String result = testMsg.sentMessage(2);
        // ASSERT
        assertEquals("Press 0 to delete the message.", result);
    }
 
    @Test
    public void testSentMessage_userSelectsStore_returnsCorrectString() {
        // ARRANGE — simulate user picking option 3 (Store)
        TestableMessage testMsg = new TestableMessage(1, 3);
        // ACT
        String result = testMsg.sentMessage(3);
        // ASSERT
        assertEquals("Message successfully stored.", result);
    }
}