package com.kinggameserver.backend.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.kinggameserver.backend.controller.GameController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * HttpHandler for to deploy the Http Rest Web Services,
 * for the BackEnd MiniGame Server.
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class GameHandler implements HttpHandler {
    /*
     *  Value for the Parameters previously treated in the URLFilter
     */

    public static final String PARAMETER_ATTRIBUTE = "PARAMETERS";
    public static final String USER_ID_PARAMETER = "userid";
    public static final String REQUEST_PARAMETER = "request";
    public static final String LEVEL_ID_PARAMETER = "level_id";
    public static final String SESSION_KEY = "session_key";
    public static final String SCORE = "score";
    /*
     *  Request for the different services
     */
    public static final String LOGIN_REQUEST = "login";
    public static final String POST_SCORE_REQUEST = "score";
    public static final String HIGH_SCORE_LIST_REQUEST = "highscorelist";
    /*
     *  Http Content type constants
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TEXT = "text/plain";
    /**
     * Instance for the GameManager,
     * where all the data are stored.
     */
    private final GameController gameController;
    /**
     * To read map sent in URLFilter
     */
    private HashMap<String, String> parameterMap;

    /**
     * Creates a new instance of GameHandler
     *
     * @param gameController
     */
    public GameHandler(GameController gameController) {
        this.gameController = gameController;
    }

    public void handle(HttpExchange exchange) throws IOException {
        parameterMap = (HashMap<String, String>) exchange.getAttribute(PARAMETER_ATTRIBUTE);
        String request = parameterMap.get(REQUEST_PARAMETER);
        String httpResponse = "";
        try {
            switch (request) {
                case LOGIN_REQUEST:
                    int user_id = Integer.parseInt(parameterMap.get(USER_ID_PARAMETER));
                    httpResponse = gameController.login(user_id);
                    break;
                case POST_SCORE_REQUEST:
                    int post_level_id = Integer.parseInt(parameterMap.get(LEVEL_ID_PARAMETER));
                    String session_key = parameterMap.get(SESSION_KEY);
                    int score = Integer.parseInt(parameterMap.get(SCORE));
                    gameController.updateScore(post_level_id, session_key, score);
                    break;
                case HIGH_SCORE_LIST_REQUEST:
                    int get_level_id = Integer.parseInt(parameterMap.get(LEVEL_ID_PARAMETER));
                    httpResponse = gameController.getHighScoreList(get_level_id);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + request);
            }
        } catch (Exception e) {
            httpResponse = e.getMessage();
        }
        exchange.getResponseHeaders().add(CONTENT_TYPE, CONTENT_TEXT);
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, httpResponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(httpResponse.getBytes());
        os.close();
    }
}
