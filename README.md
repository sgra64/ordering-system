<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!-- A1 (SE-2)
-->
# C3: *Datamodel*, Classes: *Customer*, *Article*, *Order*

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!-- 
The assignment presents a simple ordering system with *Customers*, *Orders*,
*OrderItems* and *Articles*:

<img src="https://raw.githubusercontent.com/sgra64/ordering-system/refs/heads/markup/img/customer-1.png" width="660"/>
 -->

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

---

Steps:

- [Fetch Code-drop *c3-datamodel*](#1-fetch-code-drop-c3-datamodel)

- [Implement Class *Article*](#2-implement-class-article)

- [Implement *Calculator*](#3-implement-calculator)

- [Implement Unit Tests for *Calculator*](#4-implement-unit-tests-for-calculator)

- [Implement Class *Order*](#5-implement-class-order)

- [Print all Tables: *Customer*, *Article* and *Order*](#6-print-all-tables-customer-article-and-order)



<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 1. Fetch Code-drop *c3-datamodel*

Create a new local branch `c3-datamodel` starting from `base` (tagged where
branch *c2-customer* was branched-off, the commit before the one labeled with
*"code-drop: se1-repo/c2-customer"*).

Merge branch `c2-customer` into `c3-datamodel` (squashed) and commit with:
`"merge branch: c2-customer"`. Show the commit log:

```
95f53e9 (HEAD -> c3-datamodel) merge branch: c2-customer            <-- 'c2-customer' merge commit
f9d1236 (tag: base, main) add src/main, src/resources, src/tests    <-- 'base'
9c2ecf4 add .gitmodules
e5a8c22 add .gitignore
832c97d (tag: root) root commit (empty)
```

Fetch the *code-drop* from branch
[*se1-repo*](https://github.com/sgra64/ordering-system/tree/c3-datamodel) / 
[*c3-datamodel*](https://github.com/sgra64/ordering-system/tree/c3-datamodel).

Check-out fetched code and commit to the new local branch `c3-datamodel`
with message `"code-drop: se1-repo/c3-datamodel"`:

```sh
git checkout se1-repo/c3-datamodel -- .
```

Commit with:
`"code drop: se1-repo/c3-datamodel"`. Show the commit log:

```
5d65aab (HEAD -> c3-datamodel) code drop: se1-repo/c3-datamodel     <-- 'c3-datamodel' code drop
95f53e9 merge branch: c2-customer                                   <-- 'c2-customer' merge commit
f9d1236 (tag: base, main) add src/main, src/resources, src/tests    <-- 'base'
9c2ecf4 add .gitmodules
e5a8c22 add .gitignore
832c97d (tag: root) root commit (empty)
```

The code drop does not compile at this point due to missing classes
*Article* and *Order*.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 2. Implement Class *Article*

Re-engineer missing class *Article.java* from the table and from the driver code:
[*runnables/RunDatamodelDemo.java*](src/main/runnables/RunDatamodelDemo.java).

The desired table for *Articles* is:

```
Articles:
+------------+--------------------+-------+----------+------------------------+
| ID         | DESCRIPTION        |  VAT %|      VAT |    (Germany) PRICE EUR |
+------------+--------------------+-------+----------+------------------------+
| SKU-458362 | Tasse              |  19.0 |     0.48 |               2.99 EUR |
| SKU-693856 | Becher             |  19.0 |     0.24 |               1.49 EUR |
| SKU-518957 | Kanne              |  19.0 |     3.19 |              19.99 EUR |
| SKU-278530 | Buch "Java"        |   7.0*|     3.26 |              49.90 EUR |
| SKU-425378 | Buch "OOP"         |   7.0*|     5.23 |              79.95 EUR |
| SKU-300926 | Pfanne             |  19.0 |     7.98 |              49.99 EUR |
| SKU-663942 | Fahrradhelm        |  19.0 |    26.98 |             169.00 EUR |
| SKU-583978 | Fahrradkarte       |   7.0*|     0.45 |               6.95 EUR |
| SKU-588268 | Radio              |  19.0 |    15.97 |             100.00 EUR |
+------------+--------------------+-------+----------+------------------------+
```

Prices for articles are internally processed in Cent (as *long*-values).
[*VAT*](https://en.wikipedia.org/wiki/Value-added_tax) stands for *"Value-added Tax"*
(*Mehrwertsteuer* für Endverbraucher bzw.
[*Umsatzsteuer*](https://de.wikipedia.org/wiki/Umsatzsteuer_(Deutschland))
zwischen Firmen). The regular VAT rate in Germany is `19%`. A reduced rate of `7%`
applies to grocery/food items, paper prints, medical prescription and (since Jan'26)
for restaurant and hospitality services.

The driver code
[*runnables/RunDatamodelDemo.java*](src/main/runnables/RunDatamodelDemo.java)
shows the creation of articles:

```java
/*
 * Create articles:
 */
var tasse = new Article("SKU-458362", "Tasse", 299);
var becher = new Article("SKU-693856", "Becher", 149);
var kanne = new Article("SKU-518957", "Kanne", 1999);
var teller = new Article("SKU-638035", "Teller", 649);
var buch_Java = new Article("SKU-278530", "Buch \"Java\"", 4990, true);  /** reduced VAT*/
var buch_OOP = new Article("SKU-425378", "Buch \"OOP\"", 7995, true);    /** reduced VAT*/
var pfanne = new Article("SKU-300926", "Pfanne", 4999);
var fahrradhelm = new Article("SKU-663942", "Fahrradhelm", 16900);
var fahrradkarte = new Article("SKU-583978", "Fahrradkarte", 695, true); /** reduced VAT*/
var radio = new Article("SKU-588268", "Radio", 10000);

final List<Article> articles = new ArrayList<>(List.of(
    tasse, becher, kanne, buch_Java, buch_OOP, pfanne, fahrradhelm, fahrradkarte, radio
));
```

Create class *Article* in the *datamodel* package that stores all relevant
attributes. Create methods that match the usage in the driver code.

*Article* objects are *immutable*. Once created, attribute values will not change.

Consider *lombok* and ommitting *setter*-methods to reduce boiler-plate code.

Also consider Java *record* as alternative to implement *Article*.


&nbsp;

#### ==> Consider a quick *Mock-Implementation* for *Order.java*

Code does not compile with missing class *Order*. Consider a quick mock-implementation
for *Order* to make code compile and print *Customer* and *Article* tables.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 3. Implement *Calculator*

With a working implementation of *Article*, the output table will show `0.00`
in the `VAT` column and not actual values for prices.

Identify the root cause of the problem in the new interface
[*Calculator*](src/main/datamodel/Calculator.java).

Develop a solution that shows actual values in the `VAT` column by creating a
proper implementation class (as *"strict Singleton"*) for the *Calculator*
interface and implemement the *VAT-calculation* method.

The *Article* table will show correct VAT values after implementation (also
shown below).


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 4. Implement Unit Tests for *Calculator*

Implement a test-class `CalculatorTests.java` and tests for following cases:

- `test_010_includedVAT_case100` - test for the regular cases of gross values:
    `100€` and `119€` with expected included VAT of `15.97€` and `19.00€` for
    19% VAT rate.

- `test_011_includedVAT_regular` - test VAT for all prices and VAT values in the
    *Article* table for the given rates:
    ```
    | SKU-458362 | Tasse              |  19.0 |     0.48 |               2.99 EUR |
    | SKU-693856 | Becher             |  19.0 |     0.24 |               1.49 EUR |
    | SKU-518957 | Kanne              |  19.0 |     3.19 |              19.99 EUR |
    | SKU-278530 | Buch "Java"        |   7.0*|     3.26 |              49.90 EUR |
    | SKU-425378 | Buch "OOP"         |   7.0*|     5.23 |              79.95 EUR |
    | SKU-300926 | Pfanne             |  19.0 |     7.98 |              49.99 EUR |
    | SKU-663942 | Fahrradhelm        |  19.0 |    26.98 |             169.00 EUR |
    | SKU-583978 | Fahrradkarte       |   7.0*|     0.45 |               6.95 EUR |
    | SKU-588268 | Radio              |  19.0 |    15.97 |             100.00 EUR |
    ```

- `test_020_includedVAT_rounding` - test rounding for cases for 19% VAT rate for
    gross and expected VAT values:
    ```java
        long[] gross = {
            0,   1,   2,   3,   4,   5,   6,   7,   8,   9,
            10,  11,  12,  13,  14,  15,  16,  17,  18,  19,
            20,  21,  22,  23,  24,  25,  26,  27,  28,  29,
            30,  31,  32,  33,  34,  35,  36,  37,  38,  39,
            40,  41,  42,  43,  44,  45,  46,  47,  48,  49,
            50,  51,  52,  53,  54,  55,  56,  57,  58,  59,
            60,  61,  62,  63,  64,  65,  66,  67,  68,  69,
            70,  71,  72,  73,  74,  75,  76,  77,  78,  79,
            80,  81,  82,  83,  84,  85,  86,  87,  88,  89,
            90,  91,  92,  93,  94,  95,  96,  97,  98,  99,
            100
    };
    long[] exp_vat = {
              0,   0,   0,   0,   1,   1,   1,   1,   1,   1,
              2,   2,   2,   2,   2,   2,   3,   3,   3,   3,
              3,   3,   4,   4,   4,   4,   4,   4,   4,   5,
              5,   5,   5,   5,   5,   6,   6,   6,   6,   6,
              6,   7,   7,   7,   7,   7,   7,   8,   8,   8,
              8,   8,   8,   8,   9,   9,   9,   9,   9,   9,
              10,  10,  10,  10,  10,  10,  11,  11,  11,  11,
              11,  11,  11,  12,  12,  12,  12,  12,  12,  13,
              13,  13,  13,  13,  13,  14,  14,  14,  14,  14,
              14,  15,  15,  15,  15,  15,  15,  15,  16,  16,
              16
    };
    ```

- `test_021_includedVAT_rounding` - test rounding for cases for 19% VAT rate for
    gross and expected VAT values:
    ```java
    long[] gross = {
        5000, 5001, 5002, 5003, 5004, 5005, 5006, 5007, 5008, 5009,
        5010, 5011, 5012, 5013, 5014, 5015, 5016, 5017, 5018, 5019,
        5020, 5021, 5022, 5023, 5024, 5025, 5026, 5027, 5028, 5029,
        5030, 5031, 5032, 5033, 5034, 5035, 5036, 5037, 5038, 5039,
        5040, 5041, 5042, 5043, 5044, 5045, 5046, 5047, 5048, 5049,
        5050, 5051, 5052, 5053, 5054, 5055, 5056, 5057, 5058, 5059
    };
    long[] exp = {
            798,  798,  799,  799,  799,  799,  799,  799,  800,  800,
            800,  800,  800,  800,  801,  801,  801,  801,  801,  801,
            802,  802,  802,  802,  802,  802,  802,  803,  803,  803,
            803,  803,  803,  804,  804,  804,  804,  804,  804,  805,
            805,  805,  805,  805,  805,  806,  806,  806,  806,  806,
            806,  806,  807,  807,  807,  807,  807,  807,  808,  808
    };
    ```

- `test_022_includedVAT_rounding_extreme` - test rounding for extreme cases
    ```java
    double rate = 0.19;
    assertEquals(16, calculator.includedVAT(100, rate));        //     1 EUR
    assertEquals(160, calculator.includedVAT(1000, rate));
    assertEquals(1597, calculator.includedVAT(10000, rate));    //   100 EUR
    assertEquals(15966, calculator.includedVAT(100000, rate));  // 1,000 EUR
    assertEquals(159664, calculator.includedVAT(1000000, rate));
    assertEquals(1596639, calculator.includedVAT(10000000, rate));
    assertEquals(15966387, calculator.includedVAT(100000000, rate));    // 1,000,000 EUR
    assertEquals(159663866, calculator.includedVAT(1000000000, rate));
    assertEquals(1596638655, calculator.includedVAT(10000000000L, rate));
    assertEquals(15966386555L, calculator.includedVAT(100000000000L, rate));    // 1,000,000,000 EUR
    assertEquals(159663865546L, calculator.includedVAT(1000000000000L, rate));
    assertEquals(1596638655462L, calculator.includedVAT(10000000000000L, rate));
    ```

- `test_030_negative_gross` - test that negative gross values have `0L` as result
    (there is no negative VAT tax).

- `test_031_negative_rate` - - test that a negative rate has `0L` as result
    (there is no negative VAT tax rate).

- `test_040_corner_cases` - test `Long.MAX_VALUE` corner cases:
    ```
    gross:                          19% included VAT
    Long.MAX_VALUE / 10L:         147263923277513248L
    Long.MAX_VALUE / 100L:         14726392327751324L
    Long.MAX_VALUE / 1000L:         1472639232775133L
    Long.MAX_VALUE / 10000L:         147263923277513L
    Long.MAX_VALUE / 1000000L:        14726392327751L
    Long.MAX_VALUE / 1000000L:         1472639232775L
    Long.MAX_VALUE / 10000000L:         147263923277L
    Long.MAX_VALUE / 100000000L:         14726392328L
    Long.MAX_VALUE / 1000000000L:         1472639233L
    Long.MAX_VALUE / 10000000000L:         147263923L
    Long.MAX_VALUE / 100000000000L:         14726392L
    Long.MAX_VALUE / 1000000000000L:         1472639L
    ```

- `test_041_corner_cases_extreme` - test `Long.MAX_VALUE` extreme corner cases:
    ```
    gross:                          19% included VAT
    Long.MAX_VALUE - 510L:       1472639232775132416L
    Long.MAX_VALUE - 510L:       1472639232775132416L
    Long.MAX_VALUE - 510L:       1472639232775132416L
    Long.MAX_VALUE - 510L:       1472639232775132160L
    Long.MAX_VALUE - 510L:       1472639232775132160L
    ```

Run *Calculator* tests:

```sh
mk run-tests -c datamodel.CalculatorTests
```

```
╷
├─ JUnit Jupiter ✔
│  └─ CalculatorTests ✔
│     ├─ test_010_includedVAT_case100() ✔
│     ├─ test_011_includedVAT_regular() ✔
│     ├─ test_020_includedVAT_rounding() ✔
│     ├─ test_021_includedVAT_rounding() ✔
│     ├─ test_022_includedVAT_rounding_extreme() ✔
│     ├─ test_030_negative_gross() ✔
│     ├─ test_031_negative_rate() ✔
│     ├─ test_040_corner_cases() ✔
│     └─ test_041_corner_cases_extreme() ✔
| 
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 161 ms
[         9 tests successful      ]
[         0 tests failed          ]
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 5. Implement Class *Order*

The driver code
[*runnables/RunDatamodelDemo.java*](src/main/runnables/RunDatamodelDemo.java)
shows the creation of orders:

```java
/*
* Create orders:
*/
List<Order> orders = new ArrayList<>(
    List.of(
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
    )
);
```

The *Orders* table shows entries for orders with: *ORDER-ID*, the owning
*Customer* with *NAME* and *CUST-ID* and the number of *ITEMS* (the
mock-implementation of *Order.java* will likely output 0):

```
Orders:
+------------+----------------------------+----------+------------------------+
| ORDER-ID   | CUSTOMER                   |  CUST-ID |                  ITEMS |
+------------+----------------------------+----------+------------------------+
| 8592356245 | Meyer, Eric                |      100 |                      4 |
| 3563561357 | Bayer, Anne                |      101 |                      2 |
| 5234968294 | Meyer, Eric                |      100 |                      1 |
| 6135735635 | Blumenfeld, Nadine-Ulla    |      103 |                      3 |
| 6173043537 | Neumann, Lena              |      105 |                      2 |
| 7372561535 | Meyer, Eric                |      100 |                      2 |
| 4450305661 | Meyer, Eric                |      100 |                      3 |
+------------+----------------------------+----------+------------------------+
```

At a later stage, orders will be printed in more detail (not yet):

```
+-------------------------------+---------+----------+----------+-------------+
| ORDER                         |    MwSt*|    Preis |     MwSt |      Gesamt |
+-------------------------------+---------+----------+----------+-------------+
| OID:8592356245, CID:100       |         |          |          |             |
| Eric Meyer                    |         |          |          |             |
| - 4 Teller, 4x 6.49€          |    4.14 |    25.96 |          |             |
| - 8 Becher, 8x 1.49€          |    1.90 |    11.92 |          |             |
| - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |          |             |
| - 4 Tasse, 4x 2.99€           |    1.91 |    11.96 |    13.18 |  129.79 EUR |
+-------------------------------+---------+----------+----------+-------------+
```

Define class *Order* (or extend the mock-class) in the *datamodel* package to
show the correct number of items of each order (as shown in the table above).

Create methods in *Order.java* that match the usage in the driver code.

Implement class *OrderItem* as an inner class of *Order*:

```java
/**
 * {@link Order} is an entity in the system that defines a collection of
 * {@link Order.OrderItem}s a {@link Customer} intends to purchase.
 */
public class Order {

    /**
     * Inner class of an {@link Order.OrderItem}.
     */
    @Getter
    @RequiredArgsConstructor
    public class OrderItem {

        /**
         * The Article ordered.
         * 
         * -- GETTER --
         * ordered {@link Article}
         */
        private final Article article;

        /**
         * Units of the Article ordered.
         * 
         * -- GETTER --
         * units ordered
         */
        private final int unitsOrdered;
    }
}
```

*Order* objects are *immutable*. Once created, attribute values will not change.

Consider *lombok* to reduce boiler-plate code for classes *Order* and *OrderItem*.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 6. Print all Tables: *Customer*, *Article* and *Order*

The completed assignment prints all tables for *Customer*, *Article* and *Order*:

```
(6) Customer objects built.
(9) Article objects built.
(7) Order objects built.
---
Customers:
+------+----------------------+----------------------+------------------------+
|   ID | NAME                 | FIRSTNAMES           | CONTACTS               |
+------+----------------------+----------------------+------------------------+
|  100 | Meyer                | Eric                 | eme@gmail.com          |
|      |                      |                      | +49 030 515 141345     |
|      |                      |                      | fax: 030 234-134651    |
+------+----------------------+----------------------+------------------------+
|  101 | Bayer                | Anne                 | anne24@yahoo.de        |
|      |                      |                      | (030) 3481-23352       |
+------+----------------------+----------------------+------------------------+
|  102 | Schulz-Mueller       | Tim                  | tim2346@gmx.de         |
|  103 | Blumenfeld           | Nadine-Ulla          | +49 152-92454          |
|  104 | Abdelalim            | Khaled Saad Mohamed  | +49 1524-12948210      |
|  105 | Neumann              | Lena                 | lena228@gmail.com      |
+------+----------------------+----------------------+------------------------+

Articles:
+------------+--------------------+-------+----------+------------------------+
| ID         | DESCRIPTION        |  VAT %|      VAT |    (Germany) PRICE EUR |
+------------+--------------------+-------+----------+------------------------+
| SKU-458362 | Tasse              |  19.0 |     0.48 |               2.99 EUR |
| SKU-693856 | Becher             |  19.0 |     0.24 |               1.49 EUR |
| SKU-518957 | Kanne              |  19.0 |     3.19 |              19.99 EUR |
| SKU-278530 | Buch "Java"        |   7.0*|     3.26 |              49.90 EUR |
| SKU-425378 | Buch "OOP"         |   7.0*|     5.23 |              79.95 EUR |
| SKU-300926 | Pfanne             |  19.0 |     7.98 |              49.99 EUR |
| SKU-663942 | Fahrradhelm        |  19.0 |    26.98 |             169.00 EUR |
| SKU-583978 | Fahrradkarte       |   7.0*|     0.45 |               6.95 EUR |
| SKU-588268 | Radio              |  19.0 |    15.97 |             100.00 EUR |
+------------+--------------------+-------+----------+------------------------+

Orders:
+------------+----------------------------+----------+------------------------+
| ORDER-ID   | CUSTOMER                   |  CUST-ID |                  ITEMS |
+------------+----------------------------+----------+------------------------+
| 8592356245 | Meyer, Eric                |      100 |                      0 |
| 3563561357 | Bayer, Anne                |      101 |                      0 |
| 5234968294 | Meyer, Eric                |      100 |                      0 |
| 6135735635 | Blumenfeld, Nadine-Ulla    |      103 |                      0 |
| 6173043537 | Neumann, Lena              |      105 |                      0 |
| 7372561535 | Meyer, Eric                |      100 |                      0 |
| 4450305661 | Meyer, Eric                |      100 |                      0 |
+------------+----------------------------+----------+------------------------+
```

Run the test for *Calculator*:

```sh
mk run-tests -c datamodel.CalculatorTests
```
```
╷
├─ JUnit Jupiter ✔
│  └─ CalculatorTests ✔
│     ├─ test_010_includedVAT_case100() ✔
│     ├─ test_011_includedVAT_regular() ✔
│     ├─ test_020_includedVAT_rounding() ✔
│     ├─ test_021_includedVAT_rounding() ✔
│     ├─ test_022_includedVAT_rounding_extreme() ✔
│     ├─ test_030_negative_gross() ✔
│     ├─ test_031_negative_rate() ✔
│     ├─ test_040_corner_cases() ✔
│     └─ test_041_corner_cases_extreme() ✔
|
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 187 ms
[         9 tests successful      ]
[         0 tests failed          ]
```

When you have reached that stage, commit to branch *c3-datamodel*
with message: `"datamodel complete, CalculatorTests.java passing"`.

Print the commit log:

```sh
log --graph --oneline c3-datamodel c2-customer
```

Output should show the correct structure of both branches:

```
                                                                    <-- branch 'c3-datamodel'
* 62800f1 (HEAD -> c3-datamodel) datamodel complete, CalculatorTests.java passing
* e7cd041 code drop: se1-repo/c3-datamodel
* 50369d2 merge branch: c2-customer
| 
|  * c2c6bb8 (c2-customer) update imports in tests (removed .*)     <-- branch 'c2-customer'
|  * 26436a1 switch to 2_NamesTests, 3_ContactsTests
|  * 618b273 add NameSplitterImpl.java, Customer_2_NamesTests.java
|  * ffc2f55 add Customer_3_ContactsTests.java
|  * ea15fa2 sanitized Customer.java, Customer_1_SanityTests.java
|  * 3a7b8ed add Customer.java, Customer_0_BaseTests.java
|  * 7a08ef8 code-drop: se1-repo/c2-customer
| /
|/
* f9d1236 (tag: base, main) add src/main, src/resources, src/tests  <-- 'base' of both branches
* 9c2ecf4 add .gitmodules
* e5a8c22 add .gitignore
* 832c97d (tag: root) root commit (empty)
```
