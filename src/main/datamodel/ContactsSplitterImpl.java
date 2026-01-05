package datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


class ContactsSplitterImpl implements ContactsSplitter {

    /**
     * {@link ObjectMapper} instance used to parse JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * (1) <i>Singleton:</i> a private static instance variable.
     */
    private static ContactsSplitter instance = new ContactsSplitterImpl();

    /*
     * (2) <i>Singleton:</i> private constructor.
     */
    private ContactsSplitterImpl() { }

    /**
     * (3) <i>Singleton:</i> static instance getter.
     * @return reference to <i>Singleton</i> instance
     */
    public static ContactsSplitter getInstance() {
        return instance;
    }

    /**
     * Return {@code i-th} contact of a {@link Customer} object or empty
     * String {@code ""} if contact does not exist.
     * @param customer {@link Customer} object to return contact
     * @param i contact to return
     * @return {@code i-th} contact or empty String {@code ""}
     */
    public String contact(Customer customer, int i) {
        if(customer != null && i >= 0) {
            var it = contactsAsIterable(customer).iterator();
            for(int j=0; it.hasNext(); j++) {
                String ct2 = it.next();
                if(j==i) {
                    return ct2;
                }
            }
        }
        return "";
    }

    /**
     * Add contact to {@link Customer} object. A single contact is stored
     * as a String value (no JSON). If contacts are added, representation
     * switches to JSON-String.
     * @param customer {@link Customer} object to add contact
     * @param contact contact to add
     * @return chainable {@link Customer} reference
     */
    public Customer addContact(Customer customer, String contact) {
        // 
        if(customer != null && contact != null && contact.length() > 0) {
            contact = trim(contact);
            String contacts = customer.getContacts();
            if(contacts.length()==0) {
                customer.setContacts(contact);  // add single, none-[] contact
            } else {
                boolean isArray = contacts.contains("[");
                if( ! isArray && ! contacts.equals(contact)) {
                    // change single-string contact to array contact
                    contacts = String.format("[\"%s\"]", contacts);
                    isArray = true;
                }
                if(isArray) {
                    try {
                        boolean found = false;
                        JsonNode jn = objectMapper.readTree(contacts);
                        if(jn.isArray()) {
                            ArrayNode ja = (ArrayNode)jn;
                            for(JsonNode jn2 : ja) {
                                // avoid duplicate contacts
                                found = found || jn2.textValue().equals(contact);
                            }
                            if( ! found ) {
                                // add contact to JSON-array (if not found), render to
                                // JSON-String and set to Customer object
                                customer.setContacts(ja.add(contact).toPrettyString());
                            }
                        }
                    } catch (JsonProcessingException e) { }
                }
            }
        }
        return customer;
    }

    /**
     * Remove the {@code i-th} contact from contacts.
     * @param customer {@link Customer} object to remove contact from
     * @param i {@code i-th} contact to remove
     * @return chainable {@link Customer} reference
     */
    public Customer removeContact(Customer customer, int i) {
        if(customer != null && i >= 0) {
            Iterable<String> it = contactsAsIterable(customer);
            List<String> contacts = StreamSupport.stream(it.spliterator(), false).toList();
            int len = contacts.size();
            for(int j=0; i < len && j < len; j++) {
                if(j == 0) customer.setContacts("");    // reset contacts
                if(j != i) addContact(customer, contacts.get(j));   // rebuild, except i-th
            }
        }
        return customer;
    }

    /**
     * Return {@link Customer} contacts as immutable collection.
     * @param customer {@link Customer} object of which contacts are returned
     * @return {@link Customer} contacts as immutable collection
     */
    public Iterable<String> contactsAsIterable(Customer customer) {
        if(customer != null) {
            String contacts = customer.getContacts();
            if(contacts.length() > 0) {
                try {
                    if( ! contacts.contains("[")) {
                        contacts = String.format("[\"%s\"]", contacts);
                    }
                    JsonNode jn = objectMapper.readTree(contacts);
                    if(jn.isArray()) {
                        List<String> contactsList = new ArrayList<>();
                        ArrayNode ja = (ArrayNode)jn;
                        var it = ja.iterator();
                        while(it.hasNext()) {
                            JsonNode jn2 = it.next();
                            contactsList.add(jn2.textValue());
                        }
                        return contactsList;
                    }
                // 
                } catch (JsonProcessingException e) { }
            }
        }
        return List.of();
    }

    /**
     * Trim leading and trailing white spaces {@code [\s]}, commata {@code [,;]}
     * and quotes {@code ["']} from a String (used for names and contacts).
     * @param s String to trim
     * @return trimmed String
     */
    private String trim(String s) {
        s = s.replaceAll("^[\\s\"',;]*", "");   // trim leading white spaces[\s], commata[,;] and quotes['"]
        s = s.replaceAll( "[\\s\"',;]*$", "");  // trim trailing accordingly
        return s;
    }
}
