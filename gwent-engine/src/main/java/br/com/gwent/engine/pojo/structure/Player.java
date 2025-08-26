package br.com.gwent.engine.pojo.structure;

import br.com.gwent.engine.pojo.structure.card.GameCard;
import lombok.Data;

import java.util.*;

@Data
public class Player {
    private final Long userId;
    private boolean hasPassed = false;
    private List<GameCard> hand;
    private Deque<GameCard> deck;

    public Player (Long userId, Deque<GameCard> deck) {
        this.userId = userId;
        this.deck = shuffleDeck(deck);
    }

    private Deque<GameCard> shuffleDeck (Deque<GameCard> notShuffledDeck) {
        List<GameCard> listOfDeck = new ArrayList<>(notShuffledDeck);

        Collections.shuffle(listOfDeck);

        return new ArrayDeque<>(listOfDeck);
    }
}
