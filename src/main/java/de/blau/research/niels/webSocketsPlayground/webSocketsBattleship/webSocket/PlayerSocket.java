package de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.webSocket;

import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;
import de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Field;
import de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Match;
import org.json.JSONObject;

import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Match.Player.first;
import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Match.Player.second;

class PlayerSocket extends DefaultWebSocket {
    private PlayerSocket otherPlayer;
    private Match.Player player;
    private Match match;

    public PlayerSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        super(protocolHandler, listeners);
    }

    public void enterMatch(Match match, PlayerSocket other) {
        this.match = match;
        this.otherPlayer = other;
        this.player = second;
    }

    public void enterMatchWaitingIsOver(PlayerSocket other) {
        this.otherPlayer = other;
        simpleResponse("waitingOver");
    }

    @Override
    public void onMessage(String text) {
        if ("iMThere".equals(text)) {
            send(match.fields.get(player).toJson());
            if (otherPlayer == null) {
                simpleResponse("wait");
            }
        } else if (otherPlayer == null) {
            simpleResponse("wait");
        } else if (match.whoSNext() != player) {
            simpleResponse("othersTurn");
        } else {
            Field.Cell shoot = match.shoot(player, text);
            send(respondHit(text, shoot, false));
            otherPlayer.send(respondHit(text, shoot, true));
            if (match.getWinner() == player) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gameOver", true);
                jsonObject.put("won", true);
                send(jsonObject.toString());
                otherPlayer.simpleResponse("gameOver");
            }
        }
    }

    private String respondHit(String position, Field.Cell shoot, boolean my) {
        JSONObject result = new JSONObject();
        if (my) {
            result.put("my", true);
        }
        result.put("status", shoot.name());
        result.put("hit", position);
        return result.toString();
    }

    @Override
    public void onClose(DataFrame frame) {
        super.onClose(frame);
        otherPlayer.simpleResponse("otherLeft");
    }

    void simpleResponse(String command) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(command, true);
        send(jsonObject.toString());
    }


    public void createMatch() {
        this.match = new Match();
        player = first;
    }

    public Match getMatch() {
        return match;
    }
}
