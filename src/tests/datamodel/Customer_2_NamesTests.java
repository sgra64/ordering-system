package datamodel;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Names tests specify the acceptance of {@link Customer} names provided
 * as {@code single-String} names such as {@code "Eric Meyer"} or
 * {@code "Meyer, Eric"}.
 */
public class Customer_2_NamesTests {

    /**
     * Tests 200+ test single-string name as sequence of names with
     * trailing last name.
     */
    @Test @Order(200)
    public void test_200_name_in_order() {
        final Customer eric = new Customer("Eric Meyer");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(201)
    public void test_201_name_in_order_double_first() {
        final Customer eric = new Customer("Eric Robert Meyer");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric Robert", first);
    }

    @Test @Order(202)
    public void test_202_name_in_order_triple_first() {
        final Customer eric = new Customer("Eric Robert Louis Meyer");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric Robert Louis", first);
    }

    @Test @Order(203)
    public void test_203_name_in_order_dashed_last() {
        final Customer eric = new Customer("Eric Robert Meyer-Santos");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer-Santos", name);
        assertEquals("Eric Robert", first);
    }

    @Test @Order(204)
    public void test_204_name_in_order_triple_dashed_last() {
        final Customer eric = new Customer("Eric Robert Meyer-Santos-Ortega");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer-Santos-Ortega", name);
        assertEquals("Eric Robert", first);
    }

    @Test @Order(205)
    public void test_205_name_in_order_dashed_first() {
        final Customer eric = new Customer("Eric-Robert Meyer-Santos-Ortega");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer-Santos-Ortega", name);
        assertEquals("Eric-Robert", first);
    }

    @Test @Order(206)
    public void test_206_many_names_in_order() {
        final Customer khaled = new Customer("Khaled Saad Md. Abdelalim");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Abdelalim", name);
        assertEquals("Khaled Saad Md.", first);
    }

    @Test @Order(207)
    public void test_207_many_names_in_order() {
        final Customer khaled = new Customer("Khaled Md. Arif Saad-Abdelalim");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Saad-Abdelalim", name);
        assertEquals("Khaled Md. Arif", first);
    }

    @Test @Order(209)
    public void test_209_many_names_in_order() {
        final Customer khaled = new Customer("Khaled-Mohamed-Arif Saad-Abdelalim");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Saad-Abdelalim", name);
        assertEquals("Khaled-Mohamed-Arif", first);
    }

    /**
     * Tests 210+ test single-string names starting with the last name.
     */
    @Test @Order(210)
    public void test_210_last_name_first_comma() {
        final Customer eric = new Customer("Meyer, Eric");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(211)
    public void test_211_last_name_first_semicolon() {
        final Customer eric = new Customer("Meyer; Eric");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(212)
    public void test_212_last_name_with_dash() {
        final Customer tim = new Customer("Schulz‐Mueller, Tim");
        String name = tim.getName();
        String first = tim.getFirstNames();
        // 
        assertEquals("Schulz‐Mueller", name);
        assertEquals("Tim", first);
    }

    @Test @Order(213)
    public void test_213_last_name_with_dash() {
        final Customer nadine = new Customer("Blumenfeld; Nadine Ulla");
        String name = nadine.getName();
        String first = nadine.getFirstNames();
        // 
        assertEquals("Blumenfeld", name);
        assertEquals("Nadine Ulla", first);
    }

    @Test @Order(214)
    public void test_214_many_last_names() {
        final Customer khaled = new Customer("Saad-Abdelalim, Khaled Mohamed-Arif");
        String name = khaled.getName();
        String first = khaled.getFirstNames();
        // 
        assertEquals("Saad-Abdelalim", name);
        assertEquals("Khaled Mohamed-Arif", first);
    }

    /**
     * Tests 220+ test handling of whitespaces.
     */
    @Test @Order(220)
    public void test_220_spaces() {
        final Customer eric = new Customer(" Eric  Meyer   ");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(221)
    public void test_221_quotes() {
        final Customer eric = new Customer(" 'Eric Meyer'  ");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(222)
    public void test_222_double_quotes() {
        final Customer eric = new Customer(" \"Eric Meyer\"  ");
        String name = eric.getName();
        String first = eric.getFirstNames();
        // 
        assertEquals("Meyer", name);
        assertEquals("Eric", first);
    }

    @Test @Order(223)
    public void test_223_whitespaces() {
        final Customer nadine = new Customer("\t  Nadine  \t\t   Ulla     Blumenfeld  \t");
        String name = nadine.getName();
        String first = nadine.getFirstNames();
        // 
        assertEquals("Blumenfeld", name);
        assertEquals("Nadine Ulla", first);
    }

    /**
     * Test 230 tests extremely long names.
     */
    @Test @Order(230)
    public void test_230_extreme_long_names() {
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
