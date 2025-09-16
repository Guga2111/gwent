package br.com.gwent.engine.core;

import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameInitializeUtilsTest {

    private Deque<GameCard> playerOneDeck;
    private Deque<GameCard> playerTwoDeck;
    private static final Long PLAYER_ONE_ID = 1L;
    private static final Long PLAYER_TWO_ID = 2L;

    @BeforeEach
    void setUp() {
        // Deck with 25 mocked cards
        playerOneDeck = IntStream.range(0, 25)
                .mapToObj(i -> mock(GameCard.class))
                .collect(Collectors.toCollection(ArrayDeque::new));

        playerTwoDeck = IntStream.range(0, 25)
                .mapToObj(i -> mock(GameCard.class))
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    @Test
    void initializeNewGame_Should_ReturnCorrectlyConfiguredTheStateOfGame() {
        //arrange
        int initialDeckSize = playerOneDeck.size();

        //act
        GameState gameState = GameInitializeUtils.initializeNewGame(
                playerOneDeck, playerTwoDeck, PLAYER_ONE_ID, PLAYER_TWO_ID
        );

        //assert
        assertNotNull(gameState);
        assertEquals(GameStatus.ROUND_IN_PROGRESS, gameState.getGameStatus());
        assertEquals(1, gameState.getCurrentRound());

        // Verify Player 2 1
        Player player1 = gameState.getPlayer1();
        assertNotNull(player1);
        assertEquals(PLAYER_ONE_ID, player1.getUserId());
        assertEquals(10, player1.getHand().size(), "The hand of player 1 should have 10 cards.");
        assertEquals(initialDeckSize - 10, player1.getDeck().size(), "The deck of player 1 should have the remaining cards.");

        // Verify Player 2
        Player player2 = gameState.getPlayer2();
        assertNotNull(player2);
        assertEquals(PLAYER_TWO_ID, player2.getUserId());
        assertEquals(10, player2.getHand().size(), "The hand of player 2 should have 10 cards.");
        assertEquals(initialDeckSize - 10, player2.getDeck().size(), "The deck of player 2 should have the remaining cards.");

        // Verify the initial player (random)
        boolean isPlayerOneOrTwoStarting = gameState.getCurrentPlayerId().equals(PLAYER_ONE_ID) ||
                gameState.getCurrentPlayerId().equals(PLAYER_TWO_ID);
        assertTrue(isPlayerOneOrTwoStarting, "The player should be player 1 or player 2");
    }
}