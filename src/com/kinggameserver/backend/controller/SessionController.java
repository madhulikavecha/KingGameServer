package com.kinggameserver.backend.controller;

import com.kinggameserver.backend.models.Session;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller class where all the session data are stored
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class SessionController {
    /**
     * ConcurrentHashMap<sessionKey, Session> with all the Sessions actives in the server.
     */
    ConcurrentHashMap<String, Session> activeSessions;
    /**
     * Static variable with the value for 10 minutes in millis,
     * for the max time for a session to be active.
     */
    public static final int TIME_TO_LIVE = 600000;

    /**
     * Creates a new instance of SessionController
     */
    public SessionController() {
        activeSessions = new ConcurrentHashMap<>();
    }

    /**
     * Method used to get the Session from the Map for the sessionKey selected
     *
     * @param sessionKey key for the Session to get
     * @return the Session for the sessionKey selected
     */
    public Session getSession(String sessionKey) {
        return activeSessions.get(sessionKey);
    }

    /**
     * Method used to create a new Session for the selected userId
     *
     * @param userId user to create the new Session
     * @return the Session created for the user selected
     */
    public synchronized Session createNewSession(int userId) {
        final long session_start_time = System.currentTimeMillis();
        final String sessionKey = UUID.randomUUID().toString().replace("-", "");
        final Session session = new Session(userId, sessionKey, session_start_time);
        activeSessions.put(sessionKey, session);
        return session;
    }

    /**
     * Method used to validate if an sessionKey is associated
     * with a Valid and Active Session in the Server
     *
     * @param session for the Session to validate
     * @return a true if the sessionKey has a valid Session associated
     */
    public synchronized boolean isSessionValid(Session session) {
        boolean isSessionValid = true;
        if (session != null) {
            if (System.currentTimeMillis() - session.getSession_start_time() > TIME_TO_LIVE) {
                isSessionValid = false;
                deleteSession(session.getSessionKey());
            }
        }
        return isSessionValid;
    }

    /**
     * Method used to delete a Session from server
     */
    public void deleteSession(String sessionKey) {
        activeSessions.remove(sessionKey);
    }
}
