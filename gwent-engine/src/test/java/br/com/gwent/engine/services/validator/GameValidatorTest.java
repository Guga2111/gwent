package br.com.gwent.engine.services.validator;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.exception.*;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameValidatorTest {

    private GameValidator gameValidator;
    private GameState gameState;

    private static final Long PLAYER_ONE_ID = 1L;
    private static final Long PLAYER_TWO_ID = 2L;

    private List<GameCard> createTestHand(int numberOfCards) {
        return IntStream.range(0, numberOfCards)
                .mapToObj(i -> {
                    Card template = Card.builder().id("test_card_" + i).name("Test Card " + i).basePower(i + 1).build();
                    return new GameCard(template);
                })
                .collect(Collectors.toList());
    }

    @BeforeEach
    public void setUp() throws Exception {

        Player player1 = new Player(PLAYER_ONE_ID, new ArrayDeque<>(), createTestHand(5));
        Player player2 = new Player(PLAYER_TWO_ID, new ArrayDeque<>(), createTestHand(5));

        gameValidator = new GameValidator();

        gameState = GameState.builder()
                .gameStatus(GameStatus.ROUND_IN_PROGRESS)
                .player1(player1)
                .player2(player2)
                .currentPlayerId(PLAYER_ONE_ID)
                .currentRound(1)
                .numberOfMoves(0)
                .build();
    }

    @Test
    @DisplayName("Validate PlayCard should throw a NotYourTurnException when the currentPlayerId on the gameState" +
            "it's not the same that the playerId that is calling the function.")
    public void validatePlayCard_Should_ThrowExceptionWhenItsNotYourTurn () {
        //arrange
        UUID existingGameCard = gameState.getPlayer1().getHand().get(0).getInstanceId();
        gameState.setCurrentPlayerId(PLAYER_TWO_ID);

        //act (call the method)
        NotYourTurnException exception = assertThrows(NotYourTurnException.class, () -> {
            gameValidator.validatePlayCard(gameState, PLAYER_ONE_ID, existingGameCard);
        });

        //assert
        String expectedMessage = "Its not your turn, player id: " + PLAYER_ONE_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Validate PlayCard should throw a PlayerNotFoundInGameException when there is no player with" +
            "this ID on the game state.")
    public void validatePlayCard_Should_ThrowExceptionWhenThePlayerIsNotFoundInGame () {
        //arrange
        Player nonOnGamePlayer = new Player(3L, new ArrayDeque<>(), createTestHand(5));
        gameState.setCurrentPlayerId(nonOnGamePlayer.getUserId());

        //act
        PlayerNotFoundInGameException exception = assertThrows(PlayerNotFoundInGameException.class, () -> {
            gameValidator.validatePlayCard(gameState, nonOnGamePlayer.getUserId(), nonOnGamePlayer.getHand().get(0).getInstanceId());
        });

        //assert
        String expectedMessage = "The player with ID: " + nonOnGamePlayer.getUserId() + " does not exist!";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Validate PlayCard should throw a PlayerHasAlreadyPassedException when the current player has already" +
            "passed it turn.")
    public void validatePlayCard_Should_ThrowExceptionWhenThePlayerHasAlreadyPassedIt () {
        //arrange
        Player currentPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        currentPlayer.setHasPassed(true);
        UUID randomCard = currentPlayer.getHand().get(0).getInstanceId();

        //act & assert
        PlayerHasAlreadyPassedException exception = assertThrows(PlayerHasAlreadyPassedException.class, () -> {
            gameValidator.validatePlayCard(gameState, currentPlayer.getUserId(), randomCard);
        });

        //assert
        String expectedMessage = "The player with ID: " + currentPlayer.getUserId() + " has already passed it!";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Validate PlayCard should throw a PlayerDOesNotHaveThisCardOnHandException when the player plays a card" +
            "that not belong to his hand or does not exist there.")
    public void validatePlayCard_Should_ThrowExceptionWhenTheCardChosenDoesNotExistInPlayersHand () {
        //arrange
        Player currentPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        UUID nonExistingCard = UUID.randomUUID();

        //act & assert
        PlayerDoesNotHaveThisCardOnHandException exception = assertThrows(PlayerDoesNotHaveThisCardOnHandException.class, () -> {
            gameValidator.validatePlayCard(gameState, currentPlayer.getUserId(), nonExistingCard);
        });

        //assert
        String expectedMessage = "The card with UUID: " + nonExistingCard + " does not exist in the hand of the player with ID: " + currentPlayer.getUserId();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Validate PassTurn should throw a PlayerNotFoundInGameException when there is no player with" +
            "this ID on the game state.")
    public void validatePassTurn_Should_ThrowExceptionWhenPlayerIsNotFoundInGame () {
        //arrange
        Player nonOnGamePlayer = new Player(3L, new ArrayDeque<>(), createTestHand(5));
        gameState.setCurrentPlayerId(nonOnGamePlayer.getUserId());

        //act
        PlayerNotFoundInGameException exception = assertThrows(PlayerNotFoundInGameException.class, () -> {
            gameValidator.validatePassTurn(gameState, nonOnGamePlayer.getUserId());
        });

        //assert
        String expectedMessage = "The player with ID: " + nonOnGamePlayer.getUserId() + " does not exist!";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Validate PassTurn should throw a NotYourTurnException when the currentPlayerId on the gameState" +
            "it's not the same that the playerId that is calling the function.")
    public void validatePassTurn_Should_ThrowExceptionWhenItsNotYourTurn () {
        //arrange
        gameState.setCurrentPlayerId(PLAYER_TWO_ID);

        //act (call the method)
        NotYourTurnException exception = assertThrows(NotYourTurnException.class, () -> {
            gameValidator.validatePassTurn(gameState, PLAYER_ONE_ID);
        });

        //assert
        String expectedMessage = "Its not your turn, player id: " + PLAYER_ONE_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Validate PassTurn should throw a PlayerHasAlreadyPassedException when the current player has already" +
            "passed it turn.")
    public void validatePassTurn_Should_ThrowExceptionWhenPlayHasAlreadyPassIt () {
        //arrange
        Player currentPlayer = gameState.getPlayerById(gameState.getCurrentPlayerId());
        currentPlayer.setHasPassed(true);

        //act & assert
        PlayerHasAlreadyPassedException exception = assertThrows(PlayerHasAlreadyPassedException.class, () -> {
            gameValidator.validatePassTurn(gameState, currentPlayer.getUserId());
        });

        //assert
        String expectedMessage = "The player with ID: " + currentPlayer.getUserId() + " has already passed it!";
        assertEquals(expectedMessage, exception.getMessage());
    }
}