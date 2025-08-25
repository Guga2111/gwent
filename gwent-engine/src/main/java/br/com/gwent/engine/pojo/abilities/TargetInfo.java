package br.com.gwent.engine.pojo.abilities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data @Builder
public class TargetInfo {
    private final UUID targetCardId;
    private final Long targetPlayerId;
}
