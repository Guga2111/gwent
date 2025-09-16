package br.com.gwent.engine.services.validator;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.exception.NotYourTurnException;
import br.com.gwent.engine.exception.PlayerDoesNotHaveThisCardOnHandException;
import br.com.gwent.engine.exception.PlayerHasAlreadyPassedException;
import br.com.gwent.engine.exception.PlayerNotFoundInGameException;
import br.com.gwent.engine.pojo.structure.Player;

import java.util.UUID;

public class GameValidator {

    // players turn? the card he is player it exists on the hand? the player has already passed it?
    public void validatePlayCard (GameState gameState, Long playerId, UUID gameCardId) {

        Player player = gameState.getPlayerById(playerId);
        if(player == null) throw new PlayerNotFoundInGameException(playerId);

        if (!gameState.getCurrentPlayerId().equals(playerId)) {
            throw new NotYourTurnException(playerId);
        }

        if (player.isHasPassed()) throw new PlayerHasAlreadyPassedException(playerId);

        if (player.getHand().stream()
                .noneMatch(card -> card.getInstanceId().equals(gameCardId))) {
            throw new PlayerDoesNotHaveThisCardOnHandException(playerId, gameCardId);
        }
    }

    // players turn? the player has already passed it ?
    public void validatePassTurn (GameState gameState, Long playerId) {

        Player player = gameState.getPlayerById(playerId);
        if(player == null) throw new PlayerNotFoundInGameException(playerId);

        if (!gameState.getCurrentPlayerId().equals(playerId)) {
            throw new NotYourTurnException(playerId);
        }

        if (player.isHasPassed()) throw new PlayerHasAlreadyPassedException(playerId);
    }
}
