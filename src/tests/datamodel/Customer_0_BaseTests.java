package datamodel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;

/**
 * Basic tests for class {@link Customer}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Customer_0_BaseTests {

    /*
     * Test object.
     */
    private final Customer eric = new Customer();


    /**
     * Test 010 validates the {@link Customer} object initialization.
     */
    @Test @Order(010)
    public void test_010_initialization() {
        long expectedId = -1L;
        long actualId = eric.getId();
        assertEquals(expectedId, actualId);
        // 
        // test remaining attributes for initial values
        assertEquals("", eric.getName());
        assertEquals("", eric.getFirstNames());
        assertEquals("", eric.getContacts());
    }

    /**
     * Test 020 validates the {@code id} getter and setter.
     */
    @Test @Order(020)
    public void test_020_id_getter_setter() {
        long expectedId = 100L;
        eric.setId(expectedId);
        long actualId = eric.getId();
        assertEquals(expectedId, actualId);
    }

    /**
     * Test 030 validates the {@code name} getter and setter.
     */
    @Test @Order(030)
    public void test_030_name_getter_setter() {
        String expected = "Eric";
        eric.setName(expected);
        String actual = eric.getName();
        assertEquals(expected, actual);
    }

    /**
     * Test 040 validates the {@code firstNames} getter and setter.
     */
    @Test @Order(040)
    public void test_040_firstNames_getter_setter() {
        String expected = "Eric";
        eric.setFirstNames(expected);
        String actual = eric.getFirstNames();
        assertEquals(expected, actual);
    }

    /**
     * Test 050 validates the {@code contact} getter and setter.
     */
    @Test @Order(050)
    public void test_050_contacts_getter_setter() {
        String expected = "eme@gmail.com";
        eric.addContact(expected);
        String actual = eric.getContacts();
        assertEquals(expected, actual);
    }

    /**
     * Test 060 validate method chaining.
     */
    @Test @Order(060)
    public void test_060_chainable_methods() {
        assertEquals(eric, eric.setId(100L));
        assertEquals(eric, eric.name("Eric Meyer"));
        assertEquals(eric, eric.setName("Meyer"));
        assertEquals(eric, eric.setFirstNames("Eric"));
    }

    @Test @Order(061)
    public void test_061_chainable_methods_contacts() {
        assertEquals(eric, eric.setContacts("eme@gmail.com"));
        assertEquals(eric, eric.addContact("eme@gmail.com"));
        assertEquals(eric, eric.removeContact(1));
    }
}
