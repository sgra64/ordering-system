package application;

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
     * @return chainable self-reference
     */
    Runnable run(ApplicationContext context);
}
