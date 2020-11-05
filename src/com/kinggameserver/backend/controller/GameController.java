package com.kinggameserver.backend.controller;

import com.kinggameserver.backend.exceptions.InvalidSessionException;
import com.kinggameserver.backend.models.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GameController Class used as Manager where who manage all the Service Request
 * and all the data for the Mini-Game BackEnd server.
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */


public class GameController {

    /**
     * Instance for the SessionController,
     * where all the session data are stored.
     */
    public final SessionController sessionController;
    /**
     * ConcurrentHashMap<levelId, ConcurrentHashMap<userId,score>
     * with all the HighScore for all the Levels
     */
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> highScoreLevelMap;
    /**
     * ConcurrentHashMap<userId,score>
     * with all the HighScore for all userId for each Level
     */
    private ConcurrentHashMap<Integer, Integer> userScoreMap;

    /**
     * Static variable with the value for 15
     * to limit  higherscore users per level
     */
    public static final int MAX_USERS_PER_LEVEL = 15;

    /**
     * Singleton Instance
     */
    public static GameController gameControllerInstance;

    /**
     * Creates a new instance of GameController
     */
    public GameController() {
        sessionController = new SessionController();
        highScoreLevelMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>();
        userScoreMap = new ConcurrentHashMap<Integer, Integer>();
    }

    /**
     * Obtain the instance for the singleton
     * <p>
     * double-checked locking
     *
     * @return the instance initialized
     */
    public static GameController getInstance() {
        if (gameControllerInstance == null) {
            synchronized (GameController.class) {
                if (gameControllerInstance == null) {
                    gameControllerInstance = new GameController();
                }
            }
        }
        return gameControllerInstance;
    }

    /**
     * Login Service Request
     *
     * @param user_id userId who want to make a login
     * @return String with the sessionKey for the active session
     */

    public String login(int user_id) {
        Session session = sessionController.createNewSession(user_id);
        return session.getSessionKey();
    }

    /**
     * Update Score Service Request
     *
     * @param session_key sessionKey for a valid and active Session
     * @param level_id    level to insert the score
     * @param score       point to insert in the level
     */
    public void updateScore(int level_id, String session_key, int score) throws InvalidSessionException {
        Session session = sessionController.getSession(session_key);
        if (session != null) {
            if (sessionController.isSessionValid(session)) {
                session.getUser().setScore(score);
                updateHighScoreMap(level_id, session, score);
            } else {
                throw new InvalidSessionException();
            }
        } else {
            throw new InvalidSessionException();
        }
    }

    /**
     * @param session  to get userId from session
     * @param level_id level to insert the score
     * @param score    point to insert in the level
     *                 <p>
     *                 This method udpates the user score into the concurrent hashMap
     */
    private void updateHighScoreMap(int level_id, Session session, int score) {
        if (highScoreLevelMap.isEmpty()) {
            userScoreMap.put(session.getUser_id(), score);
            highScoreLevelMap.put(level_id, userScoreMap);
        } else if (!highScoreLevelMap.containsKey(level_id)) {
            userScoreMap.put(session.getUser_id(), score);
            highScoreLevelMap.put(level_id, userScoreMap);
        } else if (highScoreLevelMap.containsKey(level_id) &&
                !highScoreLevelMap.get(level_id).containsKey(session.getUser_id())) {
            userScoreMap.put(session.getUser_id(), score);
            highScoreLevelMap.put(level_id, userScoreMap);
        } else if (highScoreLevelMap.containsKey(level_id) &&
                (score > highScoreLevelMap.get(level_id).get(session.getUser_id()))) {
            userScoreMap.put(session.getUser_id(), score);
            highScoreLevelMap.put(level_id, userScoreMap);
        }
    }


    /**
     * HighScoreList Service Request
     *
     * @param get_level_id level to print the High Score List
     * @return CSV of <userid>=<score>
     */
    public String getHighScoreList(int get_level_id) {
        List<String> highScoreCSVList = new ArrayList<String>();
        if (highScoreLevelMap.containsKey(get_level_id)) {
            highScoreLevelMap.get(get_level_id).entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(MAX_USERS_PER_LEVEL)
                    .forEach(e -> {
                        highScoreCSVList.add(e.getKey().toString() + "=" + e.getValue().toString());
                    });
        }
        System.out.println(highScoreCSVList.toString());
        return highScoreCSVList.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(", ", ",");
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton, cannot be clonned");
    }

}
