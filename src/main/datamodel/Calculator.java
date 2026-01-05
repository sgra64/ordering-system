package datamodel;

/**
 * {@link Calculator} performs price and tax calculations.
 */
public interface Calculator {

    /**
     * Create new {@link Calculator} for {@link Pricing}.
     * @param pricing {@link Pricing} on which {@link Calculator} is based
     * @return new {@link Calculator} instance
     */
    public static Calculator create() {
        // 
        // @TODO: replace with instance of {@link Calculator} implementation class
        return new Calculator() {

            @Override
            public long includedVAT(long grossValue, double rate) {
                return 0L;
            }
        };
    }

    /**
     * Calculate the {@link VAT} included in a gross value.
     * @param grossValue value with included {@link VAT}
     * @param rate {@link VAT} rate in percent, e.g. {@code 0.19} for {@code 19%}
     * @return {@link VAT} included in gross value
     */
    long includedVAT(long grossValue, double rate);
}
