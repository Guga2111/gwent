package br.com.gwent.engine.exception;

import java.util.UUID;

public class PlayerDoesNotHaveThisCardOnHandException extends RuntimeException {
    public PlayerDoesNotHaveThisCardOnHandException (Long playerId, UUID gameCardId) {
        super ("The card with UUID: " + gameCardId + " does not exist in the hand of the player with ID: " + playerId);
    }
}
