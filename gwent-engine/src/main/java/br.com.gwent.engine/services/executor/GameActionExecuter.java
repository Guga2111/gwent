package br.com.gwent.engine.services.executor;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.abilities.TargetInfo;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.board.BoardRow;
import br.com.gwent.engine.pojo.structure.card.GameCard;

import java.util.UUID;

public class GameActionExecuter {

    public void playCard (GameState gameState, Long playerId, UUID gameCardId, RowType targetRow, TargetInfo targetInfo) {

        Player actingPlayer = gameState.getPlayerById(playerId);

        BoardRow destinationRow = actingPlayer.getBoard().getRow(targetRow);

        GameCard cardToPlay = actingPlayer.removeCardFromHand(gameCardId);

        destinationRow.addCardToRow(cardToPlay);

        //remains the use of targetInfo (interface for abilities)

    }

    // add ways to destroy card (with a card to destroy or an ability), ways to draw cards (purchase those card from deck), etc...
}
