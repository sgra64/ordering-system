package datamodel;

import java.util.List;
import java.util.Optional;

/**
 * Public facade of immutable types of package {@link datamodel} with public
 * factory methods for creating objects.
 */
public interface DataModel {

    /**
     * Facade of type {@link Customer}.
     * @TODO: facade is not fully immutable ({@code setContacts()}).
     */
    sealed interface Customer permits datamodel.Customer {
        long id();
        String name();
        String firstNames();
        Customer addContact(String contact);
        String contacts();
        Customer setContacts(String contacts);  /* @TODO: not immutable */
        String contact(int i);
        Iterable<String> contactsAsIterable();
        Customer removeContact(int i);
    }

    /**
     * Facade of type {@link Article}.
     */
    sealed interface Article permits datamodel.Article {
        String id();
        String description();
        long unitPrice();
        boolean reduced_VAT();
    }

    /**
     * Facade of type {@link OrderItem}.
     */
    sealed interface OrderItem permits datamodel.Order.OrderItem {
        Article article();
        int unitsOrdered();
    }

    /**
     * Facade of type {@link Order}.
     */
    sealed interface Order permits datamodel.Order {
        long id();
        Customer customer();
        List<OrderItem> items();
        Order addItem(Optional<Article> article, int unitsOrdered);
    }

    /**
     * Factory method to create objects of type {@link Customer}.
     * Method internally assigns unique id.
     * @param singleStringName {@link Customer} name in single-String form, e.g. {@code "Eric Meyer"}
     * @return new object of type {@link Customer} or empty Optional if object could not be created from arguments
     */
    static Optional<Customer> createCustomer(String singleStringName) {
        return DataFactory.getInstance().createCustomer(singleStringName);
    }

    /**
     * Factory method to create objects of type {@link Article}.
     * @param id externally provided id, e.g. by data import
     * @param description article description
     * @param unitPrice price of one unit of article (in EURO cent)
     * @param reduced_VAT optional argument for articles with reduced VAT
     * @return new object of type {@link Article} or empty Optional if object could not be created from arguments
     */
    static Optional<Article> createArticle(String id, String description, long unitPrice, Boolean... reduced_VAT) {
        return DataFactory.getInstance().createArticle(id, description, unitPrice, reduced_VAT);
    }

    /**
     * Factory method to create objects of type {@link Order}.
     * @param id externally provided id, e.g. by data import
     * @param customer reference to {@link Customer} owning the order
     * @return new object of type {@link Order} or empty Optional if object could not be created from arguments
     */
    static Optional<Order> createOrder(long id, Optional<Customer> customer) {
        return DataFactory.getInstance().createOrder(id, customer);
    }
}
