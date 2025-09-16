package br.com.gwent.engine;

import br.com.gwent.engine.Game;
import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.Faction;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import br.com.gwent.engine.services.executor.GameActionExecuter;
import br.com.gwent.engine.services.flow.GameFlowManager;
import br.com.gwent.engine.services.validator.GameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameTest {

    @Mock
    private GameActionExecuter mockActionExecuter;
    @Mock
    private GameFlowManager mockFlowManager;
    @Mock
    private GameValidator mockValidator;

    private Game game;

    private static Deque<GameCard> createTestDeck (Faction faction) {
        return IntStream.range(0, 25)
                .mapToObj(i -> {
                    Card cardTemplate = Card.builder()
                            .id(faction.name().toLowerCase() + "_card" + i)
                            .name(faction.name() + " Unity " + (i + 1))
                            .basePower( (i % 5) + 1)
                            .faction(faction)
                            .build();
                    return new GameCard(cardTemplate);
                })
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    @BeforeEach
    void setUp () {

        Deque<GameCard> p1Deck = createTestDeck(Faction.NORTHERN_REALMS);
        Deque<GameCard> p2Deck = createTestDeck(Faction.NILFGAARD);

        game = new Game(
                p1Deck, p2Deck, 1L, 2L,
                mockActionExecuter, mockFlowManager, mockValidator
        );
    }

    @Test
    void playCard_Should_CallValidatorExecutorAndFlowManagerInCorrectOrder () {
        //arrange
        Long playerId = 1L;
        UUID cardId = UUID.randomUUID();
        RowType targetRow = RowType.INFANTRY;
        GameState currentState = game.getGameState();

        // Act (Ação)
        game.playCard(cardId, targetRow, playerId);

        //assert
        // Setting the order
        InOrder inOrder = inOrder(mockValidator, mockActionExecuter, mockFlowManager);

        // 1. verify if validator is the first
        inOrder.verify(mockValidator).validatePlayCard(currentState, playerId, cardId);

        // 2. verify if action executer comes next
        inOrder.verify(mockActionExecuter).playCard(currentState, playerId, cardId, targetRow, null);

        // 3. verify if
        inOrder.verify(mockFlowManager).advanceTurn(currentState);
    }

    @Test
    void passTurn_ShouldCallValidatorAndFlowManagerInCorrectOrder () {
        //arrange
        Long playerId = 1L;
        GameState currentState = game.getGameState();
        InOrder inOrder = inOrder(mockValidator, mockFlowManager);

        //act
        game.passTurn(playerId);

        //assert
        // verify the correct order
        inOrder.verify(mockValidator).validatePassTurn(currentState, playerId);
        inOrder.verify(mockFlowManager).executePass(currentState, playerId);
    }
}