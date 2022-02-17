package stuff;
import resources.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.*;
import static java.lang.Math.pow;

/**
 * The type Ip.
 */
public class IP implements Comparable<IP> {

    /**
     * The constant REGEX_GROUP_BLOCKS.
     */
    public static final int REGEX_GROUP_BLOCKS = 2;
    /**
     * Instantiates a new Ip.
     *
     * @param pointNotation the point notation
     * @throws ParseException the parse exception
     */
    List<Integer> blocks;
    private static final int MAX_BLOCK_VALUE = 255;
    private static final String REGEX_BLOCK = "(\\d{1,4})";
    public static final String REGEX_IP = "((?:" + REGEX_BLOCK +".){3}\\d{1,4})";


    /**
     * Instantiates a new Ip.
     *
     * @param pointNotation the point notation
     * @throws ParseException the parse exception
     */
    public IP(final String pointNotation) throws ParseException {
        blocks = parseIP(pointNotation);



    }


    /**
     * Parse ip list.
     *
     * @param ipString the ip string
     * @return the list
     */
    public static List<Integer> parseIP(String ipString) throws ParseException {
        Pattern ipPattern = Pattern.compile(REGEX_IP);
        Matcher ipMatcher = ipPattern.matcher(ipString);
        Pattern blockPattern = Pattern.compile(REGEX_BLOCK);
        Matcher blockMatcher = blockPattern.matcher(ipString);
        List<Integer> blocks = new ArrayList<>();

        if (!ipMatcher.matches()) {
            return null;
        }

        while (blockMatcher.find()) {
            blocks.add(parseInt(blockMatcher.group()));
        }
        if(blocks.stream().anyMatch(x -> x > MAX_BLOCK_VALUE)){
            throw new ParseException(Errors.IP_HAS_VALUES_GREATER_THAN_256);
        }

        return blocks;
    }

    @Override
    public String toString() {
        return blocks.stream().map(x -> Integer.toString(x)).collect(Collectors.joining("."));
    }

    @Override
    public int compareTo(IP o) {
        // TODO: 16.02.22 Es koennte sein dass das hier andersherum sein muss
        return this.getIPValue() - o.getIPValue();
    }

    public int getIPValue(){
        int IPValue = 0;
        for (int i = 0; i < blocks.size(); i++) {
            IPValue += pow(MAX_BLOCK_VALUE, i) * blocks.get(i);
        }
        return IPValue;
    }

}
