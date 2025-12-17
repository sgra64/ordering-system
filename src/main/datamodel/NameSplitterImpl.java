package datamodel;

import java.util.Optional;

import datamodel.NameSplitter.SplitName;

/**
 * None-public implementation class of the {@link NameSplitter} interface,
 * see {@link NameSplitter}.
 */
class NameSplitterImpl implements NameSplitter {

    /**
     * Class @{link NameSplitter} is implemented based on the <i>Singleton</i>
     * Pattern. The <i>Singleton</i> pattern has:
     * (1) a private static instance variable.
     */
    private static NameSplitter instance = new NameSplitterImpl();

    /*
     * (2) a private constructor.
     */
    private NameSplitterImpl() { }

    /**
     * (3) static public instance getter.
     * @return reference to <i>Singleton</i> instance
     */
    static NameSplitter getInstance() {
        return instance;
    }

    /**
     * Split single-String name into last- and first name parts.
     * @param name single-String name to split into first- and last name parts
     * @returns record {@link SplitName} or empty {@link Optional} if name
     * is illegal or could not be split
     */
    @Override
    public Optional<SplitName> split(String name) {
        if(name==null || name.length()==0)
            return Optional.empty();
        //
        String first="", last="";
        String[] spl1 = name.split("[,;]");
        if(spl1.length > 1) {
            // two-section name with last name first
            last = trim(spl1[0]);
            first = trim(spl1[1]);
        } else {
            // no separator [,;] -> split by white spaces;
            for(String s : name.split("\\s+")) {
                if(last.length() > 0) {
                    // collect firstNames in order and lastName as last
                    first += (first.length()==0? "" : " ") + last;
                }
                last = trim(s);
            }
        }
        return Optional.of(new SplitName(last, first));
    }

    /**
     * Trim leading and trailing white spaces {@code [\s]}, commata {@code [,;]}
     * and quotes {@code ["']} from a String (used for names and contacts).
     * @param s String to trim
     * @return trimmed String
     */
    private String trim(String s) {
        s = s.replaceAll("^[\\s\"',;]*", "");   // trim leading white spaces[\s], commata[,;] and quotes['"]
        s = s.replaceAll( "[\\s\"',;]*$", "");  // trim trailing accordingly
        return s;
    }
}
