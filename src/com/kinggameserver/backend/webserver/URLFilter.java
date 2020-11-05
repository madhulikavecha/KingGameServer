package com.kinggameserver.backend.webserver;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Class used to Filter all the HttpRequest in the Game  Server
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class URLFilter extends Filter {

    private HashMap<String, String> parameterMap = new HashMap<String, String>();

    /*
     *  Enum class to create  Patterns for all the valid requests for each service
     */
    public enum REGEX_PATTERNS {
        LOGIN_REGEX("/(\\d*)/login"),
        POST_SCORE_TO_LEVEL_REGEX("/(\\d*)/score\\?sessionkey=(.*)"),
        GET_HIGH_SCORE_REGEX("/(\\d*)/highscorelist");

        /**
         * String to read uri
         */
        public final String urlPattern;

        /**
         * Creates a new instance of REGEX_PATTERNS
         *
         * @param urlPattern
         */
        private REGEX_PATTERNS(String urlPattern) {
            this.urlPattern = urlPattern;
        }

        /**
         * Get the value of enum constant
         *
         * @return the value of enum constant
         */

        public String getUrlPattern() {

            return urlPattern;
        }
    }

    /**
     * Method where all the filter are applied
     *
     * @param httpExchange
     * @param chain
     * @throws IOException
     */
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        String uri = httpExchange.getRequestURI().toString();
        REGEX_PATTERNS regexPattern = null;
        for (REGEX_PATTERNS regex_pattern : REGEX_PATTERNS.values()) {
            if (uri.matches(regex_pattern.getUrlPattern())) {
                regexPattern = regex_pattern;
            }
        }
        switch (regexPattern) {
            case LOGIN_REGEX:
                parameterMap = parseLoginParameters(uri);
                break;
            case POST_SCORE_TO_LEVEL_REGEX:
                parameterMap = parseScoreParameters(httpExchange, uri);
                break;
            case GET_HIGH_SCORE_REGEX:
                parameterMap = parseHighScoreListParameters(uri);
                break;
            default:
                throw new MalformedURLException("Unexpected value: " + regexPattern);
        }

        httpExchange.setAttribute(GameHandler.PARAMETER_ATTRIBUTE, parameterMap);
        chain.doFilter(httpExchange);
    }

    /**
     * Method where parse the Parameters from the Login Request
     *
     * @param uri
     * @return Hashmap with paramaters
     */
    private HashMap<String, String> parseLoginParameters(String uri) {
        String userId = uri.split("/")[1];
        parameterMap.put(GameHandler.REQUEST_PARAMETER, GameHandler.LOGIN_REQUEST);
        parameterMap.put(GameHandler.USER_ID_PARAMETER, userId);
        return parameterMap;
    }

    /**
     * Method where parse the Parameters from the Update score Request
     *
     * @param httpExchange,uri
     * @return Hashmap with paramaters
     */
    private HashMap<String, String> parseScoreParameters(HttpExchange httpExchange, String uri) {
        String params[] = uri.split("/");
        String levelId = params[1];
        String sessionKey = params[2].split("=")[1];
        String score;
        try {
            BufferedReader httpBody = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
            try {
                score = httpBody.readLine();
            } finally {
                httpBody.close();
            }
            parameterMap.put(GameHandler.LEVEL_ID_PARAMETER, levelId);
            parameterMap.put(GameHandler.REQUEST_PARAMETER, GameHandler.POST_SCORE_REQUEST);
            parameterMap.put(GameHandler.SESSION_KEY, sessionKey);
            parameterMap.put(GameHandler.SCORE, score);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameterMap;
    }

    /**
     * Method where parse the Parameters from the HighScoreList Request
     *
     * @param uri
     * @return Hashmap with paramaters
     */

    private HashMap<String, String> parseHighScoreListParameters(String uri) {
        String params[] = uri.split("/");
        String levelId = params[1];
        parameterMap.put(GameHandler.LEVEL_ID_PARAMETER, levelId);
        parameterMap.put(GameHandler.REQUEST_PARAMETER, GameHandler.HIGH_SCORE_LIST_REQUEST);
        return parameterMap;
    }

    @Override
    public String description() {
        return "Manage the Http Requests.";
    }
}
