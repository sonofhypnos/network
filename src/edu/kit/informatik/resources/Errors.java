package edu.kit.informatik.resources;

/**
 * Error messages for the Programming Assignment.
 *
 * @author upkim
 * @version 1.0 2022-03-12 22:12
 */
public class Errors {
    /**
     * Required prefix for the KIT Programming course Programming Assignments.
     */
    public static final String KIT_ERROR_PREFIX = "Error, ";
    /**
     * Thrown if input to IP constructor is null.
     */
    public static final String IP_IS_NULL = KIT_ERROR_PREFIX + "IP is null.";
    /**
     * IP_HAS_VALUES_GREATER_THAN_256 is thrown if one block in the ip has a value greater than 256.
     */
    public static final String IP_HAS_VALUES_GREATER_THAN_256 = KIT_ERROR_PREFIX + "IP has values "
                                                                + "greater than 256.";
    /**
     * ERROR_LEADING_ZEROS is thrown if the String provided to the IP constructor contains leading zeros.
     */
    public static final String ERROR_LEADING_ZEROS = KIT_ERROR_PREFIX + "the provided string contains leading zeros.";
    /**
     * FORMAT_FOR_AN_IP is thrown if the String supplied to the IP constructor does not follow the IPv4 format.
     */
    public static final String FORMAT_FOR_AN_IP = KIT_ERROR_PREFIX + "the provided String does not follow "
                                                  + "the format for an IP.";
    /**
     * ERROR_INVALID_IPS. Thrown if not all ips in the Network could be parsed as valid ips.
     */
    public static final String ERROR_INVALID_IPS = KIT_ERROR_PREFIX + "network contains invalid ips.";
    /**
     * Thrown if there is not a closing parenthesis for every opening parenthesis.
     */
    public static final String ERROR_BRACKET = KIT_ERROR_PREFIX
                                               + "there is a parenthesis mismatch in the provided string.";
    /**
     * ERROR_NETWORK_STRING_IS_NULL is thrown if the input to the Network constructor is null.
     */
    public static final String ERROR_NETWORK_STRING_IS_NULL = KIT_ERROR_PREFIX + "network string is null.";
    /**
     * ERROR_NETWORK_STRING_IS_EMPTY is thrown if the input string to the Network constructor is empty.
     */
    public static final String ERROR_NETWORK_STRING_IS_EMPTY = KIT_ERROR_PREFIX + "network string is empty.";
    /**
     * ERROR_DUPLICATE_I_PS is thrown if there are duplicate ips in the input string to the Network constructor.
     */
    public static final String ERROR_DUPLICATE_I_PS = KIT_ERROR_PREFIX + "duplicate entries in provided String.";
    /**
     * ERROR_THERE_ARE_DUPLICATE_CHILDREN iff there are duplicate children in the children list provided to the Network
     * constructor.
     */
    public static final String ERROR_THERE_ARE_DUPLICATE_CHILDREN = KIT_ERROR_PREFIX + "there are duplicate children.";
    /**
     * Thrown if input to Network violates rules for a valid forest.
     */
    public static final String ERROR_NOT_A_VALID_FOREST = KIT_ERROR_PREFIX
                                                          + "network does not describe a valid forest.";
    /**
     * ERROR_MINIMUM_OF_1_CONNECTION thrown if the network does not contain a minimum of one connection.
     */
    public static final String ERROR_MINIMUM_OF_1_CONNECTION = KIT_ERROR_PREFIX
                                                               + "The network must have a minimum of one."
                                                               + "connection";
    /**
     * ERROR_FOREST_EDGE is thrown if adding an edge would make a forest invalid.
     */

    private Errors() {
    }
}