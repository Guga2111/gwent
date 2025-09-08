package br.com.gwent.engine.exception;

import java.util.UUID;

public class CardNotPresentInHandException extends RuntimeException {
    public CardNotPresentInHandException (UUID gameCardId) {
        super ("The card with ID: " + gameCardId + " was not found in the player's hand");
    }
}
