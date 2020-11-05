package com.kinggameserver.backend.exceptions;

public class BackEndException extends Exception{

    public static final String GENERIC_ERROR_MESSAGE = "";

    /**
     * Constructs a {@code BackEndException} with no detail message.
     */
    public BackEndException() {
        super(GENERIC_ERROR_MESSAGE);
    }

    /**
     * Constructs a {@code BackEndException} with the specified
     * detail message.
     *
     * @param errorMessage the detail message.
     */
    public BackEndException(String errorMessage) {
        super(errorMessage);
    }

}
