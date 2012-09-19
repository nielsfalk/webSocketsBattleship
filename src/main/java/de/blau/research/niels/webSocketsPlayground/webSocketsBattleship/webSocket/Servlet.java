package de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.webSocket;


import com.sun.grizzly.websockets.WebSocketEngine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "WebSocketServlet", urlPatterns = "/WebSocketServlet", loadOnStartup = 1)
public class Servlet extends HttpServlet {
    private final App app = new App();

    @Override
    public void init(ServletConfig config) throws ServletException {
        WebSocketEngine.getEngine().register(app);
    }
}
