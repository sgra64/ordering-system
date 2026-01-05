package application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface with method {@code run(ApplicationContext context)}
 * that can be configured as {@code 'application.run'} property
 * in {@code 'application.properties'}.
 */
public interface Runnable {

    /**
     * Method invoked with {@link ApplicationContext} containing {@code args[]}
     * and {@link java.util.Properties} obtained from {@code 'application.properties'}.
     * 
     * @param context {@link ApplicationContext} passed to {@link Runnable} instance
     */
    void run(ApplicationContext context);

    /**
     * Define {@code @Runnable} annotation.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Accessors {
        public int priority() default -1;
    }
}
