package br.com.gwent.engine;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.Faction;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main (String[] args) {
        System.out.println("=========================================");
        System.out.println("  INITIALIZING GWENT ENGINE SIMULATION!  ");
        System.out.println("=========================================");

        Deque<GameCard> p1Deck = createTestDeck(Faction.NORTHERN_REALMS);
        Deque<GameCard> p2Deck = createTestDeck(Faction.NILFGAARD);

        long playerOneId = 1L;
        long playerTwoId = 2L;

        Game game = new Game(p1Deck, p2Deck, playerOneId, playerTwoId);

        System.out.println("\n--- Jogo Iniciado! ---");
        printGameState(game.getGameState());

        //simulate here the actions

        // example: player1 plays a card on artillery row
        GameState currentState = game.getGameState();
        long p1Id = currentState.getCurrentPlayerId();
        GameCard cardToPlay = currentState.getPlayerById(p1Id).getHand().get(0);

        System.out.println("\n--- Player " + p1Id + " plays the card: " + cardToPlay.getCardTemplate().getName() + " ---");
        game.playCard( cardToPlay.getInstanceId(), RowType.ARTILLERY, p1Id);
        printGameState(game.getGameState());

        // example: player2 pass your turn
        long p2Id = game.getGameState().getCurrentPlayerId();
        System.out.println("\n--- Player " + p2Id + " pass the turn ---");
        game.passTurn(p2Id);
        printGameState(game.getGameState());

        System.out.println("\n=========================================");
        System.out.println("  SIMULATION FINISHED!  ");
        System.out.println("=========================================");
    }

    private static Deque<GameCard> createTestDeck (Faction faction) {
        return IntStream.range(0, 25)
                .mapToObj(i -> {
                    Card cardTemplate = Card.builder()
                            .id(faction.name().toLowerCase() + "_card" + i)
                            .name(faction.name() + " Unity " + (i + 1))
                            .basePower( (i % 5) + 1)
                            .faction(faction)
                            .build();
                    return new GameCard(cardTemplate);
                })
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    private static void printGameState(GameState state) {
        System.out.println("-----------------------------------------");
        System.out.println("Round: " + state.getCurrentRound() + " | Turn of Player: " + state.getCurrentPlayerId());
        System.out.println("Player 1 (ID " + state.getPlayer1().getUserId() + "):");
        System.out.println("  > Hand: " + state.getPlayer1().getHand().size() + " cards");
        System.out.println("  > Board: " + state.getPlayer1().getBoard().getTotalPoints() + " points");
        System.out.println("Player 2 (ID " + state.getPlayer2().getUserId() + "):");
        System.out.println("  > Hand: " + state.getPlayer2().getHand().size() + " cards");
        System.out.println("  > Board: " + state.getPlayer2().getBoard().getTotalPoints() + " points");
        System.out.println("-----------------------------------------");
    }
}
