package runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import application.ApplicationContext;
import application.Runnable;
import datamodel.Article;
import datamodel.Calculator;
import datamodel.Currency;
import datamodel.Customer;
import datamodel.Order;

/**
 * The driver creates some {@link Customer}, {@link Article} and {@link Order}
 * objects and prints tables:
 * <pre>
 * (6) Customer objects built.
 * (9) Article objects built.
 * (7) Order objects built.
 * ---
 * Customers:
 * +------+----------------------+----------------------+------------------------+
 * |   ID | NAME                 | FIRSTNAMES           | CONTACTS               |
 * +------+----------------------+----------------------+------------------------+
 * |  100 | Meyer                | Eric                 | eme@gmail.com          |
 * |      |                      |                      | +49 030 515 141345     |
 * |      |                      |                      | fax: 030 234-134651    |
 * +------+----------------------+----------------------+------------------------+
 * |  101 | Bayer                | Anne                 | anne24@yahoo.de        |
 * |      |                      |                      | (030) 3481-23352       |
 * +------+----------------------+----------------------+------------------------+
 * |  102 | Schulz-Mueller       | Tim                  | tim2346@gmx.de         |
 * |  103 | Blumenfeld           | Nadine-Ulla          | +49 152-92454          |
 * |  104 | Abdelalim            | Khaled Saad Mohamed  | +49 1524-12948210      |
 * |  105 | Neumann              | Lena                 | lena228@gmail.com      |
 * +------+----------------------+----------------------+------------------------+
 * 
 * Articles:
 * +------------+--------------------+-------+----------+------------------------+
 * | ID         | DESCRIPTION        |  VAT %|      VAT |    (Germany) PRICE EUR |
 * +------------+--------------------+-------+----------+------------------------+
 * | SKU-458362 | Tasse              |  19.0 |     0.48 |               2.99 EUR |
 * | SKU-693856 | Becher             |  19.0 |     0.24 |               1.49 EUR |
 * | SKU-518957 | Kanne              |  19.0 |     3.19 |              19.99 EUR |
 * | SKU-278530 | Buch "Java"        |   7.0*|     3.26 |              49.90 EUR |
 * | SKU-425378 | Buch "OOP"         |   7.0*|     5.23 |              79.95 EUR |
 * | SKU-300926 | Pfanne             |  19.0 |     7.98 |              49.99 EUR |
 * | SKU-663942 | Fahrradhelm        |  19.0 |    26.98 |             169.00 EUR |
 * | SKU-583978 | Fahrradkarte       |   7.0*|     0.45 |               6.95 EUR |
 * | SKU-588268 | Radio              |  19.0 |    15.97 |             100.00 EUR |
 * +------------+--------------------+-------+----------+------------------------+
 * 
 * Orders:
 * +------------+----------------------------+----------+------------------------+
 * | ORDER-ID   | CUSTOMER                   |  CUST-ID |                  ITEMS |
 * +------------+----------------------------+----------+------------------------+
 * | 8592356245 | Meyer, Eric                |      100 |                      4 |
 * | 3563561357 | Bayer, Anne                |      101 |                      2 |
 * | 5234968294 | Meyer, Eric                |      100 |                      1 |
 * | 6135735635 | Blumenfeld, Nadine-Ulla    |      103 |                      3 |
 * | 6173043537 | Neumann, Lena              |      105 |                      2 |
 * | 7372561535 | Meyer, Eric                |      100 |                      2 |
 * | 4450305661 | Meyer, Eric                |      100 |                      3 |
 * +------------+----------------------------+----------+------------------------+
 * </pre>
 */
@Runnable.Accessors(priority=3)
public class RunDatamodelDemo implements Runnable {

    /**
     * Component that performs price and VAT taxt calulations.
     */
    private final Calculator calcuator = Calculator.create();

    /**
     * Method to run the application.
     * @param context {@link ApplicationContext} instance
     */
    @Override
    public void run(ApplicationContext context) {

        /*
         * Customers:
         */
        final Customer eric = new Customer("Eric Meyer")
            .setId(100L)
            .addContact("eme@gmail.com")
            .addContact("+49 030 515 141345")
            .addContact("fax: 030 234-134651")  // duplicate entry
            .addContact("fax: 030 234-134651");

        final Customer anne = new Customer("Bayer, Anne")
            .setId(101L)
            .addContact("anne24@yahoo.de")
            .addContact("(030) 3481-23352");

        final Customer tim = new Customer("Tim Schulz-Mueller")
            .setId(102L)
            .addContact("tim2346@gmx.de");

        final Customer nadine = new Customer("Nadine-Ulla Blumenfeld")
            .setId(103L)
            .addContact("+49 152-92454");

        final Customer khaled = new Customer("Khaled Saad Mohamed Abdelalim")
            .setId(104L)
            .addContact("+49 1524-12948210");

        final Customer lena = new Customer("Lena Neumann")
            .setId(105L)
            .addContact("lena228@gmail.com");

        final List<Customer> customers = new ArrayList<>(List.of(
            eric, anne, tim, nadine, khaled, lena
        ));

        /*
         * Articles:
         */
        var tasse = new Article("SKU-458362", "Tasse", 299);
        var becher = new Article("SKU-693856", "Becher", 149);
        var kanne = new Article("SKU-518957", "Kanne", 1999);
        var teller = new Article("SKU-638035", "Teller", 649);
        var buch_Java = new Article("SKU-278530", "Buch \"Java\"", 4990, true);
        var buch_OOP = new Article("SKU-425378", "Buch \"OOP\"", 7995, true);
        var pfanne = new Article("SKU-300926", "Pfanne", 4999);
        var fahrradhelm = new Article("SKU-663942", "Fahrradhelm", 16900);
        var fahrradkarte = new Article("SKU-583978", "Fahrradkarte", 695, true);
        var radio = new Article("SKU-588268", "Radio", 10000);

        final List<Article> articles = new ArrayList<>(List.of(
            tasse, becher, kanne, buch_Java, buch_OOP, pfanne, fahrradhelm, fahrradkarte, radio
        ));

        /*
         * Orders:
         */
        List<Order> orders = new ArrayList<>(List.of(
            // 
            // Eric's 1st order
            new Order(8592356245L, eric)
                .addItem(teller, 4)     // + item: 4 Teller, 4x 6.49 € = 25.96 €, 19% MwSt (4.14€)
                .addItem(becher, 8)     // + item: 8 Becher, 8x 1.49 € = 11.92 €, 19% MwSt (1.90€)
                .addItem(buch_OOP, 1)   // + item: 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
                .addItem(tasse, 4),     // + item: 4 Tassen, 4x 2.99 € = 11.96 €, 19% MwSt (1.91€)
            // 
            // Anne's order
            new Order(3563561357L, anne)
                .addItem(teller, 2)
                .addItem(tasse, 2),
            // 
            // Eric's 2nd order
            new Order(5234968294L, eric)
                .addItem(kanne, 1),
            // 
            // Nadine's order
            new Order(6135735635L, nadine)
                .addItem(teller, 12)
                .addItem(buch_Java, 1)
                .addItem(buch_OOP, 1),
            // 
            // Lena's order
            new Order(6173043537L, lena)
                .addItem(buch_Java, 1)
                .addItem(fahrradkarte, 1),
            // 
            // Eric's 3rd order
            new Order(7372561535L, eric)
                .addItem(fahrradhelm, 1)
                .addItem(fahrradkarte, 1),
            // 
            // Eric's 4th order
            new Order(4450305661L, eric)
                .addItem(tasse, 3)
                .addItem(becher, 3)
                .addItem(kanne, 1)
        ));

        System.out.println(String.format(   // print numbers of objects in collections
            "(%d) Customer objects built.\n" +
            "(%d) Article objects built.\n" +
            "(%d) Order objects built.\n---",
            customers.size(), articles.size(), orders.size()));
        // 
        // print {@link Customer} table
        printCustomerTable(customers);
        // 
        // print {@link Article} table
        printArticleTable(articles);
        // 
        // print {@link Order} table
        printOrderTable(orders);
    }


    /**
     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * 
     * TableFormatter used to format a table of {@link Customer} objects.
     * <pre>
     * Customers:
     * +------+----------------------+----------------------+------------------------+
     * |   ID | NAME                 | FIRSTNAMES           | CONTACTS               |
     * +------+----------------------+----------------------+------------------------+
     * |  100 | Meyer                | Eric                 | eme@gmail.com          |
     * |  101 | Bayer                | Anne                 | anne24@yahoo.de        |
     * |  102 | Schulz-Mueller       | Tim                  | tim2346@gmx.de         |
     * |  103 | Blumenfeld           | Nadine-Ulla          | +49 152-92454          |
     * |  104 | Abdelalim            | Khaled Saad Mohamed  | +49 1524-12948210      |
     * |  105 | Neumann              | Lena                 | lena228@gmail.com      |
     * +------+----------------------+----------------------+------------------------+
     * </pre>
     */
    private final TableFormatter customerTableFormatter =
        TableFormatter.builder()
            // 
            .columns("| ID | NAME | FIRSTNAMES | CONTACTS |")
            .widths(6, 22, 22, 24)
            .alignments("R")
            // 
            // .rowMapper(Customer.class, customerRowMapper())
            .multiRowMapper(Customer.class, customerMultiRowMapper())
            // 
            .build();

    /**
     * Row mapper maps a {@link Customer} object to {@code String[]} fields
     * that corresond to consecutive fields in one table row.
     */
    final Function<Customer, String[]> customerRowMapper() {
        return customer -> {
            var id = Long.toString(customer.getId());
            var name = customer.getName().length()==0? " " : customer.getName();
            var firstNames = customer.getFirstNames().length()==0? " " : customer.getFirstNames();
            var contact = customer.contact(0).length()==0? " " : customer.contact(0);
            return new String[] {id, name, firstNames, contact};
        };
    }

    /**
     * Multi-row mapper maps a {@link Customer} object to multiple table rows,
     * e.g. to include multiple contacts.
     * <pre>
     * +------+----------------------+----------------------+------------------------+
     * |  100 | Meyer                | Eric                 | eme@gmail.com          |
     * |      |                      |                      | +49 030 515 141345     |
     * |      |                      |                      | fax: 030 234-134651    |
     * +------+----------------------+----------------------+------------------------+
     * </pre>
     */
    final Function<Customer, String[][]> customerMultiRowMapper() {
        return customer -> {
            String[][] rows = null;
            if(customer != null) {
                var id = Long.toString(customer.getId());
                var name = customer.getName().length()==0? " " : customer.getName();
                var firstNames = customer.getFirstNames().length()==0? " " : customer.getFirstNames();
                List<String> contacts = new ArrayList<>();
                customer.getContactsAsIterable().forEach(contacts::add);
                int sepLine = contacts.size() > 1? 1 : 0;   // add seperator line for multi-contact rows
                int rowsLen = Math.max(1, contacts.size()) + sepLine;
                rows = new String[rowsLen][];
                for(int i=0; i < rows.length; i++) {
                    var contact = i < contacts.size()? contacts.get(i) : "---";
                    rows[i] = i==0? new String[] {id, name, firstNames, contact} :
                            i==rows.length-1 && sepLine==1? new String[] {"{---}", "{---}", "{---}", "{---}"} :
                            new String[] {" ", " ", " ", contact};
                }
            }
            return rows;
        };
    }

    /**
     * Print {@link Customer} table.
     * @param customers each {@link Customer} is printed as row in the table
     */
    private void printCustomerTable(List<Customer> customers) {
        // 
        customerTableFormatter
            .text("Customers:")
            .header()
                // .row(eric)
                // .row(anne)
                // .row(tim)
                // .row(nadine)
                // .row(khaled)
                .row(customers)
            .footer()
            .print(System.out);
    }


    /**
     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * 
     * TableFormatter used to format a table of {@link Article} objects.
     * Letter 'R' indicates a reduced VAT tax rate applies.
     * <pre>
     * Articles:
     * +------------+--------------------+-------+----------+------------------------+
     * | ID         | DESCRIPTION        |  VAT %|      VAT |    (Germany) PRICE EUR |
     * +------------+--------------------+-------+----------+------------------------+
     * | SKU-458362 | Tasse              |  19.0 |     0.48 |               2.99 EUR |
     * | SKU-693856 | Becher             |  19.0 |     0.24 |               1.49 EUR |
     * | SKU-518957 | Kanne              |  19.0 |     3.19 |              19.99 EUR |
     * | SKU-278530 | Buch "Java"        |   7.0*|     3.26 |              49.90 EUR |
     * | SKU-425378 | Buch "OOP"         |   7.0*|     5.23 |              79.95 EUR |
     * | SKU-300926 | Pfanne             |  19.0 |     7.98 |              49.99 EUR |
     * | SKU-663942 | Fahrradhelm        |  19.0 |    26.98 |             169.00 EUR |
     * | SKU-583978 | Fahrradkarte       |   7.0*|     0.45 |               6.95 EUR |
     * | SKU-588268 | Radio              |  19.0 |    15.97 |             100.00 EUR |
     * +------------+--------------------+-------+----------+------------------------+
     * </pre>
     */
    private final TableFormatter articleTableFormatter =
        TableFormatter.builder()
            // 
            .columns("| ID | DESCRIPTION | VAT %| VAT | PRICE |")
            .widths(12, 20, 7, 10, 24)
            .alignments("LLRRR")
            // 
            .rowMapper(Article.class, articleRowMapper())
            // 
            .build();

    /**
     * Row mapper maps a {@link Article} object to {@code String[]} fields
     * that corresond to consecutive fields in one table row.
     */
    private final Function<Article, String[]> articleRowMapper() {
        return article -> {
            var id = article.id();
            var description = article.description();
            long unit_price = article.unit_price();
            var unit_price_fmt = fmtPrice(unit_price, Currency.EUR, 1);
            // 
            var reduced = article.reduced_VAT();
            double rate = reduced? 7.0 : 19.0;  // 19% VAT (MwSt), 7.0% reduced VAT
            var marker = reduced? "*" : " ";
            var rate_fmt = String.format("%.1f%s", rate, marker);
            var incl_vat = calcuator.includedVAT(unit_price, rate/100.0);
            var incl_vat_fmt = fmtPrice(incl_vat, Currency.EUR, 0);
            // 
            return new String[] {id, description, rate_fmt, incl_vat_fmt, unit_price_fmt};
        };
    }

    /**
     * Print {@link Article} table.
     * @param customers each {@link Article} is printed as row in the table
     */
    private void printArticleTable(List<Article> articles) {
        // 
        var atf = articleTableFormatter
            .text("Articles:")
            .header("{label}", "{label}", "{label}", "{label}", "(Germany) PRICE EUR");
        // 
        articles.forEach(article -> {
            atf.row(article);
        });
        // 
        atf.footer()
            .print(System.out);
    }

    /**
     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * 
     * TableFormatter used to format a table of {@link Order} objects.
     * <pre>
     * Orders:
     * +------------+----------------------------+----------+------------------------+
     * | ORDER-ID   | CUSTOMER                   |  CUST-ID |                  ITEMS |
     * +------------+----------------------------+----------+------------------------+
     * | 8592356245 | Meyer, Eric                |      100 |                      4 |
     * | 3563561357 | Bayer, Anne                |      101 |                      2 |
     * | 5234968294 | Meyer, Eric                |      100 |                      1 |
     * | 6135735635 | Blumenfeld, Nadine-Ulla    |      103 |                      3 |
     * | 6173043537 | Neumann, Lena              |      105 |                      2 |
     * | 7372561535 | Meyer, Eric                |      100 |                      2 |
     * | 4450305661 | Meyer, Eric                |      100 |                      3 |
     * +------------+----------------------------+----------+------------------------+
     * </pre>
     * 
     * TableFormatter used to format a table of {@link Order} objects.
     */
    private final TableFormatter orderTableFormatter =
        TableFormatter.builder()
            // 
            .columns("| ORDER-ID | CUSTOMER | CUST-ID | ITEMS |")
            .widths(12, 28, 10, 24)
            .alignments("LLRR")
            // 
            .rowMapper(Order.class, orderRowMapper())
            // 
            .build();

    /**
     * Multi-row mapper maps an {@link Order} object to multiple table rows.
     */
    private final Function<Order, String[]> orderRowMapper() {
        return order -> {
            var id = Long.toString(order.id());
            var customerName = String.format("%s, %s", order.customer().getName(), order.customer().getFirstNames());
            var customerId = Long.toString(order.customer().getId());
            var items = Integer.toString(order.items().size());
            return new String[] {id, customerName, customerId, items};
        };
    }

    /**
     * Print {@link Order} table.
     * @param customers each {@link Article} is printed as row in the table
     */
    private void printOrderTable(List<Order> orders) {
        // 
        var otf = orderTableFormatter
            .text("Orders:")
            .header();
        // 
        orders.forEach(order -> {
            otf.row(order);
        });
        // 
        otf.line().print(System.out);
    }


    /**
     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * 
     */

    /**
     * Format long value to price according to a format (0 is default):
     * <pre>
     * Example: long value: 499
     * Style: 0: "4.99"
     *        1: "4.99 EUR"     3: "4.99 €"
     *        2: "4.99EUR"      4: "4.99€"
     * </pre>
     * @param price long value as price
     * @param currency {@link Currency} to obtain currency three-letter code or Unicode
     * @param style price formatting style
     * @return price formatted according to selcted style
     */
    private String fmtPrice(long price, Currency currency, int... style) {
        final var cur = currency==null? Currency.EUR : currency;
        final int ft = style.length > 0? style[0] : 0;	// 0 is default format
        switch(ft) {
        case 0: return fmtDecimal(price, 2);
        case 1: return fmtDecimal(price, 2, " " + cur.code());
        case 2: return fmtDecimal(price, 2, cur.code());
        case 3: return fmtDecimal(price, 2, " " + cur.marking());
        case 4: return fmtDecimal(price, 2, cur.marking());
        default: return fmtPrice(price, cur, 0);
        }
    }

    /**
     * Format long value to a decimal String with specified digit formatting:
     * <pre>
     *      {      "%,d", 1L },     // no decimal digits:  16,000Y
     *      { "%,d.%01d", 10L },
     *      { "%,d.%02d", 100L },   // double-digit price: 169.99E
     *      { "%,d.%03d", 1000L },  // triple-digit unit:  16.999-
     * </pre>
     * @param value value to format to String in decimal format
     * @param decimalDigits number of digits
     * @param unit appended unit as String
     * @return decimal value formatted according to specified digit formatting
     */
    private String fmtDecimal(long value, int decimalDigits, String... unit) {
        final String unitStr = unit.length > 0? unit[0] : null;
        final Object[][] dec = {
            {      "%,d", 1L },     // no decimal digits:  16,000Y
            { "%,d.%01d", 10L },
            { "%,d.%02d", 100L },   // double-digit price: 169.99E
            { "%,d.%03d", 1000L },  // triple-digit unit:  16.999-
        };
        String result;
        String fmt = (String)dec[decimalDigits][0];
        if(unitStr != null && unitStr.length() > 0) {
            fmt += "%s";	// add "%s" to format for unit string
        }
        int decdigs = Math.max(0, Math.min(dec.length - 1, decimalDigits));
        //
        if(decdigs==0) {
            Object[] args = {value, unitStr};
            result = String.format(fmt, args);
        } else {
            long digs = (long)dec[decdigs][1];
            long frac = Math.abs( value % digs );
            Object[] args = {value/digs, frac, unitStr};
            result = String.format(fmt, args);
        }
        return result;
    }
}
