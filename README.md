<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!-- A1 (SE-2)
-->
# C2: *Customer* class

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
The assignment presents a simple ordering system with *Customers*, *Orders*,
*OrderItems* and *Articles*:

<img src="https://raw.githubusercontent.com/sgra64/ordering-system/refs/heads/markup/img/customer-1.png" width="660"/>


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

---

Steps:

- [Fetch Code-drop *c2-customer*](#1-fetch-code-drop-c2-customer)

- [Implement Class *Customer*](#2-implement-class-customer)

- [Sanitize Class *Customer*](#3-sanitize-class-customer)

- [*Customer* Contacts](#4-customer-contacts)

- [*Customer* Names](#5-customer-names)

- [Run all *Customer* Tests](#6-run-all-customer-tests)


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 1. Fetch Code-drop *c2-customer*

Fetch a code-drop from branch
[*c2-customer*](https://github.com/sgra64/ordering-system/tree/c2-customer)
from the [*se1-repo*](https://github.com/sgra64/ordering-system.git).

Make sure you have added the remote *se1-repo*:

```sh
git remote -v
```
```
se1-repo        https://github.com/sgra64/ordering-system (fetch)
se1-repo        https://github.com/sgra64/ordering-system (push)
```

```sh
# fetch branch 'c2-customer' from 'se1-repo'
git fetch se1-repo c2-customer

# show new remote branch
git branch -avv
```
```
remotes/origin/c2-customer    363dcbb add README.md
```

Create new local branch `c2-customer` off the *main* branch and show that
you are on that branch:

```sh
git branch
```
```
c2-customer
```

Chek-out paths `src/main` and `src/resources` from the code-drop from the
new branch and see the new additions:

```sh
git checkout se1-repo/c2-customer -- src/main
git checkout se1-repo/c2-customer -- src/resources

git status                      # see the delta of the code-drop

find src/main src/resources     # show new content under 'src'
```

Understand the new additions and the structure of the project, specifically:

- [*Application.java*](src/main/application/Application.java)

- [*ApplicationContext.java*](src/main/application/ApplicationContext.java)

- [*Runnable.java*](src/main/application/Runnable.java)

- [*RunCustomerDemo.java*](src/main/runnables/RunCustomerDemo.java)

- [*NameSplitter.java*](src/main/datamodel/NameSplitter.java)

- [*ContactsSplitter.java*](src/main/datamodel/ContactsSplitter.java)

- [*application.properties*](src/resources/application.properties)

- [*log4j2.properties*](src/resources/log4j2.properties)


```
src/main
src/main/application
src/main/application/Application.java               <-- Application main()
src/main/application/ApplicationContext.java        <-- ApplicationContext
src/main/application/package-info.java
src/main/application/Runnable.java                  <-- Runnable interface

src/main/application/runnables
src/main/application/runnables/RunCustomerDemo.java <-- demo
src/main/application/runnables/TableFormatter.java

src/main/datamodel                                  <-- add 'Customer.java'
src/main/datamodel/ContactsSplitter.java            <-- interface
src/main/datamodel/ContactsSplitterImpl.java        <-- implementation class
src/main/datamodel/NameSplitter.java                <-- add implementation class:
...                                                 <-- 'NameSplitterImpl.java'
src/main/module-info.java

src/main/runnables                                  <-- driver classes
src/main/runnables/RunCustomerDemo.java
src/main/runnables/TableFormatter.java

src/resources
src/resources/application.properties                <-- application.properties
src/resources/log4j2.properties                     <-- logger configuration
src/resources/META-INF
src/resources/META-INF/MANIFEST.MF
```

Commit the code-drop to branch *c2-customer* with message:
`code-drop: se1-repo/c2-customer`.


Answer questions:

1. What is *"ApplicationContext"* used for?

1. Name the three properties of the *"Singleton"* pattern used in
    [*ContactsSplitter.java*](src/main/datamodel/ContactsSplitter.java).

1. Understand the *"Builder"* pattern used in
    [*TableFormatter.java*](src/main/runnables/TableFormatter.java).

1. What is the purpose of configuration in
    [*application.properties*](src/resources/application.properties)

1. What is the purpose of
    [*NameSplitter.java*](src/main/datamodel/NameSplitter.java)?

1. What is the purpose of
    [*ContactsSplitter.java*](src/main/datamodel/ContactsSplitter.java)?

1. What are chainable methods, see
    [*RunCustomerDemo.java*](src/main/runnables/RunCustomerDemo.java)?


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 2. Implement Class *Customer*

Implement class *Customer* in package *"datamodel"* according to:
<img src="https://raw.githubusercontent.com/sgra64/ordering-system/refs/heads/markup/img/customer-1.png" width="660"/>

Run the demo:

```sh
mk run
```

The *Customer* table should show (with no split names yet):

```
+------+----------------+--------------------+----------------------------+
|   ID | NAME           | FIRSTNAMES         | CONTACTS                   |
+------+----------------+--------------------+----------------------------+
|  100 | Eric Meyer     | -                  | eme@gmail.com              |
|      |                |                    | +49 030 515 141345         |
|      |                |                    | fax: 030 234-134651        |
+------+----------------+--------------------+----------------------------+
|  101 | Bayer, Anne    | -                  | anne24@yahoo.de            |
|      |                |                    | (030) 3481-23352           |
+------+----------------+--------------------+----------------------------+
|  102 | Tim Schulz-Muel| -                  | tim2346@gmx.de             |
|  103 | Nadine-Ulla Blu| -                  | +49 152-92454              |
|  104 | Khaled Saad Moh| -                  | +49 1524-12948210          |
+------+----------------+--------------------+----------------------------+
```

Checkout the test for *Customer*:

```sh
git checkout se1-repo/c2-customer -- src/tests/datamodel/Customer_0_BaseTests.java
```

Make sure your *Customer*-implementation compiles with the test.

Run the test in your IDE and in the terminal:

```sh
mk run-tests -c datamodel.Customer_0_BaseTests
```
```
╷
├─ JUnit Jupiter ✔
│  └─ Customer_0_BaseTests ✔
│     ├─ test_061_chainable_methods_contacts() ✔
│     ├─ test_060_chainable_methods() ✔
│     ├─ test_050_contacts_getter_setter() ✔
│     ├─ test_030_name_getter_setter() ✔
│     ├─ test_010_initialization() ✔
│     ├─ test_040_firstNames_getter_setter() ✔
│     └─ test_020_id_getter_setter() ✔
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 436 ms
[         7 tests successful      ]
[         0 tests failed          ]
```

When tests are passing, commit to branch *c2-customer* with message:
`add Customer.java, Customer_0_BaseTests.java`.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 3. Sanitize Class *Customer*

In software development, *"sanitization"* is used as jargon for hardening an
implementation, which means making it compliant to more detailed specifications,
which are often given as *"sanity tests"*.

Checkout the test for *Customer*:

```sh
git checkout se1-repo/c2-customer -- src/tests/datamodel/Customer_1_SanityTests.java
```

Make sure your *Customer*-implementation still compiles with the test.

Understand the root causes when tests fail and fix your implementation in
*Customer.java*.

Run *sanity test* in your IDE and in the terminal:

```sh
mk run-tests -c datamodel.Customer_1_SanityTests
```
```
╷
├─ JUnit Jupiter ✔
│  └─ Customer_1_SanityTests ✔
│     ├─ test_130_null_names() ✔
│     ├─ test_100_positive_id() ✔
│     ├─ test_102_zero_id() ✔
│     ├─ test_101_negative_id() ✔
│     ├─ test_120_empty_names() ✔
│     ├─ test_140_empty_contacts() ✔
│     ├─ test_131_null_firstNames() ✔
│     ├─ test_141_null_contacts() ✔
│     └─ test_110_set_id_only_once() ✔
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 507 ms
[         9 tests successful      ]
[         0 tests failed          ]
```

When tests are passing, commit to branch *c2-customer* with message:
`sanitized Customer.java, Customer_1_SanityTests.java`.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 4. *Customer* Contacts

Understand [*ContactsSplitter.java*](src/main/datamodel/ContactsSplitter.java)
and how it represents multiple customer contacts.

Check *Customer* contacts tests and validate your implementation:

```sh
git checkout se1-repo/c2-customer -- src/tests/datamodel/Customer_2_ContactsTests.java
```
```sh
mk run-tests -c datamodel.Customer_2_ContactsTests
```
```
╷
├─ JUnit Jupiter ✔
│  └─ Customer_2_ContactsTests ✔
│     ├─ test_200_contact_initialization() ✔
│     ├─ test_220_multiple_contacts() ✔
│     ├─ test_210_single_contact() ✔
│     ├─ test_230_out_of_bounds_contacts() ✔
│     └─ test_221_multiple_contacts_list() ✔
| 
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 468 ms
[         5 tests successful      ]
[         0 tests failed          ]
```

When tests are passing, commit to branch *c2-customer* with message:
`add Customer_2_ContactsTests.java`.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 5. *Customer* Names

Understand interface [*NameSplitter.java*](src/main/datamodel/NameSplitter.java)
and how it deals with single-String names.

```java
package datamodel;

import java.util.Optional;

/**
 * Class splits <i>single-String</i> name into last- and first name parts
 * according to rules:
 * <ul>
 * <li> if a name contains no seperators (comma or semicolon {@code [,;]}),
 *      the trailing consecutive part is the last name, all prior parts
 *      are first name parts, e.g. {@code "Tim Anton Schulz-Müller"}, splits
 *      into <i>first name:</i> {@code "Tim Anton"} and <i>last name</i>
 *      {@code "Schulz-Müller"}.
 * <li> names with seperators (comma or semicolon {@code [,;]}) split into
 *      a last name part before the seperator and a first name part after
 *      the seperator, e.g. {@code "Schulz-Müller, Tim Anton"} splits into
 *      <i>first name:</i> {@code "Tim Anton"} and <i>last name</i>
 *      {@code "Schulz-Müller"}.
 * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]}
 *      and quotes {@code ["']} must be trimmed from names, e.g.
 *      {@code "  'Schulz-Müller, Tim Anton'    "}.
 * <li> interim white spaces between name parts must be trimmed, e.g.
 *      {@code "Schulz-Müller, <white-spaces> Tim <white-spaces> Anton <white-spaces> "}.
 * </ul>
 * <pre>
 * Examples:
 * +------------------------------------+-----------------------+-----------------------+
 * |Single-String name                  |first name parts       |last name parts        |
 * +------------------------------------+-----------------------+-----------------------+
 * |"Eric Meyer"                        |"Eric"                 |"Meyer"                |
 * |"Meyer, Anne"                       |"Anne"                 |"Meyer"                |
 * |"Meyer; Anne"                       |"Anne"                 |"Meyer"                |
 * |"Tim Schulz‐Mueller"                |"Tim"                  |"Schulz‐Mueller"       |
 * |"Nadine Ulla Blumenfeld"            |"Nadine Ulla"          |"Blumenfeld"           |
 * |"Nadine‐Ulla Blumenfeld"            |"Nadine‐Ulla"          |"Blumenfeld"           |
 * |"Khaled Saad Mohamed Abdelalim"     |"Khaled Saad Mohamed"  |"Abdelalim"            |
 * +------------------------------------+-----------------------+-----------------------+
 * 
 * Trim leading, trailing and interim white spaces and quotes:
 * +------------------------------------+-----------------------+-----------------------+
 * |" 'Eric Meyer'  "                   |"Eric"                 |"Meyer"                |
 * |"Nadine     Ulla     Blumenfeld"    |"Nadine Ulla"          |"Blumenfeld"           |
 * +------------------------------------+-----------------------+-----------------------+
 * </pre>
 */
public interface NameSplitter {

    /**
     * {@link NameSplitter} <i>Singleton</i> instance getter.
     * @return reference to <i>Singleton</i> instance
     */
    static NameSplitter getInstance() {
        return NameSplitterImpl.getInstance();
    }

    /**
     * Record of a name split into {@code name} and {@code firstNames} parts.
     */
    public record SplitName(String name, String firstNames) { }


    /**
     * Split single-String name into last- and first name parts.
     * @param name single-String name to split into first- and last name parts
     * @returns record {@link SplitName} or empty {@link Optional} if name
     * is illegal or could not be split
     */
    public Optional<SplitName> split(String name);

}
```

Implement the interface [*NameSplitter.java*](src/main/datamodel/NameSplitter.java)
with a new *Singleton* class *NameSplitterImpl.java*.

Implement all three properties of the *Singleton* pattern as in
[*ContactsSplitter.java*](src/main/datamodel/ContactsSplitter.java).

When you are done, check *Customer* names tests and validate your implementation:

```sh
git checkout se1-repo/c2-customer -- src/tests/datamodel/Customer_3_NamesTests.java
```
```sh
mk run-tests -c datamodel.Customer_3_NamesTests
```
```
╷
├─ JUnit Jupiter ✔
│  └─ Customer_3_NamesTests ✔
│     ├─ test_307_many_names_in_order() ✔
│     ├─ test_304_name_in_order_triple_dashed_last() ✔
│     ├─ test_309_many_names_in_order() ✔
│     ├─ test_305_name_in_order_dashed_first() ✔
│     ├─ test_322_double_quotes() ✔
│     ├─ test_321_quotes() ✔
│     ├─ test_314_many_last_names() ✔
│     ├─ test_323_whitespaces() ✔
│     ├─ test_311_last_name_first_semicolon() ✔
│     ├─ test_320_spaces() ✔
│     ├─ test_310_last_name_first_comma() ✔
│     ├─ test_312_last_name_with_dash() ✔
│     ├─ test_302_name_in_order_triple_first() ✔
│     ├─ test_313_last_name_with_dash() ✔
│     ├─ test_330_extreme_long_names() ✔
│     ├─ test_300_name_in_order() ✔
│     ├─ test_306_many_names_in_order() ✔
│     ├─ test_303_name_in_order_dashed_last() ✔
│     └─ test_301_name_in_order_double_first() ✔
| 
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 180 ms
[        19 tests successful      ]
[         0 tests failed          ]
```

When tests are passing, commit to branch *c2-customer* with message:
`add NameSplitterImpl.java, Customer_3_NamesTests.java`.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 6. Run all *Customer* Tests

Run all *Customer* Tests 

```sh
mk run-tests
```
```
Test run finished after 662 ms
[        42 tests successful      ]     <-- 42 tests successfull
[         0 tests failed          ]
```

Show the commit-log of branch *c2-customer*:

```
618b273 (HEAD -> c2-customer) add NameSplitterImpl.java, Customer_3_NamesTests.java
ffc2f55 add Customer_2_ContactsTests.java
ea15fa2 sanitized Customer.java, Customer_1_SanityTests.java
3a7b8ed add Customer.java, Customer_0_BaseTests.java
7a08ef8 code-drop: se1-repo/c2-customer
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
9c2ecf4 add .gitmodules
e5a8c22 add .gitignore
832c97d (tag: root) root commit (empty)
```
