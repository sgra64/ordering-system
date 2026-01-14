package datamodel;

import java.util.Optional;

import components.Accessor;
import datamodel.DataModel.Article;
import datamodel.DataModel.Customer;
import datamodel.DataModel.Order;

/**
 * Factory class that implements factory methods of the {@link DataModel} interface.
 */
class DataFactory {

    /*
     * Counter for internally created customer {@code id}
     */
    private long customerIdCounter = 1000L;

    /**
     * Singleton {@link DataFactory} instance.
     */
    private static DataFactory instance = new DataFactory();

    /**
     * Private constructor as part of the Singleton pattern.
     */
    private DataFactory() { }

    /**
     * Static accessor method to Singleton {@link DataFactory} instance
     * @return
     */
    static DataFactory getInstance() { return instance; }

    /**
     * Factory method to create objects of type {@link Customer}.
     * Method internally assigns unique id.
     * @param singleStringName {@link Customer} name in single-String form, e.g. {@code "Eric Meyer"}
     * @return new object of type {@link Customer} or empty Optional if object could not be created from arguments
     */
    Optional<Customer> createCustomer(String singleStringName) {
        // 
        return Accessor.getInstance().getNameSplitter()
            .split(singleStringName)
            .map(splitName -> {
                Customer customer = new datamodel.Customer(customerIdCounter++, splitName.name(), splitName.firstNames());
                return Optional.of(customer);
            }).orElse(Optional.empty());
    }

    /**
     * Factory method to create objects of type {@link Article}.
     * @param id externally provided id, e.g. by data import
     * @param description article description
     * @param unitPrice price of one unit of article (in EURO cent)
     * @param reduced_VAT optional argument for articles with reduced VAT
     * @return new object of type {@link Article} or empty Optional if object could not be created from arguments
     */
    Optional<Article> createArticle(String id, String description, long unit_price, Boolean... reduced_VAT) {
        return Optional.of(
            new datamodel.Article(id, description, unit_price, reduced_VAT.length > 0? reduced_VAT[0].booleanValue() : false)
        );
    }

    /**
     * Factory method to create objects of type {@link Order}.
     * @param id externally provided id, e.g. by data import
     * @param customer reference to {@link Customer} owning the order
     * @return new object of type {@link Order} or empty Optional if object could not be created from arguments
     */
    Optional<Order> createOrder(long id, Optional<Customer> customer) {
        Order order = null;
        if(id > 0L && customer != null && customer.isPresent()) {
            order = new datamodel.Order(id, customer.get());
        }
        return Optional.ofNullable(order);
    }
}
