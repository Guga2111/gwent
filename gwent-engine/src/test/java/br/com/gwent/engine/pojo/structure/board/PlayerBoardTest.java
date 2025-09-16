package br.com.gwent.engine.pojo.structure.board;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.CardType;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerBoardTest {

    private PlayerBoard playerBoard;

    @Mock
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        playerBoard = new PlayerBoard();
    }

    @Test
    public void initialize_Should_NotHaveEmptyRows () {

        assertNotNull(playerBoard.getRows());
        assertEquals(RowType.values().length, playerBoard.getRows().size());

        for (RowType type : RowType.values()) {
            assertNotNull(playerBoard.getRow(type), "Deveria existir uma fileira para o tipo " + type);
            assertEquals(type, playerBoard.getRow(type).getRowType(), "O tipo da fileira não corresponde ao esperado.");
        }
    }

    @Test
    public void getTotalPoints_Should_ReturnAllPointsFromAllRows () {
        //arrange
        GameCard card1 = mock(GameCard.class);
        when(card1.getCurrentPower()).thenReturn(5);

        GameCard card2 = mock(GameCard.class);
        when(card2.getCurrentPower()).thenReturn(3);

        GameCard card3 = mock(GameCard.class);
        when(card3.getCurrentPower()).thenReturn(1);

        playerBoard.getRow(RowType.INFANTRY).addCardToRow(card1);
        playerBoard.getRow(RowType.ARTILLERY).addCardToRow(card2);
        playerBoard.getRow(RowType.SIEGE).addCardToRow(card3);

        int totalPoints = playerBoard.getTotalPoints();

        assertEquals(9, totalPoints);
    }

    @Test
    public void getTotalPoints_Should_Return0WhenTheBoardIsClear () {

        int totalPoints = playerBoard.getTotalPoints();

        assertEquals(0, totalPoints);
    }

    @Test
    public void clearBoard_Should_MoveAllCardsFromTheBoardToTheDiscardPill () {
        List<GameCard> discard = new ArrayList<>();
        when(mockPlayer.getDiscard()).thenReturn(discard);

        GameCard card1 = mock(GameCard.class);
        GameCard card2 = mock(GameCard.class);

        playerBoard.getRow(RowType.INFANTRY).addCardToRow(card1);
        playerBoard.getRow(RowType.ARTILLERY).addCardToRow(card2);

        playerBoard.clearBoard(mockPlayer);

        assertEquals(2, discard.size());
        assertTrue(discard.contains(card1));
        assertTrue(discard.contains(card2));

        assertTrue(playerBoard.getRow(RowType.INFANTRY).getCards().isEmpty());
        assertTrue(playerBoard.getRow(RowType.ARTILLERY).getCards().isEmpty());
    }
}