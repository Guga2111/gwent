package br.com.gwent.engine.pojo.structure;

import br.com.gwent.engine.pojo.structure.board.PlayerBoard;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import lombok.Data;

import java.util.*;

@Data
public class Player {
    private final Long userId;

    private int roundsWon = 0;
    private boolean hasPassed = false;

    private PlayerBoard board;

    private List<GameCard> hand;
    private Deque<GameCard> deck;
    private List<GameCard> discard;

    public Player (Long userId, Deque<GameCard> deck, List<GameCard> hand) {
        this.userId = userId;
        this.deck = deck;
        this.hand = hand;

        this.discard = new ArrayList<>();
        this.board = new PlayerBoard();
    }

    public GameCard removeCardFromHand (UUID gameCardId) {
        Optional<GameCard> requestCard = this.hand.stream()
                .filter(card -> card.getInstanceId().equals(gameCardId))
                .findFirst();

        requestCard.ifPresent(card -> this.hand.remove(card));
        return requestCard.get();
    }
}
