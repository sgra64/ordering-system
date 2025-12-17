package datamodel;

import java.util.Iterator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that specify {@link Customer} contacts.
 */
public class Customer_3_ContactsTests {

    /*
     * Test object.
     */
    private final Customer eric = new Customer();


    /**
     * Test 010 validates the {@link Customer} contact initialization.
     */
    @Test @Order(300)
    public void test_300_contact_initialization() {
        assertEquals("", eric.getContacts());
    }

    /**
     * Test 310 validates a single contact.
     */
    @Test @Order(310)
    public void test_310_single_contact() {
        eric.addContact("eme@gmail.com");
        assertEquals("eme@gmail.com", eric.getContacts());
        assertEquals("eme@gmail.com", eric.contact(0));
    }

    /**
     * Test 320 validates a multiple contacts.
     */
    @Test @Order(320)
    public void test_320_multiple_contacts() {
        eric.addContact("eme@gmail.com");
        eric.addContact("+49 030 515 141345");
        eric.addContact("fax: 030 234-134651");
        // 
        assertEquals("eme@gmail.com", eric.contact(0));
        assertEquals("+49 030 515 141345", eric.contact(1));
        assertEquals("fax: 030 234-134651", eric.contact(2));
    }

    @Order(321)
    @Test public void test_321_multiple_contacts_list() {
        eric.addContact("eme@gmail.com");
        eric.addContact("+49 030 515 141345");
        eric.addContact("fax: 030 234-134651");
        // 
        Iterator<String> it = eric.getContactsAsIterable().iterator();
        for(int i=0; it.hasNext(); i++) {
            String contact = it.next();
            switch(i) {
            case 0: assertEquals("eme@gmail.com", contact); break;
            case 1: assertEquals("+49 030 515 141345", contact); break;
            case 2: assertEquals("fax: 030 234-134651", contact); break;
            }
        }
    }

    /**
     * Test 330 contact out-of-bounds cases.
     */
    @Test @Order(330)
    public void test_330_out_of_bounds_contacts() {
        eric.addContact("eme@gmail.com");
        eric.addContact("+49 030 515 141345");
        eric.addContact("fax: 030 234-134651");
        // 
        assertEquals("", eric.contact(-1));
        assertEquals("", eric.contact(3));
    }
}
