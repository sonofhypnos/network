package edu.kit.informatik.resources;

/**
 * Parse exception is thrown if parse-format is violated in any way.
 *
 * @author upkim
 * @version 1.0 2022-03-12 22:13
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
