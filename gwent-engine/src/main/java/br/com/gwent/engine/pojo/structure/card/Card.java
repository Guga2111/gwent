package br.com.gwent.engine.pojo.structure.card;

import br.com.gwent.engine.pojo.enums.CardType;
import br.com.gwent.engine.pojo.enums.Faction;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data @Builder
public class Card {
    private Long id;
    private String name;
    private int basePower;
    private CardType type;
    private Faction faction;
}
