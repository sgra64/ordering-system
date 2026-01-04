package datamodel;

import java.util.Optional;

/**
 * {@link NameSplitter} splits a <i>single-String</i> name into last- and
 * first name parts returning as {@link SplitName}.
 * <p>
 * Name splitting follows rules:
 * <ul>
 * <li> If a name contains no seperators (comma or semicolon {@code [,;]}),
 *      trailing consecutive parts form the last name, while prior parts
 *      form the first name, e.g. {@code "Tim Anton Schulz-Müller"}, splits
 *      into <i>first name:</i> {@code "Tim Anton"} and <i>last name</i>
 *      {@code "Schulz-Müller"}.
 * <li> Names with seperators (comma or semicolon {@code [,;]}) split into
 *      a last name part before the seperator and a first name part after
 *      the seperator, e.g. {@code "Schulz-Müller, Tim Anton"} splits into
 *      <i>first name:</i> {@code "Tim Anton"} and <i>last name</i>
 *      {@code "Schulz-Müller"}.
 * <li> Leading and trailing white spaces {@code [\s]}, commata {@code [,;]}
 *      and quotes {@code ["']} are trimmed from names, e.g.
 *      {@code "  'Schulz-Müller, Tim Anton'    "}.
 * <li> White spaces between name parts are trimmed as well, e.g.
 *      {@code "Schulz-Müller, <white-spaces> Tim <white-spaces> Anton
 *      <white-spaces> "}.
 * </ul>
 * <pre>
 * Examples:
 * +--------------------------------+----------------------+-----------------+
 * |Single-String name              |first name parts      |last name parts  |
 * +--------------------------------+----------------------+-----------------+
 * |"Eric Meyer"                    |"Eric"                |"Meyer"          |
 * |"Meyer, Anne"                   |"Anne"                |"Meyer"          |
 * |"Meyer; Anne"                   |"Anne"                |"Meyer"          |
 * |"Tim Schulz‐Mueller"            |"Tim"                 |"Schulz‐Mueller" |
 * |"Nadine Ulla Blumenfeld"        |"Nadine Ulla"         |"Blumenfeld"     |
 * |"Nadine‐Ulla Blumenfeld"        |"Nadine‐Ulla"         |"Blumenfeld"     |
 * |"Khaled Saad Mohamed Abdelalim" |"Khaled Saad Mohamed" |"Abdelalim"      |
 * +--------------------------------+----------------------+-----------------+
 * 
 * Trim leading, trailing and interim white spaces and quotes:
 * +--------------------------------+----------------------+-----------------+
 * |" 'Eric Meyer'  "               |"Eric"                |"Meyer"          |
 * |"Nadine     Ulla    Blumenfeld" |"Nadine Ulla"         |"Blumenfeld"     |
 * +--------------------------------+----------------------+-----------------+
 * </pre>
 */
public interface NameSplitter {

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

    /**
     * Static getter of <i>Singleton</i> instance of implementation class.
     * @return reference to <i>Singleton</i> instance of implementation class
     */
    public static NameSplitter getInstance() {
        // 
        // replace with actual NameSplitterImpl.getInstance();
        NameSplitter instance = null; // NameSplitterImpl.getInstance();
        // 
        return Optional.ofNullable(instance).orElse(
                // return mock instance, if no actual instance is provided
                new NameSplitter() {
                    // 
                    @Override public Optional<SplitName> split(String name) {
                        return Optional.of(new SplitName(name, "-"));
                    }
                }
            );
    }
}
