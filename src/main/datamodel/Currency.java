package datamodel;

/**
 * {@link Currency} is a standardized unit to express monetary value
 * in a currency system.
 */
public enum Currency {

    /** United States Dollar */ USD (/* $ */  "$", 0x0024),

    /** European Euro */        EUR (/* € */  "\u20AC", 0x20AC),

    /** Swiss Franc */          CHF (/* CHF */"\u20A3", 0x20A3),

    /** British Pound */        GBP (/* £ */  "\u00A3", 0x00A3),

    /** Norwegian Krone */      NOK (/* kr */ "kr", 0),

    /** Polish Zloty */         PLZ (/* zł */ "z\u0142", 0), // 'ł': U+0142; 'Ł': U+0141

    /** Bitcoin */              BTC (/* ₿ */  "\u20BF", 0x20BF),
    ;

    /**
     * Currency marking, e.g. "€" or "kr".
     */
    private final String marking;

    /**
     * Currency symbol in utf8 (0 if no currency symbol exists).
     */
    private final int utf8;

    /**
     * Constructor.
     * @param marking currency marking, e.g. "€" or "kr"
     * @param utf8 currency symbol utf8 (0 if not existing)
     */
    Currency(String marking, int utf8) {
        this.marking = marking;
        this.utf8 = utf8;
    }

    /**
     * Return currency marking, e.g. {@code "€"} or {@code "kr"}.
     * @return currency marking, e.g. "€" or "kr"
     */
    public String marking() { return marking; }

    /**
     * Return {@code utf8} of currency symbol or 0 if no utf8 symbol exists.
     * @return {@code utf8} of currency symbol or 0 if no utf8 symbol exists
     */
    public int utf8() { return utf8; }

    /**
     * Return three-letter currency code, e.g. {@code "EUR"} or {@code "NOK"}.
     * @return three-letter currency code
     */
    public String code() { return this.name(); }

    public int style(int a, int b) {
        return this==PLZ || this==NOK? b : a;
    }
}
