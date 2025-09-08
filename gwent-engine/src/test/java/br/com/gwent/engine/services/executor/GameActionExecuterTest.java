package br.com.gwent.engine.services.executor;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.exception.CardNotPresentInHandException;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.board.BoardRow;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameActionExecuterTest {

    private GameActionExecuter gameActionExecuter;
    private static final Long PLAYER_ONE_ID = 1L;
    private static final Long PLAYER_TWO_ID = 2L;

    private GameState gameState;

    private List<GameCard> createTestHand(int numberOfCards) {
        return IntStream.range(0, numberOfCards)
                .mapToObj(i -> {
                    Card template = Card.builder().id("test_card_" + i).name("Test Card " + i).basePower(i + 1).build();
                    return new GameCard(template);
                })
                .collect(Collectors.toList());
    }

    @Before
    public void setUp() throws Exception {

        List<GameCard> player1Hand = createTestHand(5);
        List<GameCard> player2Hand = createTestHand(5);

        Player player1 = new Player(PLAYER_ONE_ID, new ArrayDeque<>(), player1Hand);
        Player player2 = new Player(PLAYER_TWO_ID, new ArrayDeque<>(), player2Hand);

        gameActionExecuter = new GameActionExecuter();

        gameState = GameState.builder()
                .player1(player1)
                .player2(player2)
                .currentPlayerId(PLAYER_ONE_ID)
                .gameStatus(GameStatus.ROUND_IN_PROGRESS)
                .numberOfMoves(0)
                .currentRound(1)
                .build();
    }

    @Test
    @DisplayName("Play card should move the chosen card from the hand for the selected row")
    public void playCard_Should_MoveCardFromHandToBoard() {
        //arrange
        Player actingPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        UUID gameCardId = actingPlayer.getHand().get(0).getInstanceId();
        int previousNumberOfCardsInHand = actingPlayer.getHand().size();

        //act
        gameActionExecuter.playCard(gameState, gameState.getCurrentPlayerId(), gameCardId, actingPlayer.getBoard().getRow(RowType.INFANTRY).getRowType(), null);

        //assert
        int actualNumberCardsInBoardRow = actingPlayer.getBoard().getRow(RowType.INFANTRY).getCards().size();
        assertEquals(1, actualNumberCardsInBoardRow);

        int actualNumberCardsInHand = actingPlayer.getHand().size();
        assertEquals(previousNumberOfCardsInHand - 1, actualNumberCardsInHand);
    }

    @Test
    @DisplayName("Play card should add points for the specific row and on total points (board)")
    public void playCard_Should_AddPointsForTheRowAndBoard () {
        //arrange
        Player actingPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        UUID gameCardId = actingPlayer.getHand().get(0).getInstanceId();

        int expectedCardPoints = actingPlayer.getHand().get(0).getCardTemplate().getBasePower();
        int previousTotalPoints = actingPlayer.getBoard().getTotalPoints();

        //act
        gameActionExecuter.playCard(gameState, gameState.getCurrentPlayerId(), gameCardId, actingPlayer.getBoard().getRow(RowType.INFANTRY).getRowType(), null);

        //assert
        int actualTotalPoints = gameState.getPlayerById(actingPlayer.getUserId()).getBoard().getTotalPoints();
        assertEquals(expectedCardPoints, actualTotalPoints);
        assertNotEquals(previousTotalPoints, actualTotalPoints);
    }

    @Test
    @DisplayName("Play a non existing card in players hand should throw a CardNotPresentInHandException with the correct custom message")
    public void playCard_Should_ThrowExceptionWhenCardIsNotInHand () {
        //arrange
        UUID nonExistingCard = UUID.randomUUID();

        Player actingPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        boolean cardExists = actingPlayer.getHand().stream()
                .anyMatch(card -> card.getInstanceId().equals(nonExistingCard));

        assertFalse("Pre Condition: The card ID of test must not exists in the players hand.", cardExists);

        //act & assert
        CardNotPresentInHandException exception = assertThrows(CardNotPresentInHandException.class, () -> {
            gameActionExecuter.playCard(gameState, gameState.getCurrentPlayerId(), nonExistingCard, RowType.INFANTRY, null);
        });

        String expectedMessage = "The card with ID: " + nonExistingCard + " was not found in the player's hand";
        assertEquals(expectedMessage, exception.getMessage());
    }
}