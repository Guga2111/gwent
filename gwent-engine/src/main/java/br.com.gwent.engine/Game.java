package br.com.gwent.engine;

import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.card.GameCard;

import java.util.*;


public class Game {
    private GameState gameState;

    public Game (Deque<GameCard> playerOneDeck, Deque<GameCard> playerTwoDeck, Long playerOneId, Long playerTwoId) {
        this.gameState = GameInitializeUtils.initializeNewGame(playerOneDeck, playerTwoDeck, playerOneId, playerTwoId);
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
