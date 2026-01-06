package datamodel;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Names tests specify the acceptance of {@link Customer} names provided
 * as {@code single-String} names such as {@code "Eric Meyer"} or
 * {@code "Meyer, Eric"}.
 */
public class Customer_3_NamesTests {

    /**
     * Tests 300+ test single-string name as sequence of names with
     * trailing last name.
     */
    @Test @Order(300)
    public void test_300_name_in_order() {
        final Customer eric = new Customer("Eric Meyer");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(301)
    public void test_301_name_in_order_double_first() {
        final Customer eric = new Customer("Eric Robert Meyer");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric Robert", first);
    }

    @Test @Order(302)
    public void test_302_name_in_order_triple_first() {
        final Customer eric = new Customer("Eric Robert Louis Meyer");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric Robert Louis", first);
    }

    @Test @Order(303)
    public void test_303_name_in_order_dashed_last() {
        final Customer eric = new Customer("Eric Robert Meyer-Santos");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer-Santos", name);
        assertEquals("Eric Robert", first);
    }

    @Test @Order(304)
    public void test_304_name_in_order_triple_dashed_last() {
        final Customer eric = new Customer("Eric Robert Meyer-Santos-Ortega");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer-Santos-Ortega", name);
        assertEquals("Eric Robert", first);
    }

    @Test @Order(305)
    public void test_305_name_in_order_dashed_first() {
        final Customer eric = new Customer("Eric-Robert Meyer-Santos-Ortega");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer-Santos-Ortega", name);
        assertEquals("Eric-Robert", first);
    }

    @Test @Order(306)
    public void test_306_many_names_in_order() {
        final Customer khaled = new Customer("Khaled Saad Md. Abdelalim");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Abdelalim", name);
        assertEquals("Khaled Saad Md.", first);
    }

    @Test @Order(307)
    public void test_307_many_names_in_order() {
        final Customer khaled = new Customer("Khaled Md. Arif Saad-Abdelalim");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Saad-Abdelalim", name);
        assertEquals("Khaled Md. Arif", first);
    }

    @Test @Order(309)
    public void test_309_many_names_in_order() {
        final Customer khaled = new Customer("Khaled-Mohamed-Arif Saad-Abdelalim");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Saad-Abdelalim", name);
        assertEquals("Khaled-Mohamed-Arif", first);
    }

    /**
     * Tests 310+ test single-string names starting with the last name.
     */
    @Test @Order(310)
    public void test_310_last_name_first_comma() {
        final Customer eric = new Customer("Meyer, Eric");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(311)
    public void test_311_last_name_first_semicolon() {
        final Customer eric = new Customer("Meyer; Eric");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(312)
    public void test_312_last_name_with_dash() {
        final Customer tim = new Customer("Schulz‐Mueller, Tim");
        String name = tim.getName();
        String first = tim.getFirstNames();
        // 
        assertEquals("Schulz‐Mueller", name);
        assertEquals("Tim", first);
    }

    @Test @Order(313)
    public void test_313_last_name_with_dash() {
        final Customer nadine = new Customer("Blumenfeld; Nadine Ulla");
        String name = nadine.getName();
        String first = nadine.getFirstNames();
        // 
        assertEquals("Blumenfeld", name);
        assertEquals("Nadine Ulla", first);
    }

    @Test @Order(314)
    public void test_314_many_last_names() {
        final Customer khaled = new Customer("Saad-Abdelalim, Khaled Mohamed-Arif");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Saad-Abdelalim", name);
        assertEquals("Khaled Mohamed-Arif", first);
    }

    /**
     * Tests 320+ test handling of whitespaces.
     */
    @Test @Order(320)
    public void test_320_spaces() {
        final Customer eric = new Customer(" Eric  Meyer   ");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(321)
    public void test_321_quotes() {
        final Customer eric = new Customer(" 'Eric Meyer'  ");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(322)
    public void test_322_double_quotes() {
        final Customer eric = new Customer(" \"Eric Meyer\"  ");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(323)
    public void test_323_whitespaces() {
        final Customer nadine = new Customer("\t  Nadine  \t\t   Ulla     Blumenfeld  \t");
        String name = nadine.getName();
        String first = nadine.getFirstNames();
        // 
        assertEquals("Blumenfeld", name);
        assertEquals("Nadine Ulla", first);
    }

    /**
     * Test 330 tests extremely long names.
     */
    @Test @Order(330)
    public void test_330_extreme_long_names() {
        var auguste = new Customer("Auguste Viktoria Friederike Luise Feodora Jenny "
                + "von-Schleswig-Holstein-Sonderburg-Augustenburg");
        String name = auguste.getName();
        String first = auguste.getFirstNames();
        assertEquals("von-Schleswig-Holstein-Sonderburg-Augustenburg", name);
        assertEquals("Auguste Viktoria Friederike Luise Feodora Jenny", first);
        // 
        auguste = new Customer("von-Schleswig-Holstein-Sonderburg-Augustenburg,"
                + "Auguste Viktoria Friederike Luise Feodora Jenny");
        name = auguste.getName();
        first = auguste.getFirstNames();
        assertEquals("von-Schleswig-Holstein-Sonderburg-Augustenburg", name);
        assertEquals("Auguste Viktoria Friederike Luise Feodora Jenny", first);
        // 
        var theo = new Customer("Buhl-Freiherr von und zu Guttenberg,"
                + "Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester");
        name = theo.getName();
        first = theo.getFirstNames();
        assertEquals("Buhl-Freiherr von und zu Guttenberg", name);
        assertEquals("Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester", first);
    }
}
