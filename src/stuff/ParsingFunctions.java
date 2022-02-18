package stuff;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Parsing functions.
 */
public class ParsingFunctions {
    /*
     * @project FinalAssignment2022_1
     * @author Tassilo Neubauer
     */

    private static final String REGEX_IP = "((?:(\\d{1,4}).){3}\\d{1,4})";

    /**
     * Parse IP String in point notation
     *
     * @param ipString the ip string
     * @return the list
     */
    public static List<String> parseIP(String ipString) {
        Pattern ipPattern = Pattern.compile(REGEX_IP);
        Matcher ipMatcher = ipPattern.matcher(ipString);
        return null;
    }

}
