package de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game;

import org.junit.Test;

import java.util.Arrays;

import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Field.Rules.standard;
import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Match.Player.first;
import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Match.Player.second;
import static de.blau.research.niels.webSocketsPlayground.webSocketsBattleship.Game.Ship.Direction.horizontal;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MatchTest {
    @Test
    public void game() {
        Field fieldPlayer1 = new Field(Arrays.asList(new Ship(3, new Position(0, 0), horizontal)));
        Field fieldPlayer2 = new Field(Arrays.asList(new Ship(3, new Position(0, 0), horizontal)));
        Match match = new Match(fieldPlayer1, fieldPlayer2);
        match.shoot(match.whoSNext(), new Position(0, 0));
        match.shoot(match.whoSNext(), new Position(9, 9));

        match.shoot(match.whoSNext(), new Position(1, 0));
        match.shoot(match.whoSNext(), new Position(9, 9));

        assertThat(match.getWinner(), is(nullValue()));
        match.shoot(match.whoSNext(), new Position(2, 0));
        assertThat(match.getWinner(), is(first));
        assertThat(match.whoSNext(), is(nullValue()));
    }

    @Test
    public void RandomGame() {
        Match match = new Match();
        while (match.getWinner() == null) {
            match.shoot(match.whoSNext(), new Position(9, 9));
            match.shoot(match.whoSNext(), Position.random(standard));
            System.out.println("--------------------\n" + match.fields.get(first));
        }
        assertThat(match.getWinner(), is(second));


    }
}
