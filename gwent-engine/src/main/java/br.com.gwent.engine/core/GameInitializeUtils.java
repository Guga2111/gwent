package br.com.gwent.engine.core;

import br.com.gwent.engine.core.GameState;
import br.com.gwent.engine.pojo.enums.GameStatus;
import br.com.gwent.engine.pojo.structure.Player;
import br.com.gwent.engine.pojo.structure.card.GameCard;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameInitializeUtils {

    public static GameState initializeNewGame (
            Deque<GameCard> playerOneDeck, Deque<GameCard> playerTwoDeck, Long playerOneId, Long playerTwoId
    ) {

        playerOneDeck = shuffleDeck(playerOneDeck);
        playerTwoDeck = shuffleDeck(playerTwoDeck);

        List<GameCard> player1Hand = createHand(playerOneDeck);
        List<GameCard> player2Hand = createHand(playerTwoDeck);

        Player player1 = new Player(playerOneId, playerOneDeck, player1Hand);
        Player player2 = new Player(playerTwoId, playerTwoDeck, player2Hand);

        Long startingPlayerId = decideStartingPlayer(playerOneId, playerTwoId);

        return GameState.builder()
                .gameStatus(GameStatus.ROUND_IN_PROGRESS)
                .gameWinnerId(null)
                .currentRound(1)
                .currentPlayerId(startingPlayerId)
                .player1(player1)
                .player2(player2)
                .build();
    }

    private static Deque<GameCard> shuffleDeck (Deque<GameCard> notShuffledDeck) {
        List<GameCard> listOfDeck = new ArrayList<>(notShuffledDeck);

        Collections.shuffle(listOfDeck);

        return new ArrayDeque<>(listOfDeck);
    }

    private static List<GameCard> createHand (Deque<GameCard> shuffledDeck) {

        List<GameCard> newHand = new ArrayList<>();

        while (newHand.size() < 10) {
            newHand.add(shuffledDeck.pop());
        }

        return newHand;
    }

    private static Long decideStartingPlayer (Long playerOneId, Long playerTwoId) {

        boolean playerOneStarts = ThreadLocalRandom.current().nextBoolean();

        return playerOneStarts ? playerOneId : playerTwoId;
    }
}
