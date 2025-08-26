package br.com.gwent.engine.pojo.structure.board;

import br.com.gwent.engine.pojo.enums.RowType;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class PlayerBoard {
    private Map<RowType, BoardRow> rows = new EnumMap<>(RowType.class);

    public int getTotalPoints () {
        return rows.values()
                .stream()
                .mapToInt(BoardRow::getPoints)
                .sum();
    }
}
