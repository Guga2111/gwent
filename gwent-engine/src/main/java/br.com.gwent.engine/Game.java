package br.com.gwent.engine;

import br.com.gwent.engine.core.GameInitializeUtils;
import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import br.com.gwent.engine.services.executor.GameActionExecuter;
import br.com.gwent.engine.services.flow.GameFlowManager;
import br.com.gwent.engine.services.validator.GameValidator;

import java.util.*;


public class Game {
    private GameState gameState;

    private final GameActionExecuter actionExecuter;
    private final GameFlowManager flowManager;
    private final GameValidator validator;

    public Game (Deque<GameCard> playerOneDeck, Deque<GameCard> playerTwoDeck, Long playerOneId, Long playerTwoId) {

        this.actionExecuter = new GameActionExecuter();
        this.flowManager = new GameFlowManager();
        this.validator = new GameValidator();

        this.gameState = GameInitializeUtils.initializeNewGame(playerOneDeck, playerTwoDeck, playerOneId, playerTwoId);
    }

    public GameState playCard (UUID gameCardId, RowType targetRow, Long playerId) {

        validator.validatePlayCard(gameState, playerId, gameCardId);

        actionExecuter.playCard(gameState, playerId, gameCardId, targetRow, null);

        flowManager.advanceTurn(gameState);

        return gameState;
    }

    public GameState passTurn (Long playerId) {

        validator.validatePassTurn(gameState, playerId);

        flowManager.executePass(gameState, playerId);

        return gameState;
    }

    public GameState useLeaderCard () {
        return gameState;
    }

}
