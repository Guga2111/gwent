package br.com.gwent.engine.services.flow;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.board.BoardRow;
import br.com.gwent.engine.pojo.structure.board.PlayerBoard;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameFlowManagerTest {

    @Mock
    private GameState mockGameState;

    @Spy
    private GameFlowManager gameFlowManager;
    private GameState gameState;

    private static final Long PLAYER_ONE_ID = 1L;
    private static final Long PLAYER_TWO_ID = 2L;

    private GameCard createTestCard() {
        Card template = Card.builder()
                .id("test_card_1")
                .name("Test Card")
                .basePower(5) // Or any fixed power you want
                .build();
        return new GameCard(template);
    }

    @BeforeEach
    public void setUp() throws Exception {

        Player player1 = new Player(PLAYER_ONE_ID, new ArrayDeque<>(), new ArrayList<>());
        Player player2 = new Player(PLAYER_TWO_ID, new ArrayDeque<>(), new ArrayList<>());

        gameState = GameState.builder()
                .gameStatus(GameStatus.ROUND_IN_PROGRESS)
                .currentRound(1)
                .numberOfMoves(0)
                .player1(player1)
                .player2(player2)
                .currentPlayerId(PLAYER_ONE_ID)
                .build();
    }

    @Test
    public void advanceTurn_Should_PassTheTurnForTheOpponentOfTheCurrentPlayer () {
        //arrange
        Player oppositePlayer = gameState.getPlayerById(PLAYER_TWO_ID);

        //act
        gameFlowManager.advanceTurn(gameState);

        //assert
        assertEquals(oppositePlayer, gameState.getPlayerById(gameState.getCurrentPlayerId()));
    }

    @Test
    public void advanceTurn_ShouldNot_PassTheTurnIfTheOppositePlayerHaveAlreadyPassedIt () {
        //arrange
        Player currentPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        Player oppositePlayer = gameState.getPlayerById(gameState.getOpponentOf(gameState.getCurrentPlayerId()).getUserId());
        oppositePlayer.setHasPassed(true);

        //act
        gameFlowManager.advanceTurn(gameState);

        //assert
        assertEquals(currentPlayer.getUserId(), gameState.getCurrentPlayerId());
    }

    @Test
    public void advanceTurn_Should_HaveOneMoreInTheNumberOfMoves () {
        //arrange
        int previousNumberOfMoves = gameState.getNumberOfMoves();

        //act
        gameFlowManager.advanceTurn(gameState);

        //assert
        assertEquals(previousNumberOfMoves + 1, gameState.getNumberOfMoves());
    }

    @Test
    public void executePass_Should_AdvanceTurnWhenOpponentHasNotPassed () {

        Player opponent = gameState.getOpponentOf(gameState.getCurrentPlayerId());
        opponent.setHasPassed(false);

        doNothing().when(gameFlowManager).advanceTurn(any(GameState.class));

        gameFlowManager.executePass(gameState, gameState.getCurrentPlayerId());

        verify(gameFlowManager).advanceTurn(gameState);
        verify(gameFlowManager, never()).endRound(gameState);
    }

    @Test
    void executePass_Should_EndRoundWhenOpponentHasPassed() {
        //arrange
        Player opponent = gameState.getOpponentOf(gameState.getCurrentPlayerId());
        opponent.setHasPassed(true);

        doNothing().when(gameFlowManager).endRound(any(GameState.class));

        //act
        gameFlowManager.executePass(gameState, gameState.getCurrentPlayerId());

        //assert
        verify(gameFlowManager).endRound(gameState);
        verify(gameFlowManager, never()).advanceTurn(gameState);
    }

    @Test
    public void endRound_Should_SetStatusToGameOver () {
        //act
        gameFlowManager.endRound(gameState);

        //assert
        assertEquals(GameStatus.ROUND_OVER, gameState.getGameStatus());
    }

    @Test
    public void endRound_Should_SetTheCurrentPlayerToTheNextInTheOrder () {
        //arrange
        Player nextPlayer = gameState.getOpponentOf(gameState.getCurrentPlayerId());

        //act
        gameFlowManager.endRound(gameState);

        //assert
        assertEquals(nextPlayer, gameState.getPlayerById(gameState.getCurrentPlayerId()));
    }

    @Test
    public void endRound_Should_AddPlusOneToRoundCount () {
        //arrange
        int previousCountOfRounds = gameState.getCurrentRound();

        //act
        gameFlowManager.endRound(gameState);

        //assert
        assertEquals(previousCountOfRounds + 1, gameState.getCurrentRound());
    }

    @Test
    public void endRound_Should_ClearBothPlayersBoards () {
        //arrange
        gameState.getPlayer1().getBoard().getRow(RowType.INFANTRY).addCardToRow(createTestCard());
        gameState.getPlayer1().getBoard().getRow(RowType.ARTILLERY).addCardToRow(createTestCard());
        gameState.getPlayer1().getBoard().getRow(RowType.SIEGE).addCardToRow(createTestCard());

        gameState.getPlayer2().getBoard().getRow(RowType.INFANTRY).addCardToRow(createTestCard());
        gameState.getPlayer2().getBoard().getRow(RowType.ARTILLERY).addCardToRow(createTestCard());
        gameState.getPlayer2().getBoard().getRow(RowType.SIEGE).addCardToRow(createTestCard());

        //act
        gameFlowManager.endRound(gameState);

        //assert
        for (BoardRow row : gameState.getPlayer1().getBoard().getRows().values()) {
            assertTrue(row.getCards().isEmpty());
        }

        for (BoardRow row : gameState.getPlayer2().getBoard().getRows().values()) {
            assertTrue(row.getCards().isEmpty());
        }
    }
}