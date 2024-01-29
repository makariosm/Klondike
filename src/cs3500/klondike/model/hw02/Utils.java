package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for validating and processing decks of cards in Klondike solitaire.
 */
public class Utils {

  /**
   * Validates whether a deck of cards is valid for Klondike solitaire.
   *
   * @param deck the deck of cards to validate
   * @return true if the deck is valid, false otherwise
   * @throws IllegalArgumentException if the deck contains null cards or is invalid
   */
  public boolean validDeck(List<Card> deck) {
    // Check if there are null cards in the deck
    for (Card card : deck) {
      if (Objects.isNull(card)) {
        throw new IllegalArgumentException("Null Card in Deck.");
      }
    }

    // Check suit sizes
    List<List<Card>> decks = this.decksOfSuit(deck);
    for (int i = decks.size() - 1; i >= 0; i--) {
      if (decks.get(i).isEmpty()) {
        decks.remove(i);
      }
    }
    int decksize = decks.get(0).size();
    for (List<Card> currdeck : decks) {
      if (decksize != currdeck.size()) {
        throw new IllegalArgumentException("Deck is invalid.");
      }
    }


    // Check for concurrency in suits
    this.checkConcurrency(decks);
    decks.remove(0);

    for (List<Card> currDeck : decks) {
      if (!currDeck.isEmpty()) {
        throw new IllegalArgumentException("Deck is invalid.");
      }
    }
    return true;
  }

  /**
   * Separates a deck of cards into lists based on their suits.
   *
   * @param deck the deck of cards to separate
   * @return a list of lists, each containing cards of the same suit
   */
  private List<List<Card>> decksOfSuit(List<Card> deck) {
    List<Card> spades = new ArrayList<>();
    List<Card> clubs = new ArrayList<>();
    List<Card> diamonds = new ArrayList<>();
    List<Card> hearts = new ArrayList<>();
    for (Card card : deck) {
      if (card.isSuit(BasicCard.Suits.Hearts)) {
        hearts.add(card);
      } else if (card.isSuit(BasicCard.Suits.Clubs)) {
        clubs.add(card);
      } else if (card.isSuit(BasicCard.Suits.Diamonds)) {
        diamonds.add(card);
      } else if (card.isSuit(BasicCard.Suits.Spades)) {
        spades.add(card);
      }
    }
    return new ArrayList<>(Arrays.asList(spades, clubs, diamonds, hearts));
  }

  /**
   * Checks if cards in different suits are concurrent.
   *
   * @param decks a list of lists, each containing cards of the same suit
   * @return true if cards are concurrent, false otherwise
   */
  private void checkConcurrency(List<List<Card>> decks) {
    List<Card> first = decks.get(0);
    for (int i = decks.size() - 1; i > 0; i--) {
      for (Card card : first) {
        this.deckContainsAndRemoves(decks.get(i), card);
      }
    }

  }

  /**
   * Checks if a list contains cards with consecutive values.
   *
   * @param deck the list of cards to check
   */
  private void deckContainsAndRemoves(List<Card> deck, Card given) {
    for (Card card : deck) {
      if (card.isCardValue(given.getValue())) {
        deck.remove(card);
        break;
      }
    }

  }

  /**
   * The method to throw some exceptions necessary for starting a game.
   *
   * @param deck       the deck field (list of cards) in the klondike model
   * @param isGameOver check the status of the game
   * @param numPiles   the number of cascade piles
   * @param numDraw    the max number of draw cards visible at a time
   */
  public static void startGameExceptions(List<Card> deck, boolean isGameOver,
                                         int numPiles, int numDraw) {
    if (!isGameOver) {
      throw new IllegalStateException("Game already started.");
    }
    if (Objects.isNull(deck)) {
      throw new IllegalArgumentException("Deck cannot be null.");
    }

    List<Card> deckCopy = new ArrayList<>(deck);
    Utils u = new Utils();
    if (!u.validDeck(deckCopy)) {
      throw new IllegalArgumentException("Deck is invalid.");
    }

    //check if enough cards to fill the piles
    if ((((numPiles + 1) * (numPiles)) / 2) > deck.size() || numPiles < 1 || numDraw < 1) {
      throw new IllegalArgumentException(
              "Number of cascade piles or number of draw cards available is invalid.");
    }
  }

  /**
   * The method to deal the cards into the cascades properly.
   * Deals across each cascade first before stacking the cards in the pile
   * Starts at the next pile when it moves down a row
   *
   * @param cascades equivalent to {@code this.cascades}
   *                 the cascades field so cards can be added to it as needed
   * @param deck     equivalent to {@code this.deck}
   *                 the deck field so one can remove from it as needed
   */
  public static void cascadesMaker(List<CascadePile> cascades, List<Card> deck) {

    List<Card> tempDeck = new ArrayList<>(deck);
    int startRow = 0;
    int currentRow = 0;
    for (Card card : tempDeck) {
      if (currentRow + startRow < cascades.size()) {
        cascades.get(currentRow + startRow).invisible.add(card);
        deck.remove(0);
        currentRow++;
      } else {
        startRow++;
        currentRow = 0;
        if (currentRow + startRow < cascades.size()) {
          cascades.get(currentRow + startRow).invisible.add(card);
          deck.remove(0);
          currentRow++;
        }
      }
    }
    for (CascadePile cascade : cascades) {
      cascade.visible.add(cascade.invisible
              .remove(cascade.invisible.size() - 1));
    }
  }

  /**
   * The method to make draw piles.
   *
   * @param deckField equivalent to {@code this.deck}
   *                  so I can mutate the deck and reset it as I want
   * @param drawPile  equivalent to {@code this.drawPile}
   *                  so I can add to the drawPile as needed
   * @param numDraw   the max number of draw cards visible at one time
   */
  public static void drawPileMaker(List<Card> deckField,
                                   DrawPile drawPile, int numDraw) {
    //Add the remaining cards to the draw pile.
    drawPile.invisible.addAll(deckField);
    deckField.clear();

    int deckInvisSize = new Integer(drawPile.invisible.size());
    //Make the first numDraw cards visible in the draw pile.
    for (int i = 0; i < Math.min(numDraw, deckInvisSize); i++) {
      drawPile.visible.add(drawPile.invisible
              .remove(0));
    }
  }

  /**
   * The method to deal the cards into the cascades properly.
   * Deals across each cascade first before stacking the cards in the pile
   * Starts at the next pile when it moves down a row
   *
   * @param cascadePiles equivalent to {@code this.cascadePiles}
   *                     the cascades field so cards can be added to it as needed
   * @param deckField    equivalent to {@code this.deck}
   *                     the deck field so one can remove from it as needed
   * @param numPiles     the number of cascade piles the player wants to play with
   */
  public static void cascadesMakerWhitehead(List<List<Card>> cascadePiles, List<Card> deckField,
                                            int numPiles) {
    List<Card> tempDeck = new ArrayList<>(deckField);
    int startRow = 0;
    int currentRow = 0;

    for (int i = 0; i < numPiles; i++) {
      cascadePiles.add(new ArrayList<>());
    }

    for (Card card : tempDeck) {
      if (currentRow + startRow < cascadePiles.size()) {
        cascadePiles.get(currentRow + startRow).add(card);
        deckField.remove(0);
        currentRow++;
      } else {
        startRow++;
        currentRow = 0;
        if (currentRow + startRow < cascadePiles.size()) {
          cascadePiles.get(currentRow + startRow).add(card);
          deckField.remove(0);
          currentRow++;
        }
      }
    }
  }

  /**
   * Can you move a card from a cascade to a foundation pile in a game of whitehead Klondike?.
   *
   * @param cascades the list of cascade piles in the game
   * @param dest     the destination foundation pile the player is trying to move a card into
   * @return true if the player can move the card to a foundation pile, false otherwise
   */
  public static boolean canMovePilesToFoundationWhiteHead(List<List<Card>> cascades,
                                                          FoundationPile dest) {
    boolean canMoveAny = false;
    for (List<Card> cascade : cascades) {
      if (dest.visible.isEmpty() && !cascade.isEmpty()) {
        canMoveAny = cascade.get(cascade.size() - 1).isCardValue(1);
      } else if (!cascade.isEmpty()) {
        if (dest.visible.get(dest.visible.size() - 1).cardOntoCardFoundation(
                (BasicCard) cascade.get(cascade.size() - 1))) {
          canMoveAny = true;
        }
      }
    }
    return canMoveAny;
  }
}