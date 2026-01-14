package runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import application.ApplicationContext;
import application.Runnable;
import components.Accessor;
import components.Calculator;
import components.TableFormatter;
import datamodel.Currency;
import datamodel.DataModel;
import datamodel.DataModel.Article;
import datamodel.DataModel.Customer;
import datamodel.DataModel.Order;

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
 * Orders (detailed):
 * +-------------------------------+---------+----------+----------+-------------+
 * | ORDER                         |    MwSt*|    Preis |     MwSt |      Gesamt |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:8592356245, CID:100       |         |          |          |             |
 * | Eric Meyer                    |         |          |          |             |
 * | - 4 Teller, 4x 6.49€          |    4.14 |    25.96 |          |             |
 * | - 8 Becher, 8x 1.49€          |    1.90 |    11.92 |          |             |
 * | - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |          |             |
 * | - 4 Tasse, 4x 2.99€           |    1.91 |    11.96 |    13.18 |  129.79 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:3563561357, CID:101       |         |          |          |             |
 * | Anne Bayer                    |         |          |          |             |
 * | - 2 Teller, 2x 6.49€          |    2.07 |    12.98 |          |             |
 * | - 2 Tasse, 2x 2.99€           |    0.95 |     5.98 |     3.02 |   18.96 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:5234968294, CID:100       |         |          |          |             |
 * | Eric Meyer                    |         |          |          |             |
 * | - 1 Kanne, 1x 19.99€          |    3.19 |    19.99 |     3.19 |   19.99 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:6135735635, CID:103       |         |          |          |             |
 * | Nadine-Ulla Blumenfeld        |         |          |          |             |
 * | - 12 Teller, 12x 6.49€        |   12.43 |    77.88 |          |             |
 * | - 1 Buch "Java", 1x 49.90€    |    3.26*|    49.90 |          |             |
 * | - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |    20.92 |  207.73 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:6173043537, CID:105       |         |          |          |             |
 * | Lena Neumann                  |         |          |          |             |
 * | - 1 Buch "Java", 1x 49.90€    |    3.26*|    49.90 |          |             |
 * | - 1 Fahrradkarte, 1x 6.95€    |    0.45*|     6.95 |     3.71 |   56.85 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:7372561535, CID:100       |         |          |          |             |
 * | Eric Meyer                    |         |          |          |             |
 * | - 1 Fahrradhelm, 1x 169.00€   |   26.98 |   169.00 |          |             |
 * | - 1 Fahrradkarte, 1x 6.95€    |    0.45*|     6.95 |    27.43 |  175.95 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 * | OID:4450305661, CID:100       |         |          |          |             |
 * | Eric Meyer                    |         |          |          |             |
 * | - 3 Tasse, 3x 2.99€           |    1.43 |     8.97 |          |             |
 * | - 3 Becher, 3x 1.49€          |    0.71 |     4.47 |          |             |
 * | - 1 Kanne, 1x 19.99€          |    3.19 |    19.99 |     5.33 |   33.43 EUR |
 * +-------------------------------+---------+----------+----------+-------------+
 *                                              Gesamt: |    76.78 |  642.70 EUR |
 *                                                      +==========+=============+
 * </pre>
 */
public class RunDatamodelDemo implements Runnable {

    /**
     * Component that performs price and VAT taxt calulations.
     */
    private final Calculator calcuator = Accessor.getInstance().getCalculator();

    /**
     * Method to run the application.
     * @param context {@link ApplicationContext} instance
     */
    @Override
    public void run(ApplicationContext context) {

        /*
         * Customers:
         */
        // final Customer eric = new Customer("Eric Meyer")
        //     .setId(100L)
        //     .addContact("eme@gmail.com")
        //     .addContact("+49 030 515 141345")
        //     .addContact("fax: 030 234-134651")  // duplicate entry
        //     .addContact("fax: 030 234-134651");

        // final Customer anne = new Customer("Bayer, Anne")
        //     .setId(101L)
        //     .addContact("anne24@yahoo.de")
        //     .addContact("(030) 3481-23352");

        // final Customer tim = new Customer("Tim Schulz-Mueller")
        //     .setId(102L)
        //     .addContact("tim2346@gmx.de");

        // final Customer nadine = new Customer("Nadine-Ulla Blumenfeld")
        //     .setId(103L)
        //     .addContact("+49 152-92454");

        // final Customer khaled = new Customer("Khaled Saad Mohamed Abdelalim")
        //     .setId(104L)
        //     .addContact("+49 1524-12948210");

        // final Customer lena = new Customer("Lena Neumann")
        //     .setId(105L)
        //     .addContact("lena228@gmail.com");
        // 
        final var eric = DataModel.createCustomer("Eric Meyer")
            .map(c -> c
                .addContact("eme@gmail.com")
                .addContact("+49 030 515 141345")
                .addContact("fax: 030 234-134651")  // duplicate entry
                .addContact("fax: 030 234-134651")
            );

        final var anne = DataModel.createCustomer("Bayer, Anne")
            .map(c -> c
                .addContact("anne24@yahoo.de")
                .addContact("(030) 3481-23352")
            );

        final var tim = DataModel.createCustomer("Tim Schulz-Mueller")
            .map(c -> c.addContact("tim2346@gmx.de"));

        final var nadine = DataModel.createCustomer("Nadine-Ulla Blumenfeld")
            .map(c -> c.addContact("+49 152-92454"));

        final var khaled = DataModel.createCustomer("Khaled Saad Mohamed Abdelalim")
            .map(c -> c.addContact("+49 1524-12948210"));

        final var lena = DataModel.createCustomer("Lena Neumann")
            .map(c -> c.addContact("lena228@gmail.com"));

        // final List<Customer> customers = new ArrayList<>(List.of(
        //     eric, anne, tim, nadine, khaled, lena
        // ));
        // 
        final List<Customer> customers = List.of(eric, anne, tim, nadine, khaled, lena)
            .stream()
            .flatMap(Optional::stream)
            .toList();

        /*
         * Articles:
         */
        // var tasse = new Article("SKU-458362", "Tasse", 299);
        // var becher = new Article("SKU-693856", "Becher", 149);
        // var kanne = new Article("SKU-518957", "Kanne", 1999);
        // var teller = new Article("SKU-638035", "Teller", 649);
        // var buch_Java = new Article("SKU-278530", "Buch \"Java\"", 4990, true);
        // var buch_OOP = new Article("SKU-425378", "Buch \"OOP\"", 7995, true);
        // var pfanne = new Article("SKU-300926", "Pfanne", 4999);
        // var fahrradhelm = new Article("SKU-663942", "Fahrradhelm", 16900);
        // var fahrradkarte = new Article("SKU-583978", "Fahrradkarte", 695, true);
        // var radio = new Article("SKU-588268", "Radio", 10000);
        // 
        var tasse = DataModel.createArticle("SKU-458362", "Tasse", 299);
        var becher = DataModel.createArticle("SKU-693856", "Becher", 149);
        var kanne = DataModel.createArticle("SKU-518957", "Kanne", 1999);
        var teller = DataModel.createArticle("SKU-638035", "Teller", 649);
        var buch_Java = DataModel.createArticle("SKU-278530", "Buch \"Java\"", 4990, true);
        var buch_OOP = DataModel.createArticle("SKU-425378", "Buch \"OOP\"", 7995, true);
        var pfanne = DataModel.createArticle("SKU-300926", "Pfanne", 4999);
        var fahrradhelm = DataModel.createArticle("SKU-663942", "Fahrradhelm", 16900);
        var fahrradkarte = DataModel.createArticle("SKU-583978", "Fahrradkarte", 695, true);
        var radio = DataModel.createArticle("SKU-588268", "Radio", 10000);

        // final List<Article> articles = new ArrayList<>(List.of(
        //     tasse, becher, kanne, buch_Java, buch_OOP, pfanne, fahrradhelm, fahrradkarte, radio
        // ));
        // 
        final List<Article> articles = List.of(
                tasse, becher, kanne, buch_Java, buch_OOP, pfanne, fahrradhelm, fahrradkarte, radio
            ).stream()
            .flatMap(Optional::stream)
            .toList();

        /*
         * Orders:
         */
        final List<Order> orders = List.of(
            // // 
            // // Eric's 1st order
            // new Order(8592356245L, eric)
            //     .addItem(teller, 4)     // + item: 4 Teller, 4x 6.49 € = 25.96 €, 19% MwSt (4.14€)
            //     .addItem(becher, 8)     // + item: 8 Becher, 8x 1.49 € = 11.92 €, 19% MwSt (1.90€)
            //     .addItem(buch_OOP, 1)   // + item: 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
            //     .addItem(tasse, 4),     // + item: 4 Tassen, 4x 2.99 € = 11.96 €, 19% MwSt (1.91€)
            // // 
            // // Anne's order
            // new Order(3563561357L, anne)
            //     .addItem(teller, 2)
            //     .addItem(tasse, 2),
            // // 
            // // Eric's 2nd order
            // new Order(5234968294L, eric)
            //     .addItem(kanne, 1),
            // // 
            // // Nadine's order
            // new Order(6135735635L, nadine)
            //     .addItem(teller, 12)
            //     .addItem(buch_Java, 1)
            //     .addItem(buch_OOP, 1),
            // // 
            // // Lena's order
            // new Order(6173043537L, lena)
            //     .addItem(buch_Java, 1)
            //     .addItem(fahrradkarte, 1),
            // // 
            // // Eric's 3rd order
            // new Order(7372561535L, eric)
            //     .addItem(fahrradhelm, 1)
            //     .addItem(fahrradkarte, 1),
            // // 
            // // Eric's 4th order
            // new Order(4450305661L, eric)
            //     .addItem(tasse, 3)
            //     .addItem(becher, 3)
            //     .addItem(kanne, 1)
            // 
            // Eric's 1st order
            // new Order(8592356245L, eric)
            DataModel.createOrder(8592356245L, eric)
                .map(o -> o
                    .addItem(teller, 4)     // + item: 4 Teller, 4x 6.49 € = 25.96 €, 19% MwSt (4.14€)
                    .addItem(becher, 8)     // + item: 8 Becher, 8x 1.49 € = 11.92 €, 19% MwSt (1.90€)
                    .addItem(buch_OOP, 1)   // + item: 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
                    .addItem(tasse, 4)      // + item: 4 Tassen, 4x 2.99 € = 11.96 €, 19% MwSt (1.91€)
                ),
            // Anne's order
            // new Order(3563561357L, anne)
            DataModel.createOrder(3563561357L, anne)
                .map(o -> o
                    .addItem(teller, 2)
                    .addItem(tasse, 2)
                ),
            // Eric's 2nd order
            // new Order(5234968294L, eric)
            DataModel.createOrder(5234968294L, eric)
                .map(o -> o
                    .addItem(kanne, 1)
                ),
            // Nadine's order
            // new Order(6135735635L, nadine)
            DataModel.createOrder(6135735635L, nadine)
                .map(o -> o
                    .addItem(teller, 12)
                    .addItem(buch_Java, 1)
                    .addItem(buch_OOP, 1)
                ),
            // Lena's order
            // new Order(6173043537L, lena)
            DataModel.createOrder(6173043537L, lena)
                .map(o -> o
                    .addItem(buch_Java, 1)
                    .addItem(fahrradkarte, 1)
                ),
            // Eric's 3rd order
            // new Order(7372561535L, eric)
            DataModel.createOrder(7372561535L, eric)
                .map(o -> o
                    .addItem(fahrradhelm, 1)
                    .addItem(fahrradkarte, 1)
                ),
            // Eric's 4th order
            // new Order(4450305661L, eric)
            DataModel.createOrder(4450305661L, eric)
                .map(o -> o
                    .addItem(tasse, 3)
                    .addItem(becher, 3)
                    .addItem(kanne, 1)
                )
        ).stream()
        .flatMap(Optional::stream)
        .toList();

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
            var id = Long.toString(customer.id());
            var name = customer.name().length()==0? " " : customer.name();
            var firstNames = customer.firstNames().length()==0? " " : customer.firstNames();
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
                var id = Long.toString(customer.id());
                var name = customer.name().length()==0? " " : customer.name();
                var firstNames = customer.firstNames().length()==0? " " : customer.firstNames();
                List<String> contacts = new ArrayList<>();
                customer.contactsAsIterable().forEach(contacts::add);
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
            var formatter = components.Accessor.getInstance().getFormatter();
            var id = article.id();
            var description = article.description();
            long unit_price = article.unitPrice();
            var unit_price_fmt = formatter.fmtPrice(unit_price, Currency.EUR, 1);
            // 
            var reduced = article.reduced_VAT();
            double rate = reduced? 7.0 : 19.0;  // 19% VAT (MwSt), 7.0% reduced VAT
            var marker = reduced? "*" : " ";
            var rate_fmt = String.format("%.1f%s", rate, marker);
            var incl_vat = calcuator.includedVAT(unit_price, rate/100.0);
            var incl_vat_fmt = formatter.fmtPrice(incl_vat, Currency.EUR, 0);
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
     * Orders (detailed):
     * +-------------------------------+---------+----------+----------+-------------+
     * | ORDER                         |    MwSt*|    Preis |     MwSt |      Gesamt |
     * +-------------------------------+---------+----------+----------+-------------+
     * | OID:8592356245, CID:100       |         |          |          |             |
     * | Eric Meyer                    |         |          |          |             |
     * | - 4 Teller, 4x 6.49€          |    4.14 |    25.96 |          |             |
     * | - 8 Becher, 8x 1.49€          |    1.90 |    11.92 |          |             |
     * | - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |          |             |
     * | - 4 Tasse, 4x 2.99€           |    1.91 |    11.96 |    13.18 |  129.79 EUR |
     * +-------------------------------+---------+----------+----------+-------------+
     * | OID:3563561357, CID:101       |         |          |          |             |
     * | Anne Bayer                    |         |          |          |             |
     * | - 2 Teller, 2x 6.49€          |    2.07 |    12.98 |          |             |
     * | - 2 Tasse, 2x 2.99€           |    0.95 |     5.98 |     3.02 |   18.96 EUR |
     * +-------------------------------+---------+----------+----------+-------------+
     * | OID:7372561535, CID:100       |         |          |          |             |
     * | Eric Meyer                    |         |          |          |             |
     * | - 1 Fahrradhelm, 1x 169.00€   |   26.98 |   169.00 |          |             |
     * | - 1 Fahrradkarte, 1x 6.95€    |    0.45*|     6.95 |    27.43 |  175.95 EUR |
     * +-------------------------------+---------+----------+----------+-------------+
     *                                              Gesamt: |    43.63 |  324.70 EUR |
     *                                                      +==========+=============+
     * </pre>
     * 
     * TableFormatter used to format a table of {@link Order} objects.
     */
    private final TableFormatter orderTableFormatter =
        TableFormatter.builder()
            // // 
            // .columns("| ORDER-ID | CUSTOMER | CUST-ID | ITEMS |")
            // .widths(12, 28, 10, 24)
            // .alignments("LLRR")
            // // 
            // .rowMapper(Order.class, orderRowMapper())
            // // 
            // .build();
            // 
            // adjusted for detailed {@link Order} table:
            .columns("| ORDER | MwSt*| Preis | MwSt | Gesamt |")
            .widths(31, 9, 10, 10, 13)
            .alignments("LRRRR")
            // 
            .multiRowMapper(Order.class, orderMultiRowMapper())
            // 
            .build();

    /**
     * Single-row mapper maps an {@link Order} object to a single table row and
     * only shows the number of items.
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
     */
    private final Function<Order, String[]> orderRowMapper() {
        return order -> {
            var id = Long.toString(order.id());
            var customerName = String.format("%s, %s", order.customer().name(), order.customer().firstNames());
            var customerId = Long.toString(order.customer().id());
            var items = Integer.toString(order.items().size());
            return new String[] {id, customerName, customerId, items};
        };
    }

    /**
     * Multi-row mapper maps an {@link Order} object to multiple table rows showing
     * the detail of an order with all order items.
     * <pre>
     * Orders (detailed):
     * +-------------------------------+---------+----------+----------+-------------+
     * | ORDER                         |    MwSt*|    Preis |     MwSt |      Gesamt |
     * +-------------------------------+---------+----------+----------+-------------+
     * | OID:8592356245, CID:100       |         |          |          |             |
     * | Eric Meyer                    |         |          |          |             |
     * | - 4 Teller, 4x 6.49€          |    4.14 |    25.96 |          |             |
     * | - 8 Becher, 8x 1.49€          |    1.90 |    11.92 |          |             |
     * | - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |          |             |
     * | - 4 Tasse, 4x 2.99€           |    1.91 |    11.96 |    13.18 |  129.79 EUR |
     * +-------------------------------+---------+----------+----------+-------------+
     * </pre>
     */
    private final Function<Order, String[][]> orderMultiRowMapper() {
        return order -> {
            var calculator = Accessor.getInstance().getCalculator();
            var formatter = Accessor.getInstance().getFormatter();
            var currency = datamodel.Currency.EUR;
            // 
            var columns = 5;
            int offset = 2;
            var items = order.items();
            int lines = items.size() + offset;
            String[][] rows = new String[lines + 1][columns];
            // 
            var orderId = Long.toString(order.id());
            var customerId = Long.toString(order.customer().id());
            var orderDesc = String.format("OID:%s, CID:%s", orderId, customerId);
            var customerName = String.format("%s %s",
                order.customer().firstNames(), order.customer().name());
            // 
            rows[0] = new String[] {orderDesc, " ", " ", " ", " "};
            rows[1] = new String[] {customerName, " ", " ", " ", " "};
            // 
            long orderValue = 0L;
            long orderTax = 0L;
            int len = items.size();
            for(int i=0; i < len; i++) {
                var item = items.get(i);
                var lineItem = String.format("- %d %s, %dx %s", item.unitsOrdered(), item.article().description(),
                            item.unitsOrdered(), formatter.fmtPrice(item.article().unitPrice(), currency, 4));
                var reducedVATMarker = item.article().reduced_VAT()? "*" : " ";
                // 
                long itemValue = calculator.valueOrderItem(item); 
                long itemTax = calculator.vatOrderItem(item);
                orderValue += itemValue;
                orderTax += itemTax;
                String mwst1 = formatter.fmtPrice(itemTax, currency, 0) + reducedVATMarker;
                String p1 = formatter.fmtPrice(itemValue, currency, 0);
                String mwst2 = i < (len-1)? " " : formatter.fmtPrice(orderTax, currency, 0);
                String p2 = i < (len-1)? " " : formatter.fmtPrice(orderValue, currency, currency.style(1, 4));
                // 
                rows[i + offset] = new String[] {lineItem, mwst1, p1, mwst2, p2};
            }
            rows[rows.length-1] = new String[] {"{---}", "{---}", "{---}", "{---}", "{---}"};
            return rows;
        };
    }

    /**
     * Print {@link Order} table.
     * @param customers each {@link Article} is printed as row in the table
     */
    private void printOrderTable(List<Order> orders) {
        var calculator = Accessor.getInstance().getCalculator();
        var formatter = Accessor.getInstance().getFormatter();
        var currency = datamodel.Currency.EUR;
        // 
        var otf = orderTableFormatter
            .text("Orders:")
            .header();
        // 
        orders.forEach(order -> {
            otf.row(order);
        });
        // 
        // otf.line().print(System.out);
        // 
        otf.row("", "", "{ }Gesamt:",
                formatter.fmtPrice(calculator.vatOrders(orders), currency, 0),
                formatter.fmtPrice(calculator.valueOrders(orders), currency, currency.style(1, 4))
            )
            .row("", "", "", "{===}", "{===}")
            .print(System.out);
    }
}
