package resources;

/**
 * Errors is an utility class for error messages
 * We don't include the content of the actual string that failed in our parsing error messages (even though that
 * would be useful) as that would not abide by the specification to not include special characters in our error
 * messages.
 */
public class Errors {

    /**
     * The constant IP_HAS_VALUES_GREATER_THAN_256.
     */
// TODO: 05.03.22 make sure no special characters
    /**
     * The constant IP_HAS_VALUES_GREATER_THAN_256.
     */
    public static final String IP_HAS_VALUES_GREATER_THAN_256 = "IP has values greater than 256!";
    /**
     * The constant S_DOES_NOT_FOLLOW_THE_FORMAT.
     */
    public static final String S_DOES_NOT_FOLLOW_THE_FORMAT = "the String %s does not follow the format for an IP";
    /**
     * The constant S_NOT_NETWORK.
     */
    public static final String S_NOT_NETWORK = "the provided String does not follow the format for a Network";
    /**
     * The constant STRING_S_CONTAINS_LEADING_ZEROS.
     */
    public static final String STRING_S_CONTAINS_LEADING_ZEROS = "the string \"%s\" contains leading zeros";
    /*
     * @project FinalAssignmentNoBloat
     * @author upkim
     */
}
