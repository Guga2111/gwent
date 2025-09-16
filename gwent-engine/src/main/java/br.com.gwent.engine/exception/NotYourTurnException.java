package br.com.gwent.engine.exception;

public class NotYourTurnException extends RuntimeException {
    public NotYourTurnException (Long id) {
        super ("Its not your turn, player id: " + id);
    }
}
