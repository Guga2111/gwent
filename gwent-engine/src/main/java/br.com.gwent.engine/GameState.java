package br.com.gwent.engine;

import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data @Builder
public class GameState {
    private final UUID gameId;

    private int currentRound;
    private Long currentPlayerId;
    private GameStatus gameStatus;

    private Player player1;
    private Player player2;

    private Long gameWinnerId;

    public Player getPlayerById (Long playerId) {

        if (playerId == null) {
            throw new RuntimeException(); //custom exception
        }

        if (player1 != null && player1.getUserId().equals(playerId)) {
            return player1;
        }

        if (player2 != null && player2.getUserId().equals(playerId)) {
            return player2;
        }

        return null;
    }

    public Player getOpponentOf (Long playerId) {

        if (playerId == null) {
            throw new RuntimeException(); //custom exception
        }

        if ((player1 != null && player2 != null) && player1.getUserId().equals(playerId)) {
            return player2;
        }

        if ((player2 != null && player1 != null) && player2.getUserId().equals(playerId)) {
            return player1;
        }

        return null;
    }
}
