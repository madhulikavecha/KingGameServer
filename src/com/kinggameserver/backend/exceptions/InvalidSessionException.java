package com.kinggameserver.backend.exceptions;

public class InvalidSessionException extends Exception{

    public static final String INVALID_SESSION_ERROR = "Invalid Session or Session Expired";

    /**
     * Constructs a {@code InvalidSessionException} with no detail message.
     */
    public InvalidSessionException() {
        super(INVALID_SESSION_ERROR);
    }

}
