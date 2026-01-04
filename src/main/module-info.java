/**
 * Module <i>Ordering_System</i> of the <i>Software Engineering I</i> course.
 * <p>
 * See Jakob Jenkov's article about <i>Java Modules</i>:
 * <a href="https://jenkov.com/tutorials/java/modules.html"><i>Java Modules</i></a>.
 * </p><p>
 * File {@code module-info.java} indicates a <i>modular</i> Java project. It
 * includes the module name: {@link Ordering_System}, external modules required
 * by this module and packages exported to other modules.
 * Opening a package makes it accessible to tools such as the JUnit test runner.
 * Javadoc requires packages open or exported.
 * </p><p>
 * Locations of <i>required</i> modules are provided by the {@code MODULEPATH}
 * environment variable.
 * </p>
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
module Ordering_System {

    /*
     * {@code exports} makes package {@code application} accessible to other modules
     * at compile and at runtime (use {@code open} for compile-time access only).
     */
    exports application;
    exports datamodel;

    /* {@code opens} makes a package accessible to tools such as the JUnit test
     * runner and the Javadoc compiler.
     */
    opens application;

    /*
     * External module required by this module (JUnit-5 module for JUnit testing).
     */
    requires org.junit.jupiter.api;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires lombok;
}
