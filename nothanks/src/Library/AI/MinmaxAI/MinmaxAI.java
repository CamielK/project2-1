package Library.AI.MinmaxAI;

import Library.AI.AIInterface;
import Library.AI.Gametree.NothanksTree;
import Library.Board;
import Library.Card;
import Library.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinmaxAI implements AIInterface {

    private NothanksTree gametree = null;

    @Override
    public boolean GetMove() {
        List<Card> deck = new ArrayList<>();
        List<Card> boardDeck = Board.getInstance().getCurrentDeck();
        int limit = boardDeck.size();
        if (limit > 18) limit = 18;
        for (int i = 0; i < limit; i++) deck.add(boardDeck.get(i));

//        List<Card> deck = new ArrayList<>();
//        deck.add(new Card(20));
//        deck.add(new Card(10));
//        deck.add(new Card(7));
//        deck.add(new Card(16));

//        List<Card> p1cards = Board.getInstance().getPlayers().get(0).getCards();
//        p1cards.add(new Card(11));
//        this.gametree = buildMinimaxTree(deck, 1, p1cards, Board.getInstance().getPlayers().get(1).getCards(), 0, 0, false);

        this.gametree = buildMinimaxTree(deck, 1, Board.getInstance().getPlayers().get(0).getCards(), Board.getInstance().getPlayers().get(1).getCards(), 0, 0, false);

        int move = minimax(this.gametree, 1)[1];
//        System.out.println("AI Move: " + move);
        return move == 1;
    }

	@Override
	public void gameIsFinished(ArrayList<Player> winner) {
		// TODO Auto-generated method stub
        this.gametree = null;
    }

    /**
     * Minimax search to determine next move
     * @param tree game tree
     * @param player player=1 and player=2 correspond to player 1 and player 2 respectively. Used to alternate turns
     * @return int[2] {value, move} move=1 corresponds to picking the card, move=0 corresponds to tossing the card
     */
	private int[] minimax(NothanksTree tree, int player) {
	    int bestScore = 0;
	    int move = 1;

	    if (tree.getPicked() == null && tree.getTossed() == null) {
            bestScore = tree.getValue();
        }
        else if (player == 1) { //maximize scoreDiff for player 1
            bestScore = -100000;

            if (tree.getTossed() != null) {
                int score = minimax(tree.getTossed(), 2)[0];
                if (score > bestScore) {
                    bestScore = score;
                    move = 0; // Tossed card
                }
            }

            if (tree.getPicked() != null) {
                int score = minimax(tree.getPicked(), 2)[0];
                if (score > bestScore) {
                    bestScore = score;
                    move = 1; // Picked card
                }
            }
        }
        else if (player == 2) { //minimize scoreDiff for player 2
            bestScore = 100000;

            if (tree.getTossed() != null) {
                int score = minimax(tree.getTossed(), 1)[0];
                if (score < bestScore) {
                    bestScore = score;
                    move = 0; // Tossed card
                }
            }

            if (tree.getPicked() != null) {
                int score = minimax(tree.getPicked(), 1)[0];
                if (score < bestScore) {
                    bestScore = score;
                    move = 1; // Picked card
                }
            }
        }

        return new int[] {bestScore, move};
    }

    /**
     * Heuristic evaluation function to determine the value of a player's deck.
     * Lower score = better
     * @param cards List of cards owned by player
     * @return value awarded to this specific deck
     */
    private int evaluate(List<Card> cards) {
	    if (cards.size() == 0) {
	        // Decks without cards are useless
	        return 0;
        }

        // Get the general score from this deck as a basis for the heuristic function
        // This is the same score as is used in the game scoreboard
        Collections.sort(cards);
        Card scoreCard = cards.get(0);
        int score = scoreCard.getNumber();
        int series = 0;
        for(Card card : cards) {
            if(card == scoreCard) continue;

            if(card.getNumber() - scoreCard.getNumber() != 1) score += card.getNumber();
            else series += 1;

            scoreCard = card;
        }

        // Boost score based on the size of the deck
        // (bigger decks are boosted to prevent minimax from converging to nevertake)
        score = score - (10 * cards.size());

        // Boost score based on the number of series in the deck
        // Having a series of cards massively increases win chance: we favor decks that have more series
        score = score - (50 * series);

        return score;
    }

    /**
     * Build the game tree from the given card deck.
     * Tree nodes have a value of 0, tree leafes contain the possible score difference determined by player 2 score - player 1 score
     * @param deck Deck of cards to build tree from
     * @param player player=1 and player=2 correspond to player 1 and player 2 respectively. Used to alternate turns
     * @param p1_cards List of cards owned by player 1 at the current node
     * @param p2_cards List of cards owned by player 2 at the current node
     * @param mustPick Set to true if previous move was a toss. In order to minimize tree size we limit the max number of tosses to 1 for each card.
     * @return Returns the nothanks game tree based on the given deck.
     */
	private NothanksTree buildMinimaxTree(List<Card> deck, int player, List<Card> p1_cards, List<Card> p2_cards, int p1_tosses, int p2_tosses, boolean mustPick) {
        NothanksTree tree = new NothanksTree();
        int tossLimit = 8;

        // Reached bottom of path. Set leaf value using evaluation function
        if (deck.size() <= 0) {
            tree.setPicked(null);
            tree.setTossed(null);
            tree.setValue(evaluate(p2_cards) - evaluate(p1_cards)); // Leaf value = score diff [p2-p1]
            return tree;
        }

        // Get the next top card from the deck
        Card currentCard = deck.get(0);
        tree.setValue(0);

        // Expand picked tree: add current card to current player and continue with decreased deck + other player
        List<Card> deck_copy = new ArrayList<>();
        for (int i = 1; i < deck.size(); i++) deck_copy.add(deck.get(i));

        List<Card> p2_cards_copy = new ArrayList<>();
        p2_cards_copy.addAll(p2_cards);

        List<Card> p1_cards_copy = new ArrayList<>();
        p1_cards_copy.addAll(p1_cards);

        if (player == 1) { // Add current card to player 1's deck
            p1_cards_copy.add(currentCard);
            tree.setPicked(buildMinimaxTree(deck_copy, 2, p1_cards_copy, p2_cards_copy, p1_tosses, p2_tosses, false));
        } else { // Add current card to player 2's deck
            p2_cards_copy.add(currentCard);
            tree.setPicked(buildMinimaxTree(deck_copy, 1, p1_cards_copy, p2_cards_copy, p1_tosses, p2_tosses, false));
        }

        // Expand tossed tree: if previous move was not already a skip, skip the card and continue with the same deck but other player
        if (!mustPick) {
            List<Card> deck_copy_tossed = new ArrayList<>();
            deck_copy_tossed.addAll(deck);
            if (player == 1 && p1_tosses < tossLimit) {
                int p1_tossed = p1_tosses + 1;
                tree.setTossed(buildMinimaxTree(deck_copy_tossed, 2, p1_cards, p2_cards, p1_tossed, p2_tosses, true));
            }
            else if (player == 2 && p2_tosses < tossLimit) {
                int p2_tossed = p2_tosses + 1;
                tree.setTossed(buildMinimaxTree(deck_copy_tossed, 1, p1_cards, p2_cards, p1_tosses, p2_tossed, true));
            }
            else {
                tree.setTossed(null);
            }
        } else {
            // We limit the number of tosses to 1 per card: stop expanding the toss tree after 1 toss
            tree.setTossed(null);
        }

        return tree;
    }

}
