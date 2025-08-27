package br.com.gwent.engine.core;

import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.enums.RoundNumber;
import br.com.gwent.engine.pojo.structure.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder
public class GameState {
    private final UUID gameId = UUID.randomUUID();

    private int currentRound; // only have 3 rounds maximum
    private int numberOfMoves; // number of play's of each player

    private Long currentPlayerId;
    private GameStatus gameStatus;

    private Player player1;
    private Player player2;

    private Long gameWinnerId;
    //map first second third the winner of the round by the ID of the player
    private Map<RoundNumber, Long> roundWinners = new EnumMap<>(RoundNumber.class);

    public Player getPlayerById (Long playerId) {

        if (playerId == null) {
            throw new RuntimeException(); //custom exception it can be IllegalStateException maybe or BadRequest
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
            throw new RuntimeException(); //custom exception it can be IllegalStateException maybe or BadRequest
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
