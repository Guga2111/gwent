package br.com.gwent.engine.pojo.structure;

import br.com.gwent.engine.exception.CardNotPresentInHandException;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private static final Long PLAYER_ID = 1L;

    private Player player;

    private List<GameCard> createTestHand(int numberOfCards) {
        return IntStream.range(0, numberOfCards)
                .mapToObj(i -> {
                    Card template = Card.builder().id("test_card_" + i).name("Test Card " + i).basePower(i + 1).build();
                    return new GameCard(template);
                })
                .collect(Collectors.toList());
    }

    @BeforeEach
    public void setUp() {
        player = new Player(PLAYER_ID, new ArrayDeque<>(), createTestHand(5));
    }

    @Test
    public void removeCardFromHand_Should_RemoveTheCardFromTheHand () {
        //arrange
        int previousSizeOfHand = player.getHand().size();

        //act
        player.removeCardFromHand(player.getHand().get(0).getInstanceId());

        assertEquals(previousSizeOfHand - 1, player.getHand().size());
    }

    @Test
    public void removeCardFromHand_Should_RetrieveAGameCard () {
        GameCard card = null;

        card = player.removeCardFromHand(player.getHand().get(0).getInstanceId());

        assertNotNull(card);
    }

    @Test
    public void removeCardFromHand_Should_ThrowExceptionWhenTheCardDoesNotExistInHand () {

        CardNotPresentInHandException exception = assertThrows(CardNotPresentInHandException.class ,() -> {
           player.removeCardFromHand(UUID.randomUUID());
        });
    }

}