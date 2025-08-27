package br.com.gwent.engine.services.validator;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.structure.Player;

import java.util.UUID;

public class GameValidator {

    // players turn? the card he is player it exists on the hand? the player has already passed it?
    public void validatePlayCard (GameState gameState, Long playerId, UUID gameCardId) {

        if (!gameState.getCurrentPlayerId().equals(playerId)) {
            throw new RuntimeException(); //custom exception for not being the player's turn "NotYourTurnException"
        }

        Player player = gameState.getPlayerById(playerId);

        if(player == null) throw new RuntimeException(); // custom exception for playerNotfoundInTheGame or something like that

        if (player.isHasPassed()) throw new RuntimeException(); // custom exception for "PlayerHasAlreadyPassedException"

        if (player.getHand().stream()
                .noneMatch(card -> card.getInstanceId().equals(gameCardId))) {
            throw new RuntimeException(); // custom exception for "PlayerDoesNotHaveThisCardOnHandException"
        }
    }

    // players turn? the player has already passed it ?
    public void validatePassTurn (GameState gameState, Long playerId) {

        if (!gameState.getCurrentPlayerId().equals(playerId)) {
            throw new RuntimeException(); //custom exception for not being the player's turn "NotYourTurnException"
        }

        Player player = gameState.getPlayerById(playerId);

        if(player == null) throw new RuntimeException(); // custom exception for playerNotfoundInTheGame or something like that

        if (player.isHasPassed()) throw new RuntimeException(); // custom exception for "PlayerHasAlreadyPassedException"
    }
}
