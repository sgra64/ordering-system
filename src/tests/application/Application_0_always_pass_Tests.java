package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Class with always-passing test methods to test the JUnit test-runner
 * setup. Tests should run and pass in the IDE and by the test-runner
 * used in the terminal.
 * 
 * Public test methods avoid {@code InaccessibleObjectException}, see:
 * {@code stackoverflow + inaccessibleobjectexception-when-running-tests}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Application_0_always_pass_Tests {

    /**
     * First @Test method (always passes).
     */
    @Test
    @Order(001)
    public void test_001_always_pass() {
        int expected = 10;
        int actual = 10;
        assertEquals(expected, actual);
    }

    /**
     * Second @Test method (always passes).
     */
    @Test
    @Order(002)
    public void test_002_always_pass() {
        int expected = 10;
        int actual = 10;
        assertEquals(expected, actual);
    }
}
