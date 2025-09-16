package br.com.gwent.engine.exception;

public class PlayerNotFoundInGameException extends RuntimeException {
    public PlayerNotFoundInGameException(Long id ) {
        super ("The player with ID: " + id + " does not exist!");
    }
}
