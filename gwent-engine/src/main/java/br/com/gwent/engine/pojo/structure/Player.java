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
    private List<GameCard> discard;

    public Player (Long userId, Deque<GameCard> deck, List<GameCard> hand) {
        this.userId = userId;
        this.deck = deck;
        this.hand = hand;
        this.discard = new ArrayList<>();
    }

}
