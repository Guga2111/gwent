package br.com.gwent.engine.services.flow;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameFlowManager {

    // remains to add "verify if the opponent has passed his turn and if it does the currentPlayerId must still the same!"
    public void advanceTurn (GameState gameState) {

        Player opponent = gameState.getOpponentOf(gameState.getCurrentPlayerId());

        if (opponent != null && !opponent.isHasPassed()) {
            gameState.setCurrentPlayerId(opponent.getUserId());
            gameState.setNumberOfMoves(gameState.getNumberOfMoves() + 1);
        } else {
            gameState.setNumberOfMoves(gameState.getNumberOfMoves() + 1);
        }

        throw new RuntimeException(); // for player not found on the specific Game "PlayerNotFoundInGameException"
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
        gameState.setGameStatus(GameStatus.ROUND_OVER);
        gameState.setCurrentPlayerId(gameState.getOpponentOf(gameState.getCurrentPlayerId()).getUserId());
        gameState.setCurrentRound(gameState.getCurrentRound() + 1);
        // remains to determine the winner of the round ( another service will )
        // this will be the future endRound function
    }
}
