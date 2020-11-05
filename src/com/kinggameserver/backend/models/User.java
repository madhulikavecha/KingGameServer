package com.kinggameserver.backend.models;

import java.io.Serializable;

/**
 * Class that represents the entity User
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class User implements Serializable {


    /**
     * Integer for the UserId
     */
    private Integer userId;
    /**
     * Integer for the score of the User
     */
    private Integer score;

    /**
     * Creates a new instance of UserScore
     *
     * @param userId
     * @param score
     */
    public User(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    /**
     * Get the value of score
     *
     * @return the value of score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the value of score
     *
     * @param score new value of score
     */
    public void setScore(int score) {

        this.score = score;
    }

    /**
     * Get the value of userId
     *
     * @return the value of userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Set the value of userId
     *
     * @param userId new value of userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
