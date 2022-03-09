package edu.kit.informatik.resources;

/**
 * ForestException is thrown if a function would turn the Forest invalid.
 *
 * @author upkim
 * @version 1.0 2022-03-08 11:52
 */
public class ForestException extends RuntimeException {

    /**
     * Instantiates a new Tree exception.
     *
     * @param message the message
     */
    public ForestException(String message) {
        super(message);
    }
}
