package org.money.back.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyServer {

    private static final String CONTEXT_PATH = "/*";

    public static Server getServer(int port){
        ApplicationConfig config = new ApplicationConfig();
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(server, CONTEXT_PATH);
        context.addServlet(servlet, CONTEXT_PATH);
        return server;
    }

    public static void startServer(int port) throws Exception {
        Server jettyServer = getServer(port);
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception ex) {
            LogManager.getLogger(JettyServer.class.getName()).log(Level.ERROR, ex);
        } finally {
            jettyServer.stop();
        }
    }
}