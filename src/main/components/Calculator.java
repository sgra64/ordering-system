package components;

import java.util.Collection;

import datamodel.DataModel.Order;
import datamodel.DataModel.OrderItem;


/**
 * {@link Calculator} performs price and tax calculations.
 */
public interface Calculator {

    /**
     * Compound the value of {@code orders} from a collection.
     * @param orders collection of {@code orders}
     * @return compound value of {@code orders} or {@code 0L} if no order is present
     */
    long valueOrders(Collection<Order> orders);

    /**
     * Compound the {@link VAT} of {@code orders} from a collection.
     * @param orders collection of {@code orders}
     * @return Compound VAT of {@code orders} or {@code 0L} if no order is present
     */
    long vatOrders(Collection<Order> orders);

    /**
     * Calculate the value of an {@link Order} as compound value of all
     * its {@link Order.OrderItem} entries.
     * @param order subject of calculation
     * @return value of {@link Order} or {@code 0L} if no order or {@code OrderItem} is present
     */
    long valueOrder(Order order);

    /**
     * Calculate the {@link VAT} of an {@link Order} as compound
     * {@link VAT} for all {@link Order.OrderItem} entries.
     * @param order subject of calculation
     * @return {@link VAT} of {@link Order} or {@code 0L} if no order or {@code OrderItem} is present
     */
    long vatOrder(Order order);

    /**
     * Calculate the value of an {@link Order.OrderItem}.
     * @param item subject of calculation
     * @return value of the {@link Order.OrderItem} or {@code 0L} if no {@code OrderItem} is present
     */
    long valueOrderItem(OrderItem item);

    /**
     * Calculate the {@link VAT} of an {@link Order.OrderItem}.
     * @param item subject of calculation
     * @return {@link VAT} of the {@link Order.OrderItem} or {@code 0L} if no {@code OrderItem} is present
     */
    long vatOrderItem(OrderItem item);

    /**
     * Calculate the {@link VAT} included in a gross value.
     * @param grossValue value with included {@link VAT}
     * @param rate {@link VAT} tax rate to apply
     * @return {@link VAT} included in a gross value
     */
    long includedVAT(long grossValue, double rate);
}
