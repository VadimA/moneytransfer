package org.money.back;

import org.money.back.config.JettyServer;

public class Application {

    private static final int SERVER_PORT = 8080;

    public static void main(String [] args) throws Exception {
        JettyServer.startServer(SERVER_PORT);
    }
}
