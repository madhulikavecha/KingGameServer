package com.kinggameserver.backend.webserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.kinggameserver.backend.controller.GameController;
import com.kinggameserver.backend.exceptions.BackEndException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Main Class where the HttpServer for the BackEnd is deployed.
 *
 * @author Sudha Madhulika
 * @version 1.0
 * @date 03/11/2020
 */

public class GameServer {
    public static int PORT = 8080;

    /**
     * Main Method where the HttpServer is deployed
     * The HttpPort can be changed, running the app with the argument [-p portNumber]
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws IOException {
        if (args.length == 0) {
            String hostName = "localhost";
            try {
                hostName = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException ex) {
                System.err.println("Unknown Host: " + ex);
            }
            HttpServer server = HttpServer.create(new InetSocketAddress(hostName, PORT), 0);
            HttpContext context = server.createContext("/");
            context.setHandler(new GameHandler(GameController.getInstance()));
            context.getFilters().add(new URLFilter());
            server.start();
            System.out.println("   HTTPServer started in http://" + hostName + ":" + PORT + "/");
            System.out.println("   Started HTTPServer Successfully!\n");
        } else {
            try {
                if (args.length == 2) {
                    if (args[0].equals("-p")) {
                        PORT = Integer.parseInt(args[1]);
                    } else {
                        throw new BackEndException("Invalid argument type'" + args[0] + "'." +
                                " proper usage: java -jar kinggameserver.jar -p <portNumber>");
                    }
                } else {
                    throw new BackEndException("Invalid number of arguments.");
                }
            } catch (Exception e) {
                System.err.println("Error with the arguments.");
                System.err.println(e.getMessage());
                System.err.println("java -jar kingbackendgame.jar -p <portNumber>");
                return;
            }
        }
    }
}