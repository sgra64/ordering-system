package application;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;

/**
 * Application class with a {@code main()} - method that starts the program.
 *
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Application {

    /**
     * Name of application properties file.
     */
    private final String propertyFile = "application.properties";

    /**
     * Application properties file
     */
    private final Properties p = new Properties();

    /**
     * Create Logger instance with name 'app-logger'.
     */
    private final Logger log = LogManager.getLogger("app-logger");

    /**
     * Arguments passed from command line or from 'application.args' property.
     */
    private String[] args;


    /**
     * {@code main()} - method as entry point for the Java VM.
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {
        var application = new Application();
        if(application.startup(args)) {
            application.run(application.args);
        }
    }

    /**
     * Run-method called after {@link Application} instance has been created.
     * @param args arguments passed from command line
     */
    public void run(String[] args) {
        // 
        Arrays.stream(args)
            .map(arg -> String.format(" - arg: %s", arg))
            .forEach(System.out::println);
    }

    /**
     * Startup program reading file 'application.properties' into {@link Properties},
     * performing test messages for the logging system and printing a greeting message.
     * @param clargs command line arguments
     * @return true if startup succeeded
     */
    private boolean startup(String[] clargs) {
        boolean succeeded = true;
        this.args = Arrays.asList(clargs).stream()  // VSCodeRunner adds active file to args[]
            // remove if detected by the path starting with "c:\path" or "/path"
            .filter(arg -> ! (arg.length() > 30 && (arg.startsWith("/") || arg.charAt(1)==':')))
            .toArray(String[]::new);
        // 
        // attempt to read propertyFile from location on the CLASSPATH
        try (InputStream is = Optional.ofNullable(
            this.getClass().getClassLoader().getResourceAsStream(propertyFile)).orElseThrow(() ->
                new IOException(String.format("could not find property file: '%s'", propertyFile)));
        ) {
            p.load(is);     // load 'application properties' file
            log.trace(String.format("found '%s' with %d properties", propertyFile, p.size()));
            // 
            greeting();
            // 
            // prioritize command line (CL) args, use 'application.args' if no CL-args are present
            if(this.args.length==0) {
                this.args = Optional.ofNullable(p.getProperty("application.args"))
                    .map(pargs -> {
                        List<String> argl = Arrays.asList(pargs.split("[\\s,;]+"));
                        int size = argl.size();
                        return size==0? this.args : argl.toArray(new String[size]);
                    }).orElse(args);
            }
            loggingTest();
        // 
        } catch (IOException e) {
            succeeded = false;
            log.error(e.getMessage());      // log error message
            greeting();     // still print greeting message
        }
        return succeeded;
    }

    /**
     * Print greeting if property "application.greeting" is not set to "false".
     */
    private void greeting() {
        if( ! matches("application.greeting", "false")) {
            String module = Optional.ofNullable(Application.class.getModule().getName()).map(m -> " (modular)").orElse("");
            String name = Optional.ofNullable((String)p.get("application.name")).orElse("program");
            System.out.println(String.format("Hello, %s%s", name, module));
        }
    }

    /**
     * Issue log messages of levels: {@code FATAL, ERROR, WARN, INFO, DEBUG, TRACE}
     * if property "application.log-test" is set to "true".
     */
    private void loggingTest() {
        if(matches("application.log-test", "true")) { 
            log.fatal("log-test message at level: FATAL");
            log.error("log-test message at level: ERROR");
            log.warn("log-test message at level: WARNING");
            log.info("log-test message at level: INFO");
            log.debug("log-test message at level: DEBUG");
            log.trace("log-test message at level: TRACE");
        }
    }

    /**
     * Match value of a property found in 'application.properties'.
     * @param property property to match
     * @param value property value to match, {@code "*"} matches any value
     * @return true if property value matches
     */
    private boolean matches(String property, String value) {
        return Optional.ofNullable(p.getProperty(property))
                .map(v -> value.equals("*") || v.toLowerCase().equals(value)).orElse(false);
    }
}
