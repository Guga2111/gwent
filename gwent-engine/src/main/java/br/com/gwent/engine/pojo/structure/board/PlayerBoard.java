package br.com.gwent.engine.pojo.structure.board;

import br.com.gwent.engine.pojo.enums.RowType;

import java.util.EnumMap;
import java.util.Map;

public class PlayerBoard {
    private Map<RowType, BoardRow> rows = new EnumMap<>(RowType.class);
    private int totalPoints;
}
