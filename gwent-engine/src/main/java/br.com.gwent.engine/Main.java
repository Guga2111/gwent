package br.com.gwent.engine;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.Faction;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.enums.RowType;
import br.com.gwent.engine.pojo.structure.card.Card;
import br.com.gwent.engine.pojo.structure.card.GameCard;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final Long PLAYER_ONE_ID = 1L;
    private static final Long PLAYER_TWO_ID = 2L;

    public static void main (String[] args) {
        System.out.println("=========================================");
        System.out.println("  INITIALIZING GWENT ENGINE SIMULATION!  ");
        System.out.println("=========================================");

        Deque<GameCard> p1Deck = createTestDeck(Faction.NORTHERN_REALMS);
        Deque<GameCard> p2Deck = createTestDeck(Faction.NILFGAARD);

        Game game = new Game(p1Deck, p2Deck, PLAYER_ONE_ID, PLAYER_TWO_ID);

        runGameLoop(game);

        //simulate here the actions

        //add here the interface for the user test

        // example: player1 plays a card on artillery row
//        GameState currentState = game.getGameState();
//        long p1Id = currentState.getCurrentPlayerId();
//        GameCard cardToPlay = currentState.getPlayerById(p1Id).getHand().get(0);
//
//        System.out.println("\n--- Player " + p1Id + " plays the card: " + cardToPlay.getCardTemplate().getName() + " ---");
//        game.playCard( cardToPlay.getInstanceId(), RowType.ARTILLERY, p1Id);
//        printGameState(game.getGameState());
//
//        // example: player2 pass your turn
//        long p2Id = game.getGameState().getCurrentPlayerId();
//        System.out.println("\n--- Player " + p2Id + " pass the turn ---");
//        game.passTurn(p2Id);
//        printGameState(game.getGameState());
//
//        System.out.println("\n=========================================");
//        System.out.println("  SIMULATION FINISHED!  ");
//        System.out.println("=========================================");
    }

    private static void runGameLoop (Game game) {
        Scanner scanner = new Scanner(System.in);

        while (game.getGameState().getGameStatus() != GameStatus.GAME_FINISHED) {
            GameState currentState = game.getGameState();
            Long currentPlayerId = currentState.getCurrentPlayerId();

            printGameState(currentState);

            if (currentPlayerId.equals(PLAYER_ONE_ID)) {
                performHumanTurn(game, scanner);
            } else {
                performAiTurn(game);
            }
        }

        System.out.println("\n=========================================");
        System.out.println("          GAME OVER!          ");
        System.out.println("  Winner: player " + game.getGameState().getGameWinnerId());
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
        System.out.println("  > Rounds Won: " + state.getPlayer1().getRoundsWon());
        System.out.println("  > Hand: " + state.getPlayer1().getHand().size() + " cards");
        System.out.println("  > Board: " + state.getPlayer1().getBoard().getTotalPoints() + " points");
        System.out.println("Player 2 (ID " + state.getPlayer2().getUserId() + "):");
        System.out.println("  > Hand: " + state.getPlayer2().getHand().size() + " cards");
        System.out.println("  > Board: " + state.getPlayer2().getBoard().getTotalPoints() + " points");
        System.out.println("  > Rounds Won: " + state.getPlayer2().getRoundsWon());
        System.out.println("-----------------------------------------");
    }

    private static void performHumanTurn(Game game, Scanner scanner) {

        GameState currentState = game.getGameState();
        List<GameCard> hand = currentState.getPlayerById(PLAYER_ONE_ID).getHand();
        printHandCards(hand);
        System.out.println("Your time! Choose a card (0-" + (hand.size() - 1) + ") or press 'p' to pass:");

        String input = scanner.next();

        if (input.equalsIgnoreCase("p")) {
            try {
                game.passTurn(PLAYER_ONE_ID);
            } catch (RuntimeException e) {
                System.out.println("!!! ILLEGAL PLAY: " + e.getMessage());
            }
            return;
        }

        try {
            int cardIndex = Integer.parseInt(input);
            if (cardIndex < 0 || cardIndex >= hand.size()) {
                System.out.println("!!! Invalid choose. Try again.");
                return;
            }

            GameCard cardToPlay = hand.get(cardIndex);

            System.out.println("Choose the row: [1] Infantry (Melee), [2] Artillery (Ranged), [3] Siege");
            int rowInput = scanner.nextInt();
            RowType targetRow;
            switch (rowInput) {
                case 1: targetRow = RowType.INFANTRY; break;
                case 2: targetRow = RowType.ARTILLERY; break;
                case 3: targetRow = RowType.SIEGE; break;
                default:
                    System.out.println("!!! Invalid row. Try again.");
                    return;
            }

            game.playCard(cardToPlay.getInstanceId(), targetRow, PLAYER_ONE_ID);

        } catch (NumberFormatException e) {
            System.out.println("!!! Invalid entry. Type a number or 'p'.");
        } catch (RuntimeException e) {
            System.out.println("!!! Illegal play: " + e.getMessage());
        }
    }

    private static void performAiTurn(Game game) {
        System.out.println("Opponent turn (AI)...");
        try {
            Thread.sleep(2000);

            GameState currentState = game.getGameState();
            List<GameCard> aiHand = currentState.getPlayerById(PLAYER_TWO_ID).getHand();

            if (aiHand.isEmpty()) {
                System.out.println("AI does not have cards and will pass.");
                game.passTurn(PLAYER_TWO_ID);
                return;
            }

            if (game.getGameState().getPlayerById(PLAYER_ONE_ID).getHand().isEmpty()) {
                game.passTurn(PLAYER_TWO_ID);
                return;
            }

            // Simple AI: Find the most powerful card and play it
            Optional<GameCard> bestCardOptional = aiHand.stream()
                    .max(Comparator.comparingInt(GameCard::getCurrentPower));

            GameCard bestCard = null;

            if (bestCardOptional.isPresent()) {
                bestCard = bestCardOptional.get();
            }

            // IA Simples: Escolhe uma fileira aleatória.
            if (bestCard != null) {
                RowType randomRow = RowType.values()[ThreadLocalRandom.current().nextInt(RowType.values().length)];

                System.out.println("IA joga a carta: " + bestCard.getCardTemplate().getName() + " na fileira " + randomRow.name());
                game.playCard(bestCard.getInstanceId(), randomRow, PLAYER_TWO_ID);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            System.out.println("!!! ERRO NA IA: " + e.getMessage());
        }
    }



    private static void printHandCards (List<GameCard> hand) {

        System.out.println("=========================================");
        System.out.println("  SELECT ONE CARD TO PLAY: ");

        for(int i = 0 ; i < hand.size(); i++) {
            GameCard card = hand.get(i);
            System.out.println(i + " - " + card.getCardTemplate().getName() + "(" + card.getCurrentPower() + "pts).");
        }

        System.out.println("=========================================");

    }

}
