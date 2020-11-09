package com.kinggameserver.backend.exceptions;

/**
 * Thrown when someone try to access a service without a valid SessionKey
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class InvalidSessionException extends Exception{

    public static final String INVALID_SESSION_ERROR = "Invalid Session or Session Expired";

    /**
     * Constructs a {@code InvalidSessionException} with no detail message.
     */
    public InvalidSessionException() {
        super(INVALID_SESSION_ERROR);
    }

}
