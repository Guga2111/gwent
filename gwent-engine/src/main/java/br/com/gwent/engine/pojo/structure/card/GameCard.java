package br.com.gwent.engine.pojo.structure.card;

import lombok.Data;

import java.util.UUID;

@Data
public class GameCard {
    private final UUID instanceId = UUID.randomUUID();
    private final Card cardTemplate;
    private int currentPower;

    public GameCard(Card cardTemplate) {
        this.cardTemplate = cardTemplate;
        this.currentPower = cardTemplate.getBasePower();
    }
}
