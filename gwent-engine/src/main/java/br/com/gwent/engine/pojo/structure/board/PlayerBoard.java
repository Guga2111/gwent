package br.com.gwent.engine.pojo.structure.board;

import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.Player;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class PlayerBoard {
    private Map<RowType, BoardRow> rows = new EnumMap<>(RowType.class);

    public PlayerBoard () {
        for(RowType type : RowType.values()) {
            rows.put(type, new BoardRow(type));
        }
    }

    public int getTotalPoints () {
        return rows.values()
                .stream()
                .mapToInt(BoardRow::getTotalPower)
                .sum();
    }

    public void clearBoard (Player player) {
        for(BoardRow row : rows.values()) {
            player.getDiscard().addAll(row.getCards());
            row.getCards().clear();
        }
    }

    public BoardRow getRow (RowType type) {
        return rows.get(type);
    }
}
