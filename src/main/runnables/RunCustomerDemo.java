package runnables;

import java.util.*;
import java.util.function.Function;

import application.ApplicationContext;
import application.Runnable;
import datamodel.Customer;


/**
 * Demo that prints a table of {@link Customer} objects:
 * <pre>
 * +------+----------------+--------------------+----------------------------+
 * |   ID | NAME           | FIRSTNAMES         | CONTACTS                   |
 * +------+----------------+--------------------+----------------------------+
 * |  100 | Meyer          | Eric               | +49 030 515 141345         |
 * |      |                |                    | eme@gmail.com              |
 * |      |                |                    | fax: 030 234-134651        |
 * +------+----------------+--------------------+----------------------------+
 * |  101 | Bayer          | Anne               | anne24@yahoo.de            |
 * |      |                |                    | (030) 3481-23352           |
 * +------+----------------+--------------------+----------------------------+
 * |  102 | Schulz-Mueller | Tim                | tim2346@gmx.de             |
 * |  103 | Blumenfeld     | Nadine-Ulla        | +49 152-92454              |
 * |  104 | Abdelalim      | Khaled Saad Mohamed| +49 1524-12948210          |
 * +------+----------------+--------------------+----------------------------+
 * </pre>
 */
public class RunCustomerDemo implements Runnable {

    /**
     * TableFormatter used to format a table of {@link Customer} objects.
     */
    private final TableFormatter customerTableFormatter =
        TableFormatter.builder()
            // 
            .columns("| ID | NAME | FIRSTNAMES | CONTACTS |")
            .widths(6, 16, 20, 28)
            .alignments("R")
            // 
            // .rowMapper(Customer.class, rowMapper())
            .multiRowMapper(Customer.class, multiRowMapper())
            // 
            .build();

    /**
     * Method to run the application.
     * @param context {@link ApplicationContext} instance
     * @return chainable self-reference
     */
    @Override
    public void run(ApplicationContext context) {

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

        final List<Customer> customers = new ArrayList<>(List.of(
            eric, anne, tim, nadine, khaled
        ));

        customerTableFormatter
            .header()
                .row(eric)
                .row(anne)
                .row(tim)
                .row(nadine)
                .row(khaled)
                // .row(customers)
            .footer()
            .print(System.out);
    }


    /**
     * Row mapper maps a {@link Customer} object to {@code String[]} fields
     * that corresond to consecutive fields in one table row.
     * <pre>
     * +------+----------------+--------------------+----------------------------+
     * |  100 | Meyer          | Eric               | +49 030 515 141345         |
     * +------+----------------+--------------------+----------------------------+
     * </pre>
     */
    Function<Customer, String[]> rowMapper() {
        return c -> {
            var id = Long.toString(c.getId());
            var name = c.getName().length()==0? " " : c.getName();
            var firstNames = c.getFirstNames().length()==0? " " : c.getFirstNames();
            var contact = c.contact(0).length()==0? " " : c.contact(0);
            return new String[] {id, name, firstNames, contact};
        };
    }

    /**
     * A multi-row mapper maps a {@link Customer} object to multiple table rows,
     * e.g. to include multiple contacts.
     * <pre>
     * +------+----------------+--------------------+----------------------------+
     * |  100 | Meyer          | Eric               | +49 030 515 141345         |
     * |      |                |                    | eme@gmail.com              |
     * |      |                |                    | fax: 030 234-134651        |
     * +------+----------------+--------------------+----------------------------+
     * </pre>
     */
    Function<Customer, String[][]> multiRowMapper() {
        return c -> {
            String[][] rows = null;
            if(c != null) {
                var id = Long.toString(c.getId());
                var name = c.getName().length()==0? " " : c.getName();
                var firstNames = c.getFirstNames().length()==0? " " : c.getFirstNames();
                List<String> contacts = new ArrayList<>();
                c.getContactsAsIterable().forEach(contacts::add);
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
}
