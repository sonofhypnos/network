package stuff;

import resources.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.Math.pow;

/**
 * The type Ip.
 */
public class IP implements Comparable<IP> {

    /**
     * The constant REGEX_GROUP_BLOCKS.
     */
    private static final int BLOCK_SIZE = 3;
    private static final int BLOCK_NUMBER = 4;
    private static final int PERIOD_NUMBER = BLOCK_NUMBER - 1;
    private static final int BASE = 256;
    private static final int MAX_BLOCK_VALUE = BASE- 1;
    private static final String REGEX_BLOCK = String.format("\\d{1,%s}", BLOCK_SIZE);
    /**
     * The Regex ip.
     */
    protected static final String REGEX_IP = String.format("((?:" + REGEX_BLOCK + "\\.){%s}\\d{1,%s})", PERIOD_NUMBER,
            BLOCK_SIZE);

    /**
     * The Blocks.
     */
    List<Integer> blocks;

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
    private static List<Integer> parseIP(String ipString) throws ParseException {
        Pattern ipPattern = Pattern.compile(REGEX_IP);
        Matcher ipMatcher = ipPattern.matcher(ipString);
        Pattern blockPattern = Pattern.compile(REGEX_BLOCK);
        Matcher blockMatcher = blockPattern.matcher(ipString);
        List<Integer> blocks = new ArrayList<>();

        if (!ipMatcher.matches()) {
            throw new ParseException(String.format(Errors.S_DOES_NOT_FOLLOW_THE_FORMAT, ipString));
        }

        while (blockMatcher.find()) {
            blocks.add(parseInt(blockMatcher.group()));
        }
        if (blocks.stream().anyMatch(x -> x > MAX_BLOCK_VALUE)) {
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
        return Integer.compare(this.getIPValue(), o.getIPValue());
    }

    /**
     * Gets ip value.
     *
     * @return the ip value
     */
    protected int getIPValue() {
        long IPValue = 0;
        for (int i = 0; i < blocks.size(); i++) {
            IPValue += pow(BASE, i) * blocks.get(blocks.size() - i - 1);
        }
        return Math.toIntExact(IPValue + MIN_VALUE); //we add Min_value to make sure it fits into int
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks);
    }

    @Override // TODO: 18.02.22 figure out what override actually did?
    public boolean equals(Object o) {
        // TODO: 18.02.22 am I supposed to do that? is that correct according to java api?
        // should I use object instead?
        if (o == null || getClass() != o.getClass()) return false;
        IP ip = (IP) o;
        return this.compareTo(ip) == 0;
    }

    // TODO: 20.02.22 implement clone method?

}
