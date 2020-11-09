package com.kinggameserver.backend.exceptions;

/**
 * Thrown when someone try to access wrong url service
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class NonValidHttpException extends Exception{

    /**
     * Constructs a {@code NonValidHttpException} with no detail message.
     */

    public NonValidHttpException(){
        super();
    }

    /**
     * Constructs a {@code NonValidHttpException} with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public NonValidHttpException(String msg){
        super(msg);
    }





}
