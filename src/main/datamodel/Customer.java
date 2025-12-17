package datamodel;


/**
 * Class of the {@link Customer} entity.
 */
public class Customer {

    /**
     * Unique identifier.
     */
    private long id = -1L;

    /**
     * Customer last name.
     */
    private String name = "";

    /**
     * Customer first names.
     */
    private String firstNames = "";

    /**
     * Customer contacts rendered as String or JSON-Array in case of multiple contacts.
     */
    private String contacts = "";


    /**
     * Default constructor.
     */
    public Customer() { }

    /**
     * {@code singleStringName}-arg constructor.
     * @param singleStringName single-String {@code name} attribute, e.g. {@code "Eric Meyer"}
     */
    public Customer(String singleStringName) {
        name(singleStringName);
    }

    /**
     * {@link id} attribute getter.
     * @return {@link id} attribute
     */
    public long getId() {
        return id;
    }

    /**
     * {@link id} attribute setter. The {@link id} attribute can only be set
     * once with a value {@code >= 0}.
     * @param id {@link id} attribute to set
     * @return chainable self-reference
     */
    public Customer setId(long id) {
        if(this.id < 0 && id > 0L) this.id = id;
        return this;
    }

    /**
     * Last {@link name} attribute getter.
     * @return last {@link name} attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Last {@link name} attribute setter. Name may include a single-String
     * name that codifies last and first {@link name} attributes, e.g.
     * {@code "Eric Meyer"} or {@code "Meyer, Eric"}.
     * @param name {@link name} attribute to set
     * @return chainable self-reference
     */
    public Customer setName(String name) {
        // this.name = name;
        if(name==null) throw new IllegalArgumentException("name argument is null");
        // 
        NameSplitter.getInstance()
            .split(name).ifPresent(spl -> {
                this.name = spl.name();
                if(spl.firstNames().length() > 0) {
                    this.firstNames = spl.firstNames();
                }
            });
        // 
        return this;
    }

    /**
     * Single-string setter that splits a name given as a single-string,
     * e.g. {@code "Eric Meyer"}, into last- and first name attributes,
     * see {@link NameSplitter}.
     * @param singleStringName single-String {@code name} attribute
     * @return chainable self-reference
     */
    public Customer name(String singleStringName) {
        NameSplitter.getInstance().split(singleStringName).ifPresent(ns -> {
            this.name = ns.name();
            this.firstNames = ns.firstNames();
        });
        return this;
    }

    /**
     * {@link firstNames} attribute getter.
     * @return {@link firstNames} attribute
     */
    public String getFirstNames() {
        return firstNames;
    }

    /**
     * {@link firstNames} attribute setter with {@link firstNames} may
     * be empty {@code ""}.
     * @param firstName {@link firstNames} attribute to set
     * @return chainable self-reference
     */
    public Customer setFirstNames(String firstName) {
        if(firstName==null) throw new IllegalArgumentException("firstName argument is null");
        this.firstNames = firstName;
        return this;

    }

    /**
     * Raw contacts getter used by {@link ContactsSplitter}.
     * @return raw contacts String
     */
    String getContacts() {
        return contacts;
    }

    /**
     * Raw contacts setter used by {@link ContactsSplitter}.
     * @param contacts raw contacts String to set
     * @return chainable self-reference
     */
    Customer setContacts(String contacts) {
        if(contacts==null) throw new IllegalArgumentException("contacts argument is null");
        this.contacts = contacts;
        return this;
    }

    /**
     * Add {@code contact} to existing {@link contacts} using {@link ContactsSplitter}.
     * Duplicate contacts are not added. A single contact is stored as String
     * value (not JSON). If contacts are added, representation switches to a
     * JSON-Array.
     * @param contact contact to add.
     * @return chainable self-reference
     */
    public Customer addContact(String contact) {
        if(contact==null) throw new IllegalArgumentException("contact argument is null");
        ContactsSplitter.getInstance().addContact(this, contact);
        // System.out.println(String.format("--> addContact: %s --> contacts: %s", contact, getContacts()));
        return this;
    }

    /**
     * Return {@link contacts} attribute as {@link Iterable}.
     * @return {@link contacts} attribute as {@link Iterable}
     */
    public Iterable<String> getContactsAsIterable() {
        return ContactsSplitter.getInstance().contactsAsIterable(this);
    }

    /**
     * Return {@code i-th} contact or empty String {@code ""} if contact
     * does not exist.
     * @param i contact to return
     * @return {@code i-th} contact or empty String {@code ""}
     */
    public String contact(int i) {
        return ContactsSplitter.getInstance().contact(this, i);
    }

    /**
     * Public method to remove the {@code i-th} contact from contacts.
     * @param i {@code i-th} contact to remove
     * @return chainable {@link Customer} reference
     */
    public Customer removeContact(int i) {
        return ContactsSplitter.getInstance().removeContact(this, i);
    }
}
