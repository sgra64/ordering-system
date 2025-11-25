package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Test the JUnit test setup. @Test methods of this class always pass.
 * Tests should run in the IDE and in the terminal.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Application_0_always_pass_Tests {

    /**
     * First @Test method (always passes).
     */
    @Test
    @Order(001)
    void test_001_always_pass() {
        int expected = 10;
        int actual = 10;
        assertEquals(expected, actual);
    }

    /**
     * Second @Test method (always passes).
     */
    @Test
    @Order(002)
    void test_002_always_pass() {
        int expected = 10;
        int actual = 10;
        assertEquals(expected, actual);
    }
}
