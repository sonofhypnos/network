package edu.kit.informatik;

import edu.kit.informatik.resources.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.Math.pow;
import static java.util.stream.Collectors.joining;

/**
 * The type Ip. The IP class is immutable.
 *
 * @author upkim
 * @version 1.0 2022-03-08 11:52
 */
public class IP implements Comparable<IP> {

    //Strings/Regex for parsing and printing
    private static final String IP_DELIMITER = "."; //Delimiter for toString
    private static final String IP_DELIMITER_REGEX = "\\."; //Delimiter for parsing
    private static final int MAX_BLOCK_LENGTH = 3;
    private static final int BLOCK_NUMBER = 4;
    private static final int BASE = 256;
    private static final int MAX_BLOCK_VALUE = BASE - 1;
    private static final String REGEX_BLOCK = String.format("\\d{1,%d}", MAX_BLOCK_LENGTH);
    /**
     * The REGEX for the IP. There is a Delimiter after every Block except the last one.
     */
    static final String REGEX_IP = String.format("((?:" + REGEX_BLOCK + IP_DELIMITER_REGEX + ")"
            + "{%d}" + REGEX_BLOCK + ")", BLOCK_NUMBER - 1);
    //either the leading 0 comes after the delimiter or it is at the front of the IP-string.
    private static final String LEADING_ZERO_REGEX = IP_DELIMITER_REGEX + "0(\\d)+|(^0(\\d)+)";

    //Error-Messages
    private static final String FORMAT_FOR_AN_IP = "Error, the provided String does not follow the format for an IP.";
    private static final String ERROR_LEADING_ZEROS = "Error, the provided string contains leading zeros.";
    private static final String IP_HAS_VALUES_GREATER_THAN_256 = "Error, IP has values greater than 256.";
    private static final String IP_IS_NULL = "Error, IP is null.";

    private final List<Integer> blocks;

    /**
     * Instantiates a new Ip.
     *
     * @param pointNotation the point notation
     * @throws ParseException if the IP does not follow the format for an IP or IP is null.
     */
    public IP(final String pointNotation) throws ParseException {
        if (pointNotation == null) {
            throw new ParseException(IP_IS_NULL);
        }
        blocks = parseIP(pointNotation);
    }

    private static List<Integer> parseIP(final String ipString) throws ParseException {
        Matcher ipMatcher = getMatcher(REGEX_IP, ipString);
        Matcher blockMatcher = getMatcher(REGEX_BLOCK, ipString);
        Matcher zeroMatcher = getMatcher(LEADING_ZERO_REGEX, ipString);

        List<Integer> blocks = new ArrayList<>();

        if (!ipMatcher.matches()) {
            throw new ParseException(FORMAT_FOR_AN_IP);
        }

        if (zeroMatcher.find()) {
            throw new ParseException(ERROR_LEADING_ZEROS);
        }

        while (blockMatcher.find()) {
            blocks.add(parseInt(blockMatcher.group()));
        }

        if (blocks.stream().anyMatch(x -> x > MAX_BLOCK_VALUE)) {
            throw new ParseException(IP_HAS_VALUES_GREATER_THAN_256);
        }

        return blocks;
    }

    private static Matcher getMatcher(final String regexIp, final String ipString) {
        Pattern pattern = Pattern.compile(regexIp);
        return pattern.matcher(ipString);
    }

    @Override
    public String toString() {
        return blocks.stream().map(x -> Integer.toString(x)).collect(joining(IP_DELIMITER));
    }

    @Override
    public int compareTo(final IP o) {
        return Integer.compare(this.getIPValue(), o.getIPValue());
    }

    /**
     * Gets ip value.
     *
     * @return the ip value
     */
    protected int getIPValue() {
        long ipValue = 0;
        for (int i = 0; i < blocks.size(); i++) {
            ipValue += pow(BASE, i) * blocks.get(blocks.size() - i - 1);
        }
        return Math.toIntExact(ipValue + MIN_VALUE); //we add Min_value to make sure it fits into int.
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IP ip = (IP) o;
        return this.compareTo(ip) == 0;
    }

}
