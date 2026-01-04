package runnables;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * Formatter for tablular content. Content can be fitted into a table
 * with header, separator lines and content rows.
 * <p>
 * For example:
 * <pre>
 * +------+----------------+----------------+------------------------+
 * |   ID | NAME           | FIRSTNAME      | CONTACT                |
 * +------+----------------+----------------+------------------------+
 * |  100 | Meyer          | Eric           | eme22@gmail.com        |
 * |  101 | Tina           | Sommer         | +49 030 22458 29425    |
 * |  102 | Tim            | Schulze        | +49 171 2358124        |
 * +------+----------------+----------------+------------------------+
 * </pre>
 * {@code TableFormatter.builder()} is used to specify an build the table,
 * for example:
 * <pre>
 * var tableFormatter = TableFormatter.builder()
 *    .columns("| ID | NAME | FIRSTNAME | CONTACT |")
 *    .widths(6, 16, 16, 24)    // column widths
 *    .alignments("R")          // column alignments
 *    // 
 *    .rowMapper(Customer.class, c ->
 *        new String[] {Long.toString(c.id()), c.name(), c.firstName(), c.contact())
 *    // 
 *    .build();
 * </pre>
 * The table then can be filled with rows:
 * <pre>
 * Customer c1 = new Customer(100L, "Eric", "Meyer", "eme22@gmail.com");
 * Customer c2 = new Customer(101L, "Sommer", "Tina", "+49 030 22458 29425");
 * Customer c3 = new Customer(102L, "Schulze", "Tim", "+49 171 2358124");
 * // 
 * // print 'Customer' table
 * tableFormatter
 *    .header()
 *        .row(c1)
 *        .row(c2)
 *        .row(c3)
 *    .footer()
 *    .print(System.out);
 * </pre>
 * Uses {@code lombok's @Builder} pattern.
 */
@Builder
@AllArgsConstructor
public class TableFormatter {

    /**
     * List of {@link Column} instances.
     */
    private final List<Column> columns;

    /**
     * Row mappers map objects of type {@code <T>>} to {@code String[]} fields
     * that corresond to consecutive fields in one table row.
     * <pre>
     * +------+----------------+--------------------+----------------------------+
     * |  100 | Meyer          | Eric               | +49 030 515 141345         |
     * +------+----------------+--------------------+----------------------------+
     * </pre>
     */
    private final Map<Class<?>, Function<Object, String[]>> rowMappers;

    /**
     * Multi-row mappers map objects of type {@code <T>} to multiple table rows,
     * e.g. to include multiple contacts.
     * <pre>
     * +------+----------------+--------------------+----------------------------+
     * |  100 | Meyer          | Eric               | +49 030 515 141345         |
     * |      |                |                    | eme@gmail.com              |
     * |      |                |                    | fax: 030 234-134651        |
     * +------+----------------+--------------------+----------------------------+
     * </pre>
     */
    private final Map<Class<?>, Function<Object, String[][]>> multiRowMappers;


    /**
     * Internal buffer to collect formatted table content.
     */
    private final StringBuilder sb = new StringBuilder();


    /**
     * Format table header with column lables. Example:
     * <pre>
     * +------+----------------+----------------+------------------------+
     * |   ID | NAME           | FIRSTNAME      | CONTACT                |
     * +------+----------------+----------------+------------------------+
     * </pre>
     * @return chainable self-reference
     */
    public TableFormatter header() {
        return line().row(
                // create all-columns args[] with: ["{label}", "{label}", ...]
                IntStream.range(0, columns.size()).boxed().map(i -> "{label}").toArray(String[]::new)
            ).line();
    }

    /**
     * Format table footer with closing line. Example:
     * <pre>
     * +------+----------------+----------------+------------------------+
     * </pre>
     * @return chainable self-reference
     */
    public TableFormatter footer() {
        return line();
    }

    /**
     * Format line with optional {@code markers} for line segments.
     * {@code Markers} are those described for {@link row} method.
     * @param markers optional {@code markers} for line segments
     * @return chainable self-reference
     */
    public TableFormatter line(String... markers) {
        // 
        return markers.length > 0? row(markers) :
            // create all-columns args[] with: ["{---}", "{---}", ...]
            row(IntStream.range(0, columns.size()).boxed()
                .map(i -> "{---}").toArray(String[]::new));
    }

    /**
     * Format collection of objects as rows in the table.
     * @param mappedObjectsCollection object mapped to rows
     * @param seperatorLine append optional seperator lines between objects
     * @return
     */
    public TableFormatter row(Collection<?> mappedObjectsCollection, Boolean... seperatorLine) {
        if(mappedObjectsCollection != null) {
            for(var mappedObject : mappedObjectsCollection) {
                row(mappedObject, seperatorLine);
            }
        }
        return this;
    }

    /**
     * Format object as row in the table using a <i>rowMapper</i> registered
     * for the object class. Example:
     * <pre>
     * |  100 | Meyer          | Eric           | eme22@gmail.com        |
     * </pre>
     * @param mappedObject object mapped to a row
     * @param seperatorLine append optional seperator line
     * @return chainable self-reference
     */
    public TableFormatter row(Object mappedObject, Boolean... seperatorLine) {
        boolean sepLine = seperatorLine.length > 0? seperatorLine[0] : false;
        if(mappedObject != null && rowMappers != null) {
            for(Class<?> key : rowMappers.keySet()) {
                // also match implementing interfaces such as 'ResultSet'
                if(key.isAssignableFrom(mappedObject.getClass())) {
                    row(rowMappers.get(key).apply(mappedObject));
                    if(sepLine) line();
                }
            }
        }
        if(mappedObject != null && multiRowMappers != null) {
            for(Class<?> key : multiRowMappers.keySet()) {
                // also match implementing interfaces such as 'ResultSet'
                if(key.isAssignableFrom(mappedObject.getClass())) {
                    for(var row : multiRowMappers.get(key).apply(mappedObject)) {
                        row(row);   // output each row
                    }
                    if(sepLine) line();
                }
            }
        }
        return this;
    }

    /**
     * Format row from {@code values} putting the {@code i-th} value into the
     * {@code i-th} column.
     * <ul>
     * <li>value: {@code "text"} put text in cell with separators,</li>
     * <li>value: {@code " "} format empty cell with separators,</li>
     * <li>value: {@code ""} format empty cell with no separators.</li>
     * </ul>
     * {@code Markers} can prepend values:
     * <ul>
     * <li>value: {@code "{label}"} format cell with column label with separators,</li>
     * <li>value: {@code "{---}"} fill cell as dashed-line with {@code "+"} separators,</li>
     * <li>value: {@code "{R}text"} format cell with right alignment with no separators,</li>
     * <li>value: {@code "{R }text"} format cell with right alignment with separators,</li>
     * <li>value: {@code "{L}text"}, {@code "{L }text"} accordingly with left alignment.</li>
     * </ul>
     * @param values to output as row
     * @return chainable self-reference
     */
    public TableFormatter row(String... values) {
        String psep = "";
        for(int i=0; i < columns.size() && i < values.length; i++) {
            final var col = columns.get(Math.min(i, columns.size()-1));
            final var w = col.width();
            final var fill = " ";
            final String value = i < values.length && values[i] != null? values[i] : "";
            String val = value;                     // mutable value
            var lmarg = col.lmarg();                // mutable left-margin
            var rmarg = col.rmarg();                // mutable right-margin
            var orientation = col.orientation();    // mutable cell orientation
            var sep = psep.equals("+")? "+" : String.valueOf(col.sep());
            sep = psep.equals(" ") && val.length()==0? " " : sep;
            psep = "";  // separator of previous column
            // 
            if(val.startsWith("{")) {
                int end = val.indexOf("}");
                if(end > 1) {
                    String marker = val.substring(1, end);
                    val = val.substring(end + 1);
                    if(marker.length()==0) {
                        val = "";   // empty line
                    } else {
                        if("label".equals(marker)) { val = col.label(); }
                        if("---".equals(marker) || "+".equals(marker)) {
                            lmarg = rmarg = 0;
                            psep = sep = "+";
                            val = marker.charAt(0)=='+'? "" : "-".repeat(w);
                        }
                        if(marker.startsWith("L")) { orientation = 'L'; }
                        if(marker.startsWith("R")) { orientation = 'R'; }
                        if(marker.contains(" ")) { psep = sep = " "; }
                    }
                }
            }
            if(val.length()==0) {       // empty cell
                lmarg = rmarg = 0;
                String prev = values[Math.max(0, Math.min(values.length-1, i-1))];
                if(prev==null || prev.length()==0) { sep = " "; }
                val = " ".repeat(w);
            }
            if(orientation=='L') {      // cut 'val' to width
                val = val.substring(0, Math.max(0, Math.min(val.length(), w - lmarg)));
                int pad = Math.max(0, w - sep.length() - lmarg - val.length() + sep.length());
                sb.append(String.format("%s%s%s%s", sep, fill.repeat(lmarg), val, fill.repeat(pad)));
            }
            if(orientation=='R') {      // cut 'val' to width
                int limit = Math.max(0, Math.min(val.length(), w - rmarg));
                val = val.substring(val.length() - limit);
                int pad = Math.max(0, w - sep.length() - rmarg - val.length() + sep.length());
                sb.append(String.format("%s%s%s%s", sep, fill.repeat(pad), val, fill.repeat(rmarg)));
            }
            if(i==columns.size()-1) {   // close last column with separator
                sb.append(value.length() > 0? sep : " ").append("\n");
            }
        };
        return this;
    }

    /**
     * Terminal method to print internal buffer to {@link OutputStream} and
     * clear internal buffer.
     * @param os {@link OutputStream} as destination for buffer content
     */
    public void print(OutputStream os) {
        if(os != null) {
            try {
                os.write(sb.toString().getBytes());
                sb.setLength(0);
            } catch (IOException e) { }
        }
    }


    /**
     * Inner class that describes a column (column specification).
     */
    @Getter
    @AllArgsConstructor
    @Accessors(fluent=true, chain=true)
    @ToString
    private static class Column {
        int i; char sep; String fill; String label; int lmarg; int rmarg;
        @Setter int width;
        @Setter char orientation;
        // 
        Column(int i, char sep, String label) {
            this(i, sep, label.trim().isEmpty()? " " : "-", label.trim(),   // fill, label
                label.length() - label.replaceAll("^\s+", "").length(),     // lmarg
                label.length() - label.replaceAll("\s+$", "").length(),     // rmarg
                label.length(), 'L');                                       // orientation
        }
    };

    /**
     * Inner class according to {@code lombok's} {@code Builder} implementation
     * (class is supplemented by {@code lombok}).
     */
    public static class TableFormatterBuilder {
        private List<Column> columns = new ArrayList<>();
        private Map<Class<?>, Function<Object, String[]>> rowMappers = null;
        private Map<Class<?>, Function<Object, String[][]>> multiRowMappers = null;

        /**
         * Define table by specifying the header line, e.g. with:
         * <pre>
         * .hdr("| ID | NAME | CONTACT |")
         * </pre>
         * @param columnSpec line specifying the table
         * @return chainable self-reference
         */
        public TableFormatterBuilder columns(String columnSpec) {
            if(columnSpec != null && columnSpec.length() > 0) {
                StringBuilder sb = new StringBuilder();
                char prev_c = '?'; int prev_i = 0; int len = columnSpec.length();
                for(int i=0; i < len; i++) {
                    char c = i < len? columnSpec.charAt(i) : prev_c;
                    switch(c) {
                    default: sb.append(c); break;
                    case '|': case '+':
                        if(i > 0) columns.add(new Column(prev_i, prev_c, sb.toString()));
                        prev_c = c; prev_i = i; sb.setLength(0);
                    }
                }
            }
            return this;
        }

        /**
         * Define column widths, e.g. with: {@code .widths(6, 16, 16, 24)}.
         * @param widths column widths
         * @return chainable self-reference
         */
        public TableFormatterBuilder widths(int... widths) {
            for(int i=0; widths != null && i < widths.length && i < columns.size(); i++) {
                columns.get(i).width(widths[i]);
            }
            return this;
        }

        /**
         * Define column alignments (L: left-aligned, R: right-aligned),
         * e.g. with {@code .orientation("RLL")}.
         * @param alignments column alignments
         * @return chainable self-reference
         */
        public TableFormatterBuilder alignments(String alignments) {
            for(int i=0; alignments != null && i < alignments.length() && i < columns.size(); i++) {
                columns.get(i).orientation(alignments.charAt(i));
            }
            return this;
        }

        /**
         * Register row-mapper for type {@code <T>} that maps an object of type
         * {@code <T>} to a {@code String} array of consecutive segments in one row.
         * @param <T> generic type of objects to map
         * @param typeToMap class of type {@code <T>}
         * @param mapper {@link Function} invoked to map object of type {@code <T>}
         * @return chainable self-reference
         */
        @SuppressWarnings("unchecked")
        public <T> TableFormatterBuilder rowMapper(Class<T> typeToMap, Function<T, String[]> mapper) {
            if(typeToMap != null && mapper != null) {
                if(this.rowMappers==null) {
                    this.rowMappers = new HashMap<>();
                }
                this.rowMappers.put(typeToMap, (Function<Object, String[]>) mapper);
            }
            return this;
        }

        /**
         * Register row-mapper for type {@code <T>} that maps an object of type
         * {@code <T>} to a {@code String} array with multiple rows of consecutive
         * segments in each row.
         * @param <T> generic type of objects to map
         * @param typeToMap class of type {@code <T>}
         * @param mapper {@link Function} invoked to map object of type {@code <T>}
         * @return chainable self-reference
         */
        @SuppressWarnings("unchecked")
        public <T> TableFormatterBuilder multiRowMapper(Class<T> typeToMap, Function<T, String[][]> mapper) {
            if(typeToMap != null && mapper != null) {
                if(this.multiRowMappers==null) {
                    this.multiRowMappers = new HashMap<>();
                }
                this.multiRowMappers.put(typeToMap, (Function<Object, String[][]>) mapper);
            }
            return this;
        }
    }

    /**
     * Test method that prints some table content:
     * <pre>
     * +------+----------------+----------------+------------------------+
     * |   ID | NAME           | FIRSTNAME      | CONTACT                |
     * +------+----------------+----------------+------------------------+
     * |  100 | Meyer          | Eric           | eme22@gmail.com        |
     * |  101 | Sommer         | Tina           | +49 030 22458 29425    |
     * |  102 | Schulze        | Tim            | +49 171 2358124        |
     * |  103 | Brinkmann      | Tobias         | +49 030 662465724      |
     * +------+----------------+----------------+------------------------+
     *                                   total: |                      4 |
     *                                          +------------------------+
     * +------+                                                           
     *        +----------------+                                          
     *                         +----------------+                         
     *                                          +------------------------+
     * | A    |                                                           
     *        | B              |                                          
     *                         | C              |                         
     *                                          | D                      |
     *   A                                                                
     *          B                                                         
     *                           C                                        
     *                                            D                       
     *   A      B                C                D                       
     * </pre>
     * @return {@link TableFormatter} instance that prints some table content.
     */
    public static TableFormatter test() {
        // 
        TableFormatter table = TableFormatter.builder()
            .columns("| ID | NAME | FIRSTNAME | CONTACT |")
            .widths(6, 16, 16, 24, 16, 21)
            .alignments("R")
            // .rowMapper(Customer.class, c ->
            //     new String[] {Long.toString(c.id()), c.name(), c.firstName(), c.contact(),
            //         c.status().toString(), c.statusChange().format(dtf)})
            .build();

            table.header()
                .row("100", "Meyer", "Eric", "eme22@gmail.com")
                .row("101", "Sommer", "Tina", "+49 030 22458 29425")
                .row("102", "Schulze", "Tim", "+49 171 2358124")
                .row("103", "Brinkmann", "Tobias", "+49 030 662465724")
                .footer()
                .row(new String[] {"", "", "{R }total:", "{R}4"})
                .row(new String[] {"", "", "", "{---}"})
                // .line("", "", "", "")   // empty line
                // 
                .line(new String[] {"{---}", "", "", ""})
                .line(new String[] {"", "{---}", "", ""})
                .line(new String[] {"", "", "{---}", ""})
                .line(new String[] {"", "", "", "{---}"})
                // 
                .row(new String[] {"{L}A", "", "", ""})
                .row(new String[] {"", "B", "", ""})
                .row(new String[] {"", "", "C", ""})
                .row(new String[] {"", "", "", "D"})
                // 
                .row(new String[] {"{L }A", "", "", ""})
                .row(new String[] {"", "{L }B", "", ""})
                .row(new String[] {"", "", "{L }C", ""})
                .row(new String[] {"", "", "", "{L }D"})
                .row(new String[] {"{L }A", "{L }B", "{L }C", "{L }D"})
                // 
            .print(System.out);
            // 
            return table;
    }
}
