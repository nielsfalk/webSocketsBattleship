package de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.webSocket;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import com.sun.grizzly.websockets.WebSocketListener;
import de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.gameCaseRenaming.Position;

import java.util.ArrayList;
import java.util.List;

import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.gameCaseRenaming.Field.Rules.standard;

public class App extends WebSocketApplication {
    private static PlayerSocket pendingPlayer;

    @Override
    public WebSocket createWebSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        return coupleSockets(protocolHandler, listeners);
    }

    private synchronized PlayerSocket coupleSockets(ProtocolHandler protocolHandler, WebSocketListener[] listeners) {
        PlayerSocket playerSocket = new PlayerSocket(protocolHandler, listeners);
        if (pendingPlayer == null) {
            playerSocket.createMatch();
            pendingPlayer = playerSocket;
        } else {
            playerSocket.enterMatch(pendingPlayer.getMatch(), pendingPlayer);
            pendingPlayer.enterMatchWaitingIsOver(playerSocket);
            pendingPlayer = null;
        }
        return playerSocket;
    }

    @Override
    public boolean isApplicationRequest(Request request) {
        return true;
    }

    public static List<ArrayList<Position>> getInitialField() {
        List<ArrayList<Position>> result = new ArrayList<ArrayList<Position>>();
        for (int y = 0; y < standard.fieldSize.y; y++) {
            ArrayList<Position> row = new ArrayList<Position>();
            result.add(row);
            for (int x = 0; x < standard.fieldSize.x; x++) {
                row.add(new Position(x, y));
            }
        }
        return result;
    }
}
