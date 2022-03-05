package edu.kit.informatik;

/**
 * The type Kit exception.
 * @author upkim
 * @version 1.0
 */
public abstract class KitException extends Exception {

    /**
     * The constant ERROR_PREFIX. Every Error must contain this prefix according to the programming assignment
     */
    private static final String ERROR_PREFIX = "Error ";

    /**
     * Instantiates a new Kit exception with ERROR_PREFIX added.
     *
     * @param message the message
     */
    public KitException(final String message) {
        super(ERROR_PREFIX + message);
    }
}
