package br.com.gwent.engine.exception;

public class PlayerHasAlreadyPassedException extends RuntimeException {
    public PlayerHasAlreadyPassedException (Long id) {
        super ("The player with ID: " + id + " has already passed it!");
    }
}
