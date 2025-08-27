package br.com.gwent.engine;

import br.com.gwent.engine.pojo.abilities.TargetInfo;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.board.BoardRow;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameActionExecuter {

    public GameState playCard (GameState gameState, Long playerId, UUID gameCardId, RowType targetRow, TargetInfo targetInfo) {

        Player actingPlayer = gameState.getPlayerById(playerId); // find player in gamestate

        BoardRow destinationRow = actingPlayer.getBoard().getRow(targetRow); // find the selected row

        GameCard cardToPlay = actingPlayer.removeCardFromHand(gameCardId); // find the correct card on the hand based on the UUID and remove it

        destinationRow.addCardToRow(cardToPlay); // add the selected card on the selected row

        //remains the use of targetInfo (interface for abilities)

        return gameState;
    }

    // add ways to destroy card (with a card to destroy or an ability), ways to draw cards (purchase those card from deck), etc...
}
