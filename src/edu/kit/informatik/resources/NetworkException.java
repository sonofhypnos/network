package edu.kit.informatik.resources;

/**
 * The type NetworkException exception. Thrown if Network does not have correct format
 *
 * @author upkim
 * @version 1.0 2022-03-08 11:52
 */
public class NetworkException extends RuntimeException {

    /**
     * Instantiates a new Tree exception.
     *
     * @param message the message
     */
    public NetworkException(String message) {
        super(message);
    }
}