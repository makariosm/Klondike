package cs3500.klondike.model.hw02;

import java.util.List;

/**
 * Represents a pile of cards in the Klondike solitaire game.
 * Implementations of this interface define the behavior of different types of piles,
 * such as cascade piles and foundation piles.
 */
public interface Pile {

  /**
   * Moves a specified number of cards from this pile to a destination cascade pile.
   *
   * @param numCards the number of cards to move from this pile
   * @param dest     the destination cascade pile for the cards to end up in
   * @param deckSize the full size of the deck
   * @param numAces  the number of aces in the deck
   */
  void movePile(int numCards, CascadePile dest, int deckSize, int numAces);

  /**
   * Moves cards from this pile to a destination foundation pile.
   *
   * @param destFound the foundation pile you want to add cards to
   */
  void moveToFoundation(FoundationPile destFound, List<Card> invisible, List<Card> visible);

  /**
   * Checks if it's possible to move cards from cascade piles to a destination cascade pile.
   *
   * @param cascades the list of cascade piles to consider for the move
   * @param dest     the destination cascade pile
   * @return true if cards can be moved to the destination cascade pile, false otherwise
   */
  boolean canMovePilesToPile(List<CascadePile> cascades, CascadePile dest);

  /**
   * Checks if it's possible to move cards from cascade piles to a destination foundation pile.
   *
   * @param cascades the list of cascade piles to consider for the move
   * @param dest     the destination foundation pile
   * @return true if cards can be moved to the destination foundation pile, false otherwise
   */
  boolean canMovePilesToFoundation(List<CascadePile> cascades, FoundationPile dest);

  /**
   * Returns a string representation of the pile.
   *
   * @return a string representation of the pile
   */
  String toString();

  /**
   * This is the helper for moving cards from a pile.
   * It moves cards from the beginning of one pile and adds them to another. Used for draw piles.
   *
   * @param dest     this is the list of cards you want to be adding into
   * @param numCards this is the number of cards you want to add
   * @param boolOp   this is the check to see if you can move the cards you want
   * @throws IllegalStateException if the move is not allowable
   */
  public void moveFrom(List<Card> dest, int numCards, boolean boolOp, List<Card> srcInvis,
                       List<Card> srcVis);

}
