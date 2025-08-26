package br.com.gwent.engine;

import br.com.gwent.engine.pojo.structure.card.GameCard;

import java.util.Deque;

public class Game {
    private GameState gameState;

    public Game (Deque<GameCard> playerOneDeck, Long playerOneId, Deque<GameCard> playerTwoDeck, Long playerTwoId) {

    }

    public GameState playCard () {
        return gameState;
    }

    public GameState passTurn () {
        return gameState;
    }

    public GameState useLeaderCard () {
        return gameState;
    }
}
