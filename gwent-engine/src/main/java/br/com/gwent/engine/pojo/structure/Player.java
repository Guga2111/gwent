package br.com.gwent.engine.pojo.structure;

import br.com.gwent.engine.pojo.structure.card.GameCard;
import lombok.Data;

import java.util.Deque;
import java.util.List;

@Data
public class Player {
    private final Long userId;
    private boolean hasPassed = false;
    private List<GameCard> hand;
    private Deque<GameCard> deck;
}
