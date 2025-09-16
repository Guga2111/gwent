package br.com.gwent.engine.services.flow;

import br.com.gwent.engine.pojo.structure.Player;

public class RoundResultService {

    public RoundResult determineRoundWinner (Player playerOne, Player playerTwo) {

        int score1 = playerOne.getBoard().getTotalPoints();
        int score2 = playerTwo.getBoard().getTotalPoints();

        Long winnerId = null;

        if (score1 > score2) {
            winnerId = playerOne.getUserId();
        } else if (score2 > score1) {
            winnerId = playerTwo.getUserId();
        }

        return new RoundResult(winnerId, score1, score2);
    }
}
