package application;

import java.util.*;
import org.apache.logging.log4j.*;

import application.runnables.*;

/**
 * Class with static {@code main()}-method that starts the program.
 * <p>
 * Creates instance of itself and of class {@link ApplicationContext},
 * where it invokes the {@code startup(args, log)} method. During
 * startup, the {@code log4j2} logging system is initialized and file
 * {@code 'application.properties'} is located on the {@code CLASSPATH}
 * and loaded into {@link Properties}.
 * <p>
 * Then, method {@code run(ApplicationContext context)} is invoked on
 * instances that implement the {@link Runnable} interface.
 */
public class Application {

    /**
     * Container holding objects shared accross the application
     */
    private final ApplicationContext context;


    /**
     * Private constructor.
     */
    private Application() {
        this.context = new ApplicationContext(this);
        this.context.log().trace("Application instance created");
    }

    /**
     * Static {@code main()} - method as entry point for the Java VM.
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {
        var application = new Application();
        var context = application.context;
        Logger log = context.log();
        if(context.startup(args)) {
            // application.run();
            // 
            log.trace("Application.context startup() passed");
            // 
            // print greeting message
            if( ! context.matchProperty("application.greeting", "false")) {
                String module = Optional.ofNullable(Application.class.getModule().getName()).map(m -> " (modular)").orElse("");
                String name = Optional.ofNullable((String)context.properties().get("application.name")).orElse("program");
                String msg = String.format("%s%s", name, module);
                context.log().trace("print greeting message: " + msg);
                System.out.println(msg);
            }
            // 
            // launch runnables found under the {@code 'application.run'} property
            for(String rn : Optional.ofNullable((String)context.properties().get("application.run"))
                .orElseGet(() -> "no property").split("[,;]")
            ) {
                rn = rn.trim();
                if(rn.contains(" ")) {
                    context.log().info(String.format("no property 'application.run' found in '%s'", context.propertyFile()));
                } else {
                    Runnable runnable = null;
                    switch(rn) {
                        case "RunCustomerDemo": runnable = new RunCustomerDemo(); break;
                    }
                    if(runnable != null) {
                        context.log().info(String.format("executing: '%s.class'", runnable.getClass().getSimpleName()));
                        runnable.run(context);
                    } else {
                        context.log().info(String.format("no runnable class: '%s' found from 'application.run'", rn));
                    }
                }
            }
        // 
        } else {
            application.context.log().error("Application.context startup() failed");
        }
    }
}
