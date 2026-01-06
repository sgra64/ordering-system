package datamodel;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Sanity tests define restrictions that must be implemented by the tested
 * class {@link Customer}.
 * 
 * If a sanity test fails, the violated restriction must be understood first
 * in the test code. Then, the restriction must be implemented in the tested
 * class {@link Customer}.
 */
public class Customer_1_SanityTests {

    /*
     * Test object.
     */
    private final Customer eric = new Customer();


    /**
     * Tests 100-102 restrict {@code setId(id)} to only set {@code id > 0}.
     */
    @Test @Order(100)
    public void test_100_positive_id() {
        long expected = 100L;       // positive id allowed
        eric.setId(expected);
        assertEquals(expected, eric.getId());
    }

    @Test @Order(101)
    public void test_101_negative_id() {
        long expected = -100L;      // negative id does not change the id
        eric.setId(expected);
        assertEquals(-1L, eric.getId());    // id remains unchanged (-1L)
    }

    @Test @Order(102)
    public void test_102_zero_id() {
        long expected = 0L;         // zero id does not change the id
        eric.setId(expected);
        assertEquals(-1L, eric.getId());    // id remains unchanged (-1L)
    }

    /**
     * Test 110 restricts {@code setId(id)} to only set {@code id}
     * once with a positive value ({@code id > 0}).
     */
    @Test @Order(110)
    public void test_110_set_id_only_once() {
        assertEquals(-1L, eric.getId());    // validate initial value
        // 
        long expected = 100L;
        eric.setId(expected);                   // set positive id once
        assertEquals(expected, eric.getId());   // validate id
        // 
        eric.setId(111L);                   // second attempt to set id
        assertEquals(expected, eric.getId());   // id is unchanged
    }

    /**
     * Test 120 restricts the use of empty ("") 'name' and 'firstNames'.
     */
    @Test @Order(120)
    public void test_120_empty_names() {
        eric.setName("Meyer");
        eric.setFirstNames("Eric"); // regular names used
        assertEquals("Eric", eric.getFirstNames());
        assertEquals("Meyer", eric.getName());
        // 
        eric.setFirstNames("");     // first names allowed to be empty
        assertEquals("", eric.getFirstNames());
        // 
        eric.setName("");           // name attribute must not be empty
        assertEquals("Meyer", eric.getName());    // name unchanged
    }

    /**
     * Tests 130-131 restrict the use of empty or null 'name' and 'firstNames'
     * arguments.
     */
    @Test @Order(130)
    public void test_130_null_names() {
        eric.setName("Meyer");
        eric.setFirstNames("Eric"); // regular names used
        assertEquals("Eric", eric.getFirstNames());
        assertEquals("Meyer", eric.getName());
        // 
        // validate IllegalArgumentException is thrown with null argument
        var thrown = assertThrows(
           IllegalArgumentException.class, () -> eric.setName(null)
        );
        // validate exception message
        assertEquals("name argument is null", thrown.getMessage());
    }

    @Test @Order(131)
    public void test_131_null_firstNames() {
        eric.setName("Meyer");
        eric.setFirstNames("Eric"); // regular names used
        assertEquals("Eric", eric.getFirstNames());
        assertEquals("Meyer", eric.getName());
        // 
        // validate IllegalArgumentException is thrown with null argument
        var thrown = assertThrows(
           IllegalArgumentException.class, () -> eric.setFirstNames(null)
        );
        // validate exception message
        assertEquals("firstName argument is null", thrown.getMessage());
    }

    /**
     * Tests 140-141 restrict the use of empty or null 'contact' arguments.
     */
    @Test @Order(140)
    public void test_140_empty_contacts() {
        assertEquals("", eric.getContacts());   // validate initial value
        // 
        eric.addContact("eme@gmail.com");       // add regular contact
        assertEquals("eme@gmail.com", eric.getContacts());  // validate contact
        // 
        eric.addContact("");                    // add empty contact
        assertEquals("eme@gmail.com", eric.getContacts());  // contact unchanged
    }

    @Test @Order(141)
    public void test_141_null_contacts() {
        assertEquals("", eric.getContacts());   // validate initial value
        // 
        eric.addContact("eme@gmail.com");       // add regular contact
        assertEquals("eme@gmail.com", eric.getContacts());  // validate contact
        // 
        // validate IllegalArgumentException is thrown with null argument
        var thrown = assertThrows(
           IllegalArgumentException.class, () -> eric.addContact(null)
        );
        // validate exception message
        assertEquals("contact argument is null", thrown.getMessage());
    }
}
