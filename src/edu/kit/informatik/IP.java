package edu.kit.informatik;

import edu.kit.informatik.resources.ErrorMessages;
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
 * The IP class models a IPv4 address. The IP class is immutable. Two IP instances are the same if they represent the
 * same IPv4 address.
 *
 * @author upkim
 * @version 1.0 2022-03-12 22:13
 */
public class IP implements Comparable<IP> {

    //Strings/Regex for parsing and printing
    /**
     * delimiter for toString
     */
    private static final String IP_DELIMITER = ".";
    /**
     * Regex delimiter for parsing
     */
    private static final String IP_DELIMITER_REGEX = "\\.";
    private static final int BLOCK_QUANTITY = 4;
    private static final int BLOCK_BASE = 256;
    private static final int MAX_BLOCK_VALUE = BLOCK_BASE - 1;
    private static final int MAX_BLOCK_LENGTH = Integer.toString(BLOCK_BASE).length();
    private static final String REGEX_BLOCK = String.format("\\d{1,%d}", MAX_BLOCK_LENGTH);
    /**
     * The REGEX for the IP. Every block is followed by IP_DELIMITER_REGEX except the last one.
     */
    static final String REGEX_IP = String.format(
            "((?:" + REGEX_BLOCK + IP_DELIMITER_REGEX + ")" + "{%d}" + REGEX_BLOCK + ")", BLOCK_QUANTITY - 1);
    //
    /**
     * String to match leading 0s. Either the leading 0 comes after the delimiter or it is at the front of the
     * IP-string.
     */
    private static final String LEADING_ZERO_REGEX = IP_DELIMITER_REGEX + "0(\\d)+|(^0(\\d)+)";

    private final List<Integer> blocks;

    /**
     * Parses String and instantiates a new IP object.
     *
     * @param pointNotation the string to be parsed in point Notation.
     * @throws ParseException if the IP does not follow the format for an IP or IP is null.
     */
    public IP(final String pointNotation) throws ParseException {
        if (pointNotation == null) {
            throw new ParseException(ErrorMessages.IP_IS_NULL);
        }
        blocks = parseIP(pointNotation);
    }

    private static List<Integer> parseIP(final String ipString) throws ParseException {
        Matcher ipMatcher = getMatcher(REGEX_IP, ipString);
        Matcher blockMatcher = getMatcher(REGEX_BLOCK, ipString);
        Matcher zeroMatcher = getMatcher(LEADING_ZERO_REGEX, ipString);

        List<Integer> blocks = new ArrayList<>();

        if (!ipMatcher.matches()) {
            throw new ParseException(ErrorMessages.FORMAT_FOR_AN_IP);
        }

        if (zeroMatcher.find()) {
            throw new ParseException(ErrorMessages.ERROR_LEADING_ZEROS);
        }

        while (blockMatcher.find()) {
            /*The following try catch block is actually superfluous with the current parameters: Because of the regex
            checks before, our values are guaranteed to fit into int, because the block are only up to 4 digits long
            (10^4 < 2^31) but we added it nonetheless, because the Programmieren Wiki mentions that
             (https://ilias.studium.kit.edu/goto.php?target=wiki_1617287_parseInt)
            not checking for this exception will lead to point reduction and leaves it ambigous whether checking
             the validity of the input through other means is also valid. One might plausibly also choose a
            different value b for block length such that 10^b > 2^31 which would allow this exception to be
            thrown.
             */
            try {
                blocks.add(parseInt(blockMatcher.group()));
            } catch (NumberFormatException e) {
                throw new ParseException(ErrorMessages.FORMAT_FOR_AN_IP);
            }
        }

        if (blocks.stream().anyMatch(x -> x > MAX_BLOCK_VALUE)) {
            throw new ParseException(ErrorMessages.IP_HAS_VALUES_GREATER_THAN_256);
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

    private int getIPValue() {
        long ipValue = 0;
        for (int i = 0; i < blocks.size(); i++) {
            ipValue += pow(BLOCK_BASE, i) * blocks.get(blocks.size() - i - 1);
        }
        return Math.toIntExact(ipValue + MIN_VALUE); //we add Min_value to make sure it fits into int (we need all 32
        // bits to compare IPs.
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IP ip = (IP) o;
        return this.blocks.equals(ip.blocks);
    }

}
