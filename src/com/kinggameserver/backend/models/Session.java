package com.kinggameserver.backend.models;

import java.io.Serializable;

/**
 * Class that represents the entity Session
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class Session implements Serializable {

    /**
     * String with the created SessionKey
     */
    private String sessionKey;

    /**
     * Long with the time where the Session was created
     */
    long session_start_time;
    User user;

    /**
     * Creates a new instance of Session and User
     *
     * @param userId
     * @param sessionKey
     * @param current_time
     */
    public Session(int userId, String sessionKey, long current_time) {
        this.sessionKey = sessionKey;
        session_start_time = current_time;
        user = new User(userId, 0);
    }

    /**
     * Get the User object
     *
     * @return user object with userId and score
     */
    public User getUser() {
        return user;
    }

    /**
     * Get the value of userId
     *
     * @return the value of userId
     */
    public Integer getUser_id() {
        return user.getUserId();
    }

    /**
     * Get the value of sessionKey
     *
     * @return the value of sessionKey
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * Get the value of session_start_time
     *
     * @return the value of session_start_time
     */
    public long getSession_start_time() {

        return session_start_time;
    }

}
