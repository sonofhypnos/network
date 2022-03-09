package edu.kit.informatik.resources;

/**
 * Parse exception.
 *
 * @author upkim
 * @version 1.0 2022-03-08 11:52
 */
public class ParseException extends RuntimeException {

    /**
     * Instantiates a new Parse exception.
     *
     * @param message the message
     */
    public ParseException(String message) {
        super(message);
    }
}
