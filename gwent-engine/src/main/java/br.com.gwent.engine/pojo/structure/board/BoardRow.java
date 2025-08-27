package br.com.gwent.engine.pojo.structure.board;

import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.card.GameCard;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardRow {
    private final RowType rowType;
    private List<GameCard> cards = new ArrayList<>();

    public int getTotalPower() {
        return cards.stream().mapToInt(GameCard::getCurrentPower).sum();
    }

    public void addCardToRow (GameCard card) {
        cards.add(card);
    }
}
