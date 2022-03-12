package edu.kit.informatik.resources;

/**
 * NetworkException is thrown if Network does not have correct format.
 *
 * @author upkim
 * @version 1.0 2022-03-12 22:13
 */
public class NetworkException extends IllegalArgumentException {

    /**
     * Instantiates a new Tree exception.
     *
     * @param message the message
     */
    public NetworkException(String message) {
        super(message);
    }
}
