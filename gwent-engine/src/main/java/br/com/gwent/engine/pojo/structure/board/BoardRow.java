package br.com.gwent.engine.pojo.structure.board;

import br.com.gwent.engine.pojo.enums.RowType;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class BoardRow {
    private final RowType rowType;
    private int points;
}
