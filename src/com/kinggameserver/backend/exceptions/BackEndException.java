package com.kinggameserver.backend.exceptions;

/**
 * Thrown as a generic Exception in the BackEnd Game Server
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class BackEndException extends Exception{

    public static final String GENERIC_ERROR_MESSAGE = "Some thing wrong happened";

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
