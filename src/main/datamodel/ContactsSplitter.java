package datamodel;

/**
 * Interface {@link ContactsSplitter} that manages multiple {@link Customer}
 * contacts rendered as JSON-array stored as String value.
 * <p>
 * A single contact is stored as String value (not as JSON). If more contacts
 * are added, representation switches to JSON-array stored as String value.
 * <p>
 * Example:
 * <pre>
 * [ "eme@gmail.com", "+49 030 515 141345", "fax: 030 234-134651" ]
 * </pre>
 */
public interface ContactsSplitter {

    /**
     * Return {@code i-th} contact of a {@link Customer} object or empty
     * String {@code ""} if contact does not exist.
     * @param customer {@link Customer} object to return contact
     * @param i contact to return
     * @return {@code i-th} contact or empty String {@code ""}
     */
    public String contact(Customer customer, int i);

    /**
     * Add contact to {@link Customer} object. A single contact is stored
     * as a String value (no JSON). If contacts are added, representation
     * switches to JSON-String.
     * @param customer {@link Customer} object to add contact
     * @param contact contact to add
     * @return chainable {@link Customer} reference
     */
    public Customer addContact(Customer customer, String contact);

    /**
     * Remove the {@code i-th} contact from contacts.
     * @param customer {@link Customer} object to remove contact from
     * @param i {@code i-th} contact to remove
     * @return chainable {@link Customer} reference
     */
    public Customer removeContact(Customer customer, int i);

    /**
     * Return {@link Customer} contacts as immutable collection.
     * @param customer {@link Customer} object of which contacts are returned
     * @return {@link Customer} contacts as immutable collection
     */
    public Iterable<String> contactsAsIterable(Customer customer);

    /**
     * Static getter of <i>Singleton</i> instance of implementation class.
     * @return reference to <i>Singleton</i> instance of implementation class
     */
    public static ContactsSplitter getInstance() {
        return ContactsSplitterImpl.getInstance();
    }
}
