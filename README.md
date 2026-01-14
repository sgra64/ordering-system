
<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
# F1: *Refactoring*

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
[*Refactoring*](https://refactoring.guru/refactoring)
is the process of changing (improving) the structure of the codebase without
changing its function. The program will perform the same function after the
refactoring as before the refactoring. No new features have been added.

Ideally, software development should be grouped into cycles (in Scrum:
*Sprints*) divided into, either:

- *Feature development* cycle - in which new features of the software are
    developed.

- *Refactoring cycle* - in which the code structure is improved with no new
    features added.

- *Bug fix* / *problem repair* cycle - in which bugs or problems are fixed
    with no new features added.

- *Release preparation* cycle - in which the software is prepared for release.

For a *Refactoring* cycle, the planned improvements should be stated at the
beginning and reviewed at the end.

Following problems have been identified for the current state of the code base
that should be improved by the refactoring:

1. **Problem:** Polluted `datamodel` package where classes of data structures (*Customer*,
    *Article*, *Order*) became intermixed with logic (*Splitter* classes):
    ```sh
    src/main/datamodel/Article.java
    src/main/datamodel/Calculator.java
    src/main/datamodel/CalculatorImpl.java
    src/main/datamodel/ContactsSplitter.java
    src/main/datamodel/ContactsSplitterImpl.java
    src/main/datamodel/Currency.java
    src/main/datamodel/Customer.java
    src/main/datamodel/NameSplitter.java
    src/main/datamodel/NameSplitterImpl.java
    src/main/datamodel/Order.java
    ```
    **Goal** is to clean up the `datamodel` package and separate data classes
    (*Customer*, *Article*, *Order*) from logic.

1. **Problem:** no coherent implementation of program *"logic"*, some logic is implemented
    simply as methods (e.g. `fmtPrice(long price, Currency currency, int... style)`)
    in the driver code (`RunDatamodelDemo.java`) while other is implemented
    as singleton instances, e.g. `Calculator.java`.

    **Goal** is to introduce a clean
    [*Component Architecture*](https://www.mendix.com/blog/what-is-component-based-architecture/)
    for the logic of the application.

1. **Problem:** data objects (*Customer*, *Article*, *Order*) are currently created and
    stored in the driver code `RunDatamodelDemo.java`:
    ```java
    // Customers:
    final Customer eric = new Customer("Eric Meyer")
        .setId(100L)
        .addContact("eme@gmail.com")
        .addContact("+49 030 515 141345");

    final Customer anne = new Customer("Bayer, Anne")
        .setId(101L)
        .addContact("(030) 3481-23352");

    final List<Customer> customers = new ArrayList<>(List.of(eric, anne));

    // Articles:
    var tasse = new Article("SKU-458362", "Tasse", 299);
    var becher = new Article("SKU-693856", "Becher", 149);
    var kanne = new Article("SKU-518957", "Kanne", 1999);

    final List<Article> articles = new ArrayList<>(List.of(tasse, becher, kanne));
    ```
    **Goal** is to introduce a central `DataStore` where objects are created by
    *factory* methods instead of using *new*, see also the
    [*Factory*](https://refactoring.guru/design-patterns/factory-method)
    pattern in software development. 

1. **Problem:** another observation is that in modern code data objects have been
    increasingly considered *immutable* (read-only) by not offering *setter* methods.
    It simplifies code and reduces the test code (no more *setters* to test).

    There are also deeper reasons for immutable data objects when considering that,
    in a real application, data objects reside in a database (e.g. as rows in a
    *CUSTOMER* table). Java-objects are copies of database entries and hence should
    represent their state. If attributes change, the change should be carried out
    in the underlying database and new Java-objects created from that.

    **Goal** is to turn data objects into *immutable* (read-only) objects using
    Java's [*record*](https://www.happycoders.eu/java/records).

1. **Problem:** `id` attributes must be unique among objects of a class, e.g. a *Customer* `id`.
    Assigning them *"somewhere in the code"* can lead to inconsisency.

    **Goal** is to centralize `id` generation and assignment to objects in *factory*
    methods at the `DataStore` (immutable data objects also have no setter method
    for the `id` attribute making this change necessary).

<!-- 
<img src="https://raw.githubusercontent.com/sgra64/ordering-system/refs/heads/markup/img/customer-order-article.png" width="800"/>
 -->


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

---

Steps:

- [Prepare new Branch: *f1-refactoring*](#1-prepare-new-branch-f1-refactoring)

- [Introduce Package *components*](#2-introduce-package-components)

- [Introduce a Coherent *Component Architecture*](#3-introduce-a-coherent-component-architecture)

- [New Component: *Formatter*](#4-new-component-formatter)

- [Data Model with *immutable* Classes](#5-data-model-with-immutable-classes)

- [*Fluid* Style in *Customer*](#6-fluid-style-in-customer)

- [Refactor *Unit Tests*](#7-refactor-unit-tests)

- [Print detailed *Order* Table](#8-print-detailed-order-table)

- [Refactor *TableFormatter* as Component](#9-refactor-tableFormatter-as-component)


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 1. Prepare new Branch: *f1-refactoring*

Prior stages of the development used branches *"c2-customer"* and *"c3-datamodel"*
spawned off the *"base"* commit.

Create a new branch *"f1-refactoring"* off the *"base"* commit:

```
       'base' commit
         v
(*) <-- (*) <----- (*) <----- (*)       <-- [main]
         \\\
          ||\+- (*) <-- (*)             <-- [c2-customer]
          ||
          | \+- (*) <-- (*) <-- (*)     <-- [c3-datamodel]
          |
           \+-- <-- [f1-refactoring]
```

Validate you have created the new branch correctly:

```sh
git branch
git log --oneline
find src
```
```
  c2-customer
  c3-datamodel
* f1-refactoring    <-- new branch is active (*)
  git-modules
  main

log:
  f9d1236 (HEAD -> f1-refactoring, tag: base, main) add src/main, src/resources, src/tests
  9c2ecf4 add .gitmodules
  e5a8c22 add .gitignore
  832c97d (tag: root) root commit (empty)

find:
  src
  src/main
  src/main/application
  src/main/application/Application.java
  src/main/application/package-info.java
  src/main/module-info.java
  src/resources
  src/resources/application.properties
  src/resources/log4j2.properties
  src/resources/META-INF
  src/resources/META-INF/MANIFEST.MF
  src/tests
  src/tests/application
  src/tests/application/Application_0_always_pass_Tests.java
```

Re-compile and run the application as it was at that stage:

```sh
mk clean compile run
```
```
Hello, 'SE-1 Ordering System' (modular)
 - arg: A
 - arg: BB
 - arg: CCC
```


&nbsp;

Next, merge the state of branch *"c3-datamodel"* (squashed) and commit with:
`"merge --squash c3-datamodel"`.

Re-compile and re-run the application:

```sh
mk clean compile run
git log --oneline
```

Tables for *Customer*, *Article* and *Order* should appear.

The commit log is:

```
15d342a (HEAD -> f1-refactoring) merge --squash c3-datamodel        <-- merge commit
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
9c2ecf4 add .gitmodules
e5a8c22 add .gitignore
832c97d (tag: root) root commit (empty)
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 2. Introduce Package *components*

In order to clean up package `datamodel`, a new package is created named: `components`
at the same level.

Move files concerning *"logic"* from package `datamodel` to the new package using
your IDE's refactoring function:

```
src/main/datamodel/Calculator.java
src/main/datamodel/CalculatorImpl.java
src/main/datamodel/ContactsSplitter.java
src/main/datamodel/ContactsSplitterImpl.java
src/main/datamodel/NameSplitter.java
src/main/datamodel/NameSplitterImpl.java
```

Fix compile errors, re-compile, re-run the code and verify the change has
been completed:

```sh
mk clean compile run

find src/main/datamodel
find src/main/components
```

Output should show the same tables as before. Package `datamodel` has been cleaned
up and logic moved to the new package `components`.

```
src/main/datamodel:
src/main/datamodel/Article.java
src/main/datamodel/Currency.java
src/main/datamodel/Customer.java
src/main/datamodel/Order.java

src/main/components:
src/main/components/Calculator.java
src/main/components/CalculatorImpl.java
src/main/components/ContactsSplitter.java
src/main/components/ContactsSplitterImpl.java
src/main/components/NameSplitter.java
src/main/components/NameSplitterImpl.java
```

Commit the change with message: `"new package components, datamodel cleanup"`
and show the commit log:

```
283fa35 (HEAD -> f1-refactoring) new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 3. Introduce a Coherent *Component Architecture*

The new package `components` holds interfaces and implementation classes (see above).

A *Software Component Architecture* follows principles:

1. Use of public interfaces (e.g. interface *Calculator.java*).

1. Use of non-public implementation classes (no `public` inside the class)
    in a separate package `components.impl`.

1. Central public *Accessor* with getter methods of singleton instances
    implementing component interfaces.


&nbsp;

Step 1:

Create package `components.impl`. Move all implementation classes into that package.
Make sure classes are not public.

Remove all static *getInstance()* or *create()* methods from remaining interfaces
such that they compile (other classes may not yet compile).


&nbsp;

Step 2:

Create a new interface *"Accessor"* in package `components` providing getter
methods to instances of implementation classes:

```java
package components;

/**
 * Interface with accessor methods to instances implementing
 * {@link component} interfaces.
 */
public interface Accessor {

    /**
     * Public {@link Accessor} instance getter.
     * @return singleton instance that implements the {@link Accessor} interface.
     */
    public static Accessor getInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInstance()'");
    }

    /**
     * Accessor to {@link Calculator} instance.
     * @return {@link Calculator} instance
     */
    public Calculator getCalculator();

    /**
     * Accessor to {@link NameSplitter} instance.
     * @return {@link NameSplitter} instance
     */
    public NameSplitter getNameSplitter();

    /**
     * Accessor to {@link ContactsSplitter} instance.
     * @return {@link ContactsSplitter} instance
     */
    public ContactsSplitter getContactsSplitter();
}
```

Fix remaining code that may not be compiling using the *"Accessor"* interface.

The code base should compile.


&nbsp;

Step 3:

Create a public implementation class *"AccessorImpl.java"* in package `components.impl`
that implements the *"Accessor"* interface.

Implement this class as a *"strict Singleton"* such that getter methods return
references to singleton instances of classes:

- *CalculatorImpl.java*,

- *NameSplitterImpl.java* and

- *ContactsSplitterImpl.java*.

Remove all static instance variables and *getInstance()* methods from implementation
classes.


&nbsp;

Step 4:

Fix in the *getInstance* method in the *Accessor* interface such that it returns
the singleton instance of the implementation class *AccessorImpl.java*:

```java
public interface Accessor {

    /**
     * Public {@link Accessor} instance getter.
     * @return singleton instance that implements the {@link Accessor} interface.
     */
    public static Accessor getInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInstance()'");
    }
    ...
}
```

The code base should compile and work:

```sh
mk clean compile run

find src/main/components
```

Output should show the same tables as before.

```
src/main/components:                <-- all public interfaces
src/main/components/Accessor.java
src/main/components/Calculator.java
src/main/components/ContactsSplitter.java
src/main/components/NameSplitter.java

src/main/components/impl:           <-- non-public implementation classes
src/main/components/impl/AccessorImpl.java
src/main/components/impl/CalculatorImpl.java
src/main/components/impl/ContactsSplitterImpl.java
src/main/components/impl/NameSplitterImpl.java
```

Commit the change with message: `"refactor component architecture"` and show
the commit log:

```
9002b7f (HEAD -> f1-refactoring) refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 4. New Component: *Formatter*

Driver code `RunDatamodelDemo.java` contains methods:

```java
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
    ...
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
    ...
}
```

Remove these methods from the driver code and turn into a new component: *"Formatter"*
in package `components`.

Create the public interface, the non-public implementation class and the accessor
method.

Compile code and run:

```sh
mk clean compile run

find src/main/components
```

Output should show the same tables as before.

```
src/main/components:
src/main/components/Accessor.java
src/main/components/Calculator.java
src/main/components/ContactsSplitter.java
src/main/components/Formatter.java              <-- new public interface
src/main/components/NameSplitter.java

src/main/components/impl:
src/main/components/impl/AccessorImpl.java
src/main/components/impl/CalculatorImpl.java
src/main/components/impl/ContactsSplitterImpl.java
src/main/components/impl/FormatterImpl.java     <-- new implementation class
src/main/components/impl/NameSplitterImpl.java
```

Commit the change with message: `"new component: Formatter"` and show the
commit log:

```
a30a9a7 (HEAD -> f1-refactoring) new component: Formatter
9002b7f refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 5. Data Model with *immutable* Classes

Step 1:

Remove `public` from classes/records in package `datamodel`:

- *Customer.java*,

- *Article.java*,

- *Order.java*.

Code no longer compiles.

These essential data classes will no longer be exposed to other packages in the
code base. Hence, objects can also no longer be created outside package `datamodel`.

Instead, interface definitions of data classes will be used that only provide
read-acces in the new public interface `DataModel`. Understand the concept of
[*sealed-interfaces*](https://www.baeldung.com/java-sealed-classes-interfaces#1-sealed-interfaces)
in Java.

The interface
[*DataModel.java*](src/main/datamodel/DataModel.java)
also provides
[*factory*](https://refactoring.guru/design-patterns/factory-method)
methods for for creating objects:

```java
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
     * @TODO: facade does not coherently use fluid style ({@code getId()}, {@code getName()}, ...).
     * @TODO: facade is not fully immutable ({@code setContacts()}).
     */
    sealed interface Customer permits datamodel.Customer {
        long getId();
        String getName();
        String getFirstNames();
        Customer addContact(String contact);
        String getContacts();
        Customer setContacts(String contacts);  /* not immutable */
        String contact(int i);
        Iterable<String> getContactsAsIterable();
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
```

The factory methods are implemented in non-public class
[*DataFactory.java*](src/main/datamodel/DataFactory.java):

```java
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
                Customer customer = new datamodel.Customer(customerIdCounter++,
                            splitName.name(), splitName.firstNames());
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
    Optional<Article> createArticle(
        String id, String description, long unit_price, Boolean... reduced_VAT)
    {
        return Optional.of(
            new datamodel.Article(id, description, unit_price, reduced_VAT.length > 0?
                reduced_VAT[0].booleanValue() : false)
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
```


&nbsp;

Step 2:

- Insert interface
[*DataModel.java*](src/main/datamodel/DataModel.java) and class
[*DataFactory.java*](src/main/datamodel/DataFactory.java)
into the code base.


Step 3:

- Add `implements DataModel.Article` to class *Article*.

- Add `implements DataModel.Customer` to class *Customer*.

- Add `implements DataModel.Order` to class *Order*.

- Add `implements DataModel.OrderItem` to class *Order.OrderItem*.

- Make all classes *final* (required by *sealed interfaces*).

Make adjustements to match implementation methods and remove unused setter
methods from class *Customer*.


Step 4:

- replace `import datamodel.Customer` with `import datamodel.DataModel.Customer`
    in *NameSplitter.java* and *NameSplitterImpl.java*

Code should now compile, except classes in the `runnables` package.


&nbsp;

Step 5:

Fix classes in the `runnables` package. Replace the old style of creating
objects using *new*:

```java
Customer eric = new Customer("Eric Meyer")
    .setId(100L)
    .addContact("eme@gmail.com")
    .addContact("+49 030 515 141345")
    .addContact("fax: 030 234-134651")  // duplicate entry
    .addContact("fax: 030 234-134651");

var tasse = new Article("SKU-458362", "Tasse", 299);

new Order(8592356245L, eric)
        .addItem(teller, 4)     // + item: 4 Teller, 4x 6.49 € = 25.96 €, 19% MwSt (4.14€)
        .addItem(becher, 8)     // + item: 8 Becher, 8x 1.49 € = 11.92 €, 19% MwSt (1.90€)
        .addItem(buch_OOP, 1)   // + item: 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
        .addItem(tasse, 4),     // + item: 4 Tassen, 4x 2.99 € = 11.96 €, 19% MwSt (1.91€)
```

with using the factory methods:

```java
var eric = DataModel.createCustomer("Eric Meyer")
    .map(c -> c
        .addContact("eme@gmail.com")
        .addContact("+49 030 515 141345")
        .addContact("fax: 030 234-134651")  // duplicate entry
        .addContact("fax: 030 234-134651")
    );

var tasse = DataModel.createArticle("SKU-458362", "Tasse", 299);

DataModel.createOrder(8592356245L, eric)
    .map(o -> o
        .addItem(teller, 4)     // + item: 4 Teller, 4x 6.49 € = 25.96 €, 19% MwSt (4.14€)
        .addItem(becher, 8)     // + item: 8 Becher, 8x 1.49 € = 11.92 €, 19% MwSt (1.90€)
        .addItem(buch_OOP, 1)   // + item: 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
        .addItem(tasse, 4)      // + item: 4 Tassen, 4x 2.99 € = 11.96 €, 19% MwSt (1.91€)
    ),
```

Change imports from:

```java
import datamodel.Currency;
import datamodel.Article;       // no longer visible
import datamodel.Customer;      // no longer visible
import datamodel.Order;         // no longer visible
```

to:

```java
import datamodel.Currency;
import datamodel.DataModel;
import datamodel.DataModel.Article;     // use facade interface instead
import datamodel.DataModel.Customer;    // use facade interface instead
import datamodel.DataModel.Order;       // use facade interface instead
```


Step 6:

Fix the creation of *Customer*, *Article* and *Order* lists:

```java
final List<Customer> customers = List.of(eric, anne, tim, nadine, khaled, lena)
    .stream()
    .flatMap(Optional::stream)
    .toList();

final List<Article> articles = List.of(
        tasse, becher, kanne, buch_Java, buch_OOP, pfanne, fahrradhelm, fahrradkarte, radio
    ).stream()
    .flatMap(Optional::stream)
    .toList();

final List<DataModel.Order> orders = List.of(

    /* orders */

    ).stream()
    .flatMap(Optional::stream)
    .toList();
```


&nbsp;

Code should now compile and produce the same tables as before:

```sh
mk clean compile run
```

If working, commit the change with message: `"add DataModel.java, DataFactory.java"`
and show the commit log:

```
8c0b6f0 (HEAD -> f1-refactoring) add DataModel.java, DataFactory.java
a30a9a7 new component: Formatter
9002b7f refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 6. *Fluid* style in *Customer*

Adjust methods in interface `DataModel.Customer` to the *fluid* style by removing
*get* and *set* prefixes. Use your IDE to perform this refactoring step in order
to propagate the adjustments throughout the code base.

The updated interface should be:

```java
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
```

&nbsp;

After this refactoring step, code should compile and produce the same tables as before:

```sh
mk clean compile run
```

If working, commit the change with message: `"fixed fluid-style in DataModel.Customer"`
and show the commit log:

```
8c0b6f0 (HEAD -> f1-refactoring) add DataModel.java, DataFactory.java
a30a9a7 new component: Formatter
9002b7f refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 7. Refactor *Unit Tests*

Changes in this refactoring profoundly impact tests. Not only adjustments are
needed, certain tests are no longer feasible (setter tests).

Review unit tests and perform necessary adjustments, mainly replacing direct
access to datamodel classes with interfaces and replacing the creation of the
test object with *new* with factory methods:


```java
import datamodel.DataModel.Customer;

/**
 * Basic tests for class {@link Customer}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Customer_0_BaseTests {

    /*
     * Test object.
     */
    // private final Customer eric = new Customer();
    private final Customer eric = DataModel.createCustomer("Eric Meyer").get();

    // ...
}
```

Remove test-methods with setter tests. They are no longer needed.

Run tests in the IDE.

Test `010` *test_010_initialization()* fails since *Customer* `id` are no longer
initialized with `-1L`. Adjust such that the test expects an initial `id > 0`.

```java
/**
 * Test 010 validates the {@link Customer} object initialization.
 */
@Test @Order(010)
public void test_010_initialization() {
    long expectedId = -1L;
    long actualId = eric.id();
    assertEquals(expectedId, actualId);
    // assertTrue(eric.id() > 0L);
    // 
    // test remaining attributes for initial values
    assertEquals("", eric.name());          // -> "Meyer"
    assertEquals("", eric.firstNames());    // -> "Eric
    assertEquals("", eric.contacts());
}
```


&nbsp;

Compile and run tests:

```sh
mk clean compile compile-tests run-tests
```

Number of tests is now 40:

```
╷
├─ JUnit Jupiter ✔
│  ├─ Customer_0_BaseTests ✔
│  │  ├─ test_010_initialization() ✔
│  │  ├─ test_050_contacts_getter_setter() ✔
│  │  └─ test_061_chainable_methods_contacts() ✔
│  ├─ Application_0_always_pass_Tests ✔
│  │  ├─ test_001_always_pass() ✔
│  │  └─ test_002_always_pass() ✔
│  ├─ CalculatorTests ✔
│  │  ├─ test_010_includedVAT_case100() ✔
│  │  ├─ test_011_includedVAT_regular() ✔
│  │  ├─ test_020_includedVAT_rounding() ✔
│  │  ├─ test_021_includedVAT_rounding() ✔
│  │  ├─ test_022_includedVAT_rounding_extreme() ✔
│  │  ├─ test_030_negative_gross() ✔
│  │  ├─ test_031_negative_rate() ✔
│  │  ├─ test_040_corner_cases() ✔
│  │  └─ test_041_corner_cases_extreme() ✔
│  ├─ Customer_2_ContactsTests ✔
│  │  ├─ test_200_contact_initialization() ✔
│  │  ├─ test_210_single_contact() ✔
│  │  ├─ test_220_multiple_contacts() ✔
│  │  ├─ test_221_multiple_contacts_list() ✔
│  │  └─ test_230_out_of_bounds_contacts() ✔
│  ├─ Customer_3_NamesTests ✔
│  │  ├─ test_300_name_in_order() ✔
│  │  ├─ test_301_name_in_order_double_first() ✔
│  │  ├─ test_302_name_in_order_triple_first() ✔
│  │  ├─ test_303_name_in_order_dashed_last() ✔
│  │  ├─ test_304_name_in_order_triple_dashed_last() ✔
│  │  ├─ test_305_name_in_order_dashed_first() ✔
│  │  ├─ test_306_many_names_in_order() ✔
│  │  ├─ test_307_many_names_in_order() ✔
│  │  ├─ test_309_many_names_in_order() ✔
│  │  ├─ test_310_last_name_first_comma() ✔
│  │  ├─ test_311_last_name_first_semicolon() ✔
│  │  ├─ test_312_last_name_with_dash() ✔
│  │  ├─ test_313_last_name_with_dash() ✔
│  │  ├─ test_314_many_last_names() ✔
│  │  ├─ test_320_spaces() ✔
│  │  ├─ test_321_quotes() ✔
│  │  ├─ test_322_double_quotes() ✔
│  │  ├─ test_323_whitespaces() ✔
│  │  └─ test_330_extreme_long_names() ✔
│  └─ Customer_1_SanityTests ✔
│     ├─ test_140_empty_contacts() ✔
│     └─ test_141_null_contacts() ✔
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 523 ms
[        40 tests successful      ]
[         0 tests failed          ]
```

If tests are passing, commit with message: `"refactoring unit tests"`
and show the commit log:

```
cfcc661 (HEAD -> f1-refactoring) refactoring unit tests
7b7a0f4 fixed fluid-style in DataModel.Customer
8c0b6f0 add DataModel.java, DataFactory.java
a30a9a7 new component: formatter
9002b7f refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 8. Print detailed *Order* Table

The *Order* table so far has only shown one line per order ommitting details
such as actual ordered items.

Include the final version of file
[*RunDatamodelDemo.java*](src/main/runnables/RunDatamodelDemo.java)
into your code base.

More calculation methods are needed in interface
[*Calculator.java*](src/main/components/Calculator.java):

```java
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
```

Implement methods and run the code.


&nbsp;

Compile and run the code and tests:

```sh
mk clean compile run

mk compile-tests run-tests
```

The detailed *Order* table should appear:

```
Orders:
+-------------------------------+---------+----------+----------+-------------+
| ORDER                         |    MwSt*|    Preis |     MwSt |      Gesamt |
+-------------------------------+---------+----------+----------+-------------+
| OID:8592356245, CID:1000      |         |          |          |             |
| Eric Meyer                    |         |          |          |             |
| - 4 Teller, 4x 6.49€          |    4.14 |    25.96 |          |             |
| - 8 Becher, 8x 1.49€          |    1.90 |    11.92 |          |             |
| - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |          |             |
| - 4 Tasse, 4x 2.99€           |    1.91 |    11.96 |    13.18 |  129.79 EUR |
+-------------------------------+---------+----------+----------+-------------+
| OID:3563561357, CID:1001      |         |          |          |             |
| Anne Bayer                    |         |          |          |             |
| - 2 Teller, 2x 6.49€          |    2.07 |    12.98 |          |             |
| - 2 Tasse, 2x 2.99€           |    0.95 |     5.98 |     3.02 |   18.96 EUR |
+-------------------------------+---------+----------+----------+-------------+
| OID:5234968294, CID:1000      |         |          |          |             |
| Eric Meyer                    |         |          |          |             |
| - 1 Kanne, 1x 19.99€          |    3.19 |    19.99 |     3.19 |   19.99 EUR |
+-------------------------------+---------+----------+----------+-------------+
| OID:6135735635, CID:1003      |         |          |          |             |
| Nadine-Ulla Blumenfeld        |         |          |          |             |
| - 12 Teller, 12x 6.49€        |   12.43 |    77.88 |          |             |
| - 1 Buch "Java", 1x 49.90€    |    3.26*|    49.90 |          |             |
| - 1 Buch "OOP", 1x 79.95€     |    5.23*|    79.95 |    20.92 |  207.73 EUR |
+-------------------------------+---------+----------+----------+-------------+
| OID:6173043537, CID:1005      |         |          |          |             |
| Lena Neumann                  |         |          |          |             |
| - 1 Buch "Java", 1x 49.90€    |    3.26*|    49.90 |          |             |
| - 1 Fahrradkarte, 1x 6.95€    |    0.45*|     6.95 |     3.71 |   56.85 EUR |
+-------------------------------+---------+----------+----------+-------------+
| OID:7372561535, CID:1000      |         |          |          |             |
| Eric Meyer                    |         |          |          |             |
| - 1 Fahrradhelm, 1x 169.00€   |   26.98 |   169.00 |          |             |
| - 1 Fahrradkarte, 1x 6.95€    |    0.45*|     6.95 |    27.43 |  175.95 EUR |
+-------------------------------+---------+----------+----------+-------------+
| OID:4450305661, CID:1000      |         |          |          |             |
| Eric Meyer                    |         |          |          |             |
| - 3 Tasse, 3x 2.99€           |    1.43 |     8.97 |          |             |
| - 3 Becher, 3x 1.49€          |    0.71 |     4.47 |          |             |
| - 1 Kanne, 1x 19.99€          |    3.19 |    19.99 |     5.33 |   33.43 EUR |
+-------------------------------+---------+----------+----------+-------------+
                                             Gesamt: |    76.78 |  642.70 EUR |
                                                     +==========+=============+
```

If the detailed *Order* table appears, commit with message:
`"detailed order table"` and show the commit log:

```
7d2226c (HEAD -> f1-refactoring) detailed order table
cfcc661 refactoring unit tests
7b7a0f4 fixed fluid-style in DataModel.Customer
8c0b6f0 add DataModel.java, DataFactory.java
a30a9a7 new component: formatter
9002b7f refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 9. Refactor *TableFormatter* as Component

*TableFormatter.java* has been used to format tables. It currently resides in
package `runnables`.

Perform these steps:

1. Move *TableFormatter.java* to package `components.impl`.

1. Rename to `TableFormatterImpl.java` (it becomes the implementation class).

1. Remove `public` from the class.

1. Add a new interface `TableFormatter.java` to package `components`.

1. Re-engineer needed methods of class *TableFormatter* and inner
    class *TableFormatterBuilder* for this interface.

1. Make implementation class `TableFormatterImpl.java` implement the interface
    *TableFormatter* and inner class *TableFormatterBuilderImpl* implement
    the interface *TableFormatterBuilder*.

1. Make classes in package `runnables` compile with the new interface.


&nbsp;

*TableFormatter* implements the *Builder* pattern, which shifts the
implementation as a component. While other components exist as singleton
instances, the *Builder* pattern requires new instances of the
*builder class* being created for each table.

This means that *TableFormatterImpl* does not exist as singleton instance.
Instead, the *builder*-method is exposed in *Accessor*:

```java
package components;

import components.TableFormatter.TableFormatterBuilder;

/**
 * Interface with accessor methods to instances implementing
 * {@link component} interfaces.
 */
public interface Accessor {

    /**
     * Factory method for {@link TableFormatterBuilder} instances.
     * @return new {@link TableFormatterBuilder} instance
     */
    public TableFormatterBuilder builder();
}
```

Complete the implementation. 


&nbsp;

Compile and run the code and tests:

```sh
mk clean compile run

mk compile-tests run-tests
```

If the detailed *Order* table appears and all tests pass,
verify the code base:

```sh
find src
```
```
src/main/application:
src/main/application/Application.java
src/main/application/ApplicationContext.java
src/main/application/package-info.java
src/main/application/Runnable.java

src/main/components:    <-- public component interfaces
src/main/components/Accessor.java
src/main/components/Calculator.java
src/main/components/ContactsSplitter.java
src/main/components/Formatter.java

src/main/components/impl:
src/main/components/impl/AccessorImpl.java  <-- only public class
src/main/components/impl/CalculatorImpl.java
src/main/components/impl/ContactsSplitterImpl.java
src/main/components/impl/FormatterImpl.java
src/main/components/impl/NameSplitterImpl.java
src/main/components/impl/TableFormatterImpl.java
src/main/components/NameSplitter.java
src/main/components/TableFormatter.java

src/main/datamodel:
src/main/datamodel/Article.java     <-- non-public
src/main/datamodel/Currency.java    <-- public
src/main/datamodel/Customer.java    <-- non-public
src/main/datamodel/DataFactory.java <-- public factory
src/main/datamodel/DataModel.java   <-- public data model
src/main/datamodel/Order.java       <-- non-public
src/main/module-info.java

src/main/runnables:             <-- 'TableFormatter' moved to 'components'
src/main/runnables/RunCustomerDemo.java
src/main/runnables/RunDatamodelDemo.java

src/resources:
src/resources/application.properties
src/resources/log4j2.properties
src/resources/META-INF
src/resources/META-INF/MANIFEST.MF

src/tests:
src/tests/application
src/tests/application/Application_0_always_pass_Tests.java
src/tests/datamodel
src/tests/datamodel/CalculatorTests.java
src/tests/datamodel/Customer_0_BaseTests.java
src/tests/datamodel/Customer_1_SanityTests.java
src/tests/datamodel/Customer_2_ContactsTests.java
src/tests/datamodel/Customer_3_NamesTests.java
```

If you have this structure, commit with message:
`"refactor TableFormatter as component"` and show the commit log:

```
cba9a51 (HEAD -> f1-refactoring) refactor TableFormatter as component
7d2226c detailed order table
cfcc661 refactoring unit tests
7b7a0f4 fixed fluid-style in DataModel.Customer
8c0b6f0 add DataModel.java, DataFactory.java
a30a9a7 new component: formatter
9002b7f refactor component architecture
283fa35 new package components, datamodel cleanup
15d342a merge --squash c3-datamodel
f9d1236 (tag: base, main) add src/main, src/resources, src/tests
...
```

This completes the refactoring with 9 major steps and commits made
on branch *f1-refactoring*.
