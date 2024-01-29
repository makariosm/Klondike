package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is an abstract class used to represent all 3 times of piles.
 */
public abstract class AbstractPile implements Pile {

  protected List<Card> invisible;
  protected List<Card> visible;

  /**
   * This is the abstract pile constructor.
   *
   * @param invisible this is the set of all cards meant to stay invisible in this pile.
   *                  (the indices are zero-based so the first in the list is zero)
   * @param visible   this is the set of all cards meant to stay visible in this pile.
   *                  (the indices are zero-based so the first in the list is zero)
   */
  public AbstractPile(List<Card> invisible, List<Card> visible) {
    this.invisible = invisible;
    this.visible = visible;
  }

  /**
   * This is another abstract pile constructor. This is primarily used for the foundation pile.
   *
   * @param cards this is the pile of cards meant for the foundation pile.
   *              (the index for this list is also zero-based)
   */
  public AbstractPile(List<Card> cards) {
    this.visible = cards;
  }

  /**
   * This is the main helper method for all the move functions.
   * that move cards from cascade to cascade.
   *
   * @param numCards the number of cards you want to move from pile to pile
   * @param dest     this is the destination cascade pile for all the cards to end up in
   * @param deckSize this is the full size of the deck
   * @param numAces  this is the number of aces in the deck
   * @throws IllegalArgumentException if there are not enough cards to move
   * @throws IllegalStateException    if the move is not valid
   */
  public void movePile(int numCards, CascadePile dest, int deckSize, int numAces) {
    if (numCards > this.visible.size() && this.invisible.isEmpty()) {
      throw new IllegalArgumentException("Not enough cards to move.");
    }
    if (numCards > this.visible.size()) {
      throw new IllegalStateException("Can't move more cards than are visible.");
    } else if (dest.visible.isEmpty() && dest.invisible.isEmpty()) {
      BasicCard srcCard = (BasicCard) this.visible.get(this.visible.size() - numCards);
      this.moveFrom(dest.visible, numCards, srcCard.isCardValue(deckSize / numAces), this.invisible,
              this.visible);
    } else if (this.visible.isEmpty()) {
      throw new IllegalArgumentException("Not enough cards from source pile to move.");
    } else if (!dest.visible.isEmpty()) {
      BasicCard srcCard = (BasicCard) this.visible.get(this.visible.size() - numCards);
      Card destCard = dest.visible.get(dest.visible.size() - 1);
      this.moveFrom(dest.visible, numCards, destCard.cardOntoCardPile(srcCard), this.invisible,
              this.visible);
    } else {
      throw new IllegalStateException("Move Invalid.");
    }
  }

  /**
   * This is the helper to move cards from one pile.
   * It moves cards from the end of one pile and adds them to another.
   *
   * @param dest     this is the list of cards you want to be adding into
   * @param numCards this is the number of cards you want to add
   * @param boolOp   this is the check to see if you can move the cards you want
   * @throws IllegalStateException if the move is not allowable
   */
  public void moveFrom(List<Card> dest, int numCards, boolean boolOp, List<Card> srcInvisible,
                       List<Card> srcVisible) {

    List<Card> empty = new ArrayList<>();
    if (boolOp) {
      for (int i = 0; i < numCards; i++) {
        empty.add(srcVisible.remove(srcVisible.size() - 1));
      }
      Collections.reverse(empty);
      dest.addAll(empty);
      if (!srcInvisible.isEmpty() && srcVisible.isEmpty()) {
        srcVisible.add(srcInvisible.remove(srcInvisible.size() - 1));
      }
    } else {
      throw new IllegalStateException("The move is not allowable");
    }
  }

  /**
   * This is the main helper used to move cards from a cascade to a foundation pile.
   *
   * @param destFound this is the foundation pile you want to add into
   * @throws IllegalStateException if the source pile is empty or the move is not valid
   */
  public void moveToFoundation(FoundationPile destFound, List<Card> srcInvisible,
                               List<Card> srcVisible) {
    if (srcVisible.isEmpty()) {
      throw new IllegalStateException("Source pile is empty.");
    } else if (destFound.visible.isEmpty()) {
      Card srcCard = srcVisible.get(srcVisible.size() - 1);
      this.moveFrom(destFound.visible, 1, srcCard.isCardValue(1), srcInvisible,
              srcVisible);
    } else {
      BasicCard srcCard = (BasicCard) srcVisible.get(srcVisible.size() - 1);
      Card destCard = destFound.visible.get(destFound.visible.size() - 1);
      this.moveFrom(destFound.visible, 1, destCard.cardOntoCardFoundation(srcCard),
              srcInvisible, srcVisible);
    }
  }


  /**
   * Checks if cards can be moved from cascades to a destination cascade pile.
   *
   * @param cascades a list of cascade piles
   * @param dest     the destination cascade pile
   * @return true if cards can be moved, false otherwise
   */
  public boolean canMovePilesToPile(List<CascadePile> cascades, CascadePile dest) {
    boolean canMoveAny = false;
    for (CascadePile cascade : cascades) {
      for (Card card : cascade.visible) {
        if (!dest.visible.isEmpty()) {
          if (dest.visible.get(dest.visible.size() - 1).cardOntoCardPile((BasicCard) card)) {
            canMoveAny = true;
          }
        }
      }
    }
    return canMoveAny;
  }

  /**
   * Checks if cards can be moved from cascades to a destination foundation pile.
   *
   * @param cascades a list of cascade piles
   * @param dest     the destination foundation pile
   * @return true if cards can be moved, false otherwise
   */
  public boolean canMovePilesToFoundation(List<CascadePile> cascades, FoundationPile dest) {
    boolean canMoveAny = false;
    for (CascadePile cascade : cascades) {
      if (dest.visible.isEmpty() && !cascade.visible.isEmpty()) {
        canMoveAny = cascade.visible.get(cascade.visible.size() - 1).isCardValue(1);
      } else if (!cascade.visible.isEmpty()) {
        if (dest.visible.get(dest.visible.size() - 1).cardOntoCardFoundation(
                (BasicCard) cascade.visible.get(cascade.visible.size() - 1))) {
          canMoveAny = true;
        }
      }
    }
    return canMoveAny;
  }


}
