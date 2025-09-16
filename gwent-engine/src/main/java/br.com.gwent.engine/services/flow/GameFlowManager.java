package br.com.gwent.engine.services.flow;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;

public class GameFlowManager {

    private final RoundResultService roundResultService;

    public GameFlowManager () {
        roundResultService = new RoundResultService();
    }

    // remains to add "verify if the opponent has passed his turn and if it does the currentPlayerId must still the same!"
    public void advanceTurn (GameState gameState) {

        Player opponent = gameState.getOpponentOf(gameState.getCurrentPlayerId());

        if (opponent != null && !opponent.isHasPassed()) {
            gameState.setCurrentPlayerId(opponent.getUserId());
            gameState.setNumberOfMoves(gameState.getNumberOfMoves() + 1);
        } else {
            gameState.setNumberOfMoves(gameState.getNumberOfMoves() + 1);
        }
        // for player not found on the specific Game "PlayerNotFoundInGameException"
    }

    public void executePass (GameState gameState, Long passingPlayerId) {

        Player passingPlayer = gameState.getPlayerById(passingPlayerId);
        Player opponent = gameState.getOpponentOf(passingPlayerId);

        if (passingPlayer != null && gameState.getCurrentRound() <= 3) {
            passingPlayer.setHasPassed(true);

            if (opponent.isHasPassed()) {
                endRound(gameState);
            } else {
                advanceTurn(gameState);
            }
        }
    }

    public void endRound (GameState gameState) {

        RoundResult result = roundResultService.determineRoundWinner(
                gameState.getPlayer1(),
                gameState.getPlayer2()
        );

        if (result.winnerId() != null) {
            Player winner = gameState.getPlayerById(result.winnerId());
            winner.setRoundsWon(winner.getRoundsWon() + 1);
        }

        gameState.setGameStatus(GameStatus.ROUND_OVER);

        checkForGameEnd(gameState);

    }

    private void checkForGameEnd (GameState gameState) {

        Player p1 = gameState.getPlayer1();
        Player p2 = gameState.getPlayer2();

        if (p1.getRoundsWon() == 2) {
            gameState.setGameWinnerId(p1.getUserId());
            gameState.setGameStatus(GameStatus.GAME_FINISHED);
        } else if (p2.getRoundsWon() == 2) {
            gameState.setGameWinnerId(p2.getUserId());
            gameState.setGameStatus(GameStatus.GAME_FINISHED);
        } else {
            setForNextRound(gameState);
        }
    }

    private void setForNextRound (GameState gameState) {

        gameState.setCurrentPlayerId(gameState.getOpponentOf(gameState.getCurrentPlayerId()).getUserId());
        gameState.setCurrentRound(gameState.getCurrentRound() + 1);

        gameState.getPlayer1().getBoard().clearBoard(gameState.getPlayer1());
        gameState.getPlayer2().getBoard().clearBoard(gameState.getPlayer2());

        gameState.getPlayer1().setHasPassed(false);
        gameState.getPlayer2().setHasPassed(false);

        gameState.setGameStatus(GameStatus.ROUND_IN_PROGRESS);
    }
}
