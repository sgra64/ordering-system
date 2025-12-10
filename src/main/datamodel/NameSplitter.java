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
        // 
        // replace implementation with actual NameSplitterImpl singleton instance:
        return new NameSplitter() {
            @Override
            public Optional<SplitName> split(String name) {
                return Optional.of(new SplitName(name, ""));
            }
        };
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
