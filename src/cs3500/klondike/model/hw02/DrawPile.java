package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a draw pile in the Klondike solitaire game.
 * The draw pile contains a stack of cards that can be drawn by the player.
 */
public class DrawPile extends AbstractPile {

  /**
   * Constructs a draw pile with the given invisible and visible cards.
   *
   * @param invisible the list of cards meant to stay invisible in this pile
   * @param visible   the list of cards meant to stay visible in this pile
   */
  public DrawPile(List<Card> invisible, List<Card> visible) {
    super(invisible, visible);
  }

  /**
   * Moves a specified number of cards from the draw pile to a destination cascade pile.
   *
   * @param numCards the number of cards to move from the draw pile
   * @param dest     the destination cascade pile for the cards to be moved
   * @param deckSize the full size of the deck
   * @param numAces  the number of aces in the deck
   * @throws IllegalArgumentException if there are not enough cards to move
   * @throws IllegalStateException    if the move is invalid
   */
  @Override
  public void movePile(int numCards, CascadePile dest, int deckSize, int numAces) {
    if (numCards > this.visible.size() && this.invisible.isEmpty()) {
      throw new IllegalArgumentException("Not enough cards to move.");
    }
    if (numCards > this.visible.size()) {
      throw new IllegalStateException("Can't move more cards than are visible.");
    } else if (dest.visible.isEmpty() && dest.invisible.isEmpty()) {
      BasicCard srcCard = (BasicCard) this.visible.get(0);
      this.moveFrom(dest.visible, numCards, srcCard.isCardValue(deckSize / numAces), this.invisible,
              this.visible);
    } else if (this.visible.isEmpty()) {
      throw new IllegalArgumentException("Not enough cards from source pile to move.");
    } else if (!dest.visible.isEmpty()) {
      BasicCard srcCard = (BasicCard) this.visible.get(0);
      Card destCard = dest.visible.get(dest.visible.size() - 1);
      this.moveFrom(dest.visible, numCards, destCard.cardOntoCardPile(srcCard), this.invisible,
              this.visible);
    } else {
      throw new IllegalStateException("Move Invalid.");
    }
  }

  /**
   * Moves a card from the draw pile to a foundation pile.
   *
   * @param destFound the foundation pile to which the card should be moved
   * @throws IllegalStateException if the source pile is empty
   */
  public void moveToFoundation(FoundationPile destFound, List<Card> cards) {
    if (this.visible.isEmpty()) {
      throw new IllegalStateException("Source pile is empty.");
    } else if (destFound.visible.isEmpty()) {
      BasicCard srcCard = (BasicCard) this.visible.get(0);
      this.moveFrom(destFound.visible, 1, srcCard.isCardValue(1), this.invisible,
              this.visible);
    } else {
      Card srcCard = this.visible.get(0);
      Card destCard = destFound.visible.get(destFound.visible.size() - 1);
      this.moveFrom(destFound.visible,
              1, destCard.cardOntoCardFoundation(srcCard), this.invisible, this.visible);
    }
  }

  /**
   * Returns a string representation of the draw pile, including its visible cards.
   *
   * @return a string containing the visible cards in the draw pile
   */
  @Override
  public String toString() {
    StringBuilder drawCard = new StringBuilder();
    for (int i = 0; i < this.visible.size(); i++) {
      if (i == this.visible.size() - 1) {
        drawCard.append(this.visible.get(i).toString());
      } else {
        drawCard.append(this.visible.get(i).toString()).append(", ");
      }
    }
    return "Draw: " + drawCard;
  }

  /**
   * This is the helper for moving cards from a pile.
   * It moves cards from the beginning of one pile and adds them to another. Used for draw piles.
   *
   * @param dest     this is the list of cards you want to be adding into
   * @param numCards this is the number of cards you want to add
   * @param boolOp   this is the check to see if you can move the cards you want
   * @throws IllegalStateException if the move is not allowable
   */
  public void moveFrom(List<Card> dest, int numCards, boolean boolOp, List<Card> srcInvisible,
                       List<Card> srcVisible) {

    List<Card> empty = new ArrayList<>();
    if (!boolOp) {
      throw new IllegalStateException("The move is not allowable");
    } else {
      for (int i = 0; i < numCards; i++) {
        empty.add(srcVisible.remove(0));
      }
      dest.addAll(empty);
      if (!srcInvisible.isEmpty() && srcVisible.isEmpty()) {
        srcVisible.add(srcInvisible.remove(0));
      }
    }
  }

  private void checkArgumentsMoveDraw(int destPile, int numCascades) {
    if (destPile < 0 || destPile >= numCascades) {
      throw new IllegalArgumentException("Destination Pile Index is Wrong");
    } else if (this.visible.isEmpty()) {
      throw new IllegalStateException("No draw cards to move.");
    }
  }

  /**
   * Moves the cards from one pile to another in a game of whitehead Klondike.
   * @param destPile the index of the destination pile you want the cards to land in
   * @param numCascades the number of cascade piles in the current game
   * @param cascades the list of cascade piles in the game
   */
  public void whiteheadMoveDraw(int destPile, int numCascades, List<List<Card>> cascades) {
    this.checkArgumentsMoveDraw(destPile, numCascades);

    if (cascades.get(destPile).isEmpty()) {
      this.moveFrom(cascades.get(destPile), 1, true, this.invisible, this.visible);
    } else if (!(cascades.get(destPile).isEmpty())) {
      Card destCard = cascades.get(destPile).get(
              cascades.get(destPile).size() - 1);
      Card srcCard = this.visible.get(0);
      this.moveFrom(cascades.get(destPile), 1,
              destCard.cardOntoCardPileSameColor(srcCard), this.invisible, this.visible);
      if (!this.invisible.isEmpty()) {
        this.visible.add(
                this.visible.size() - 1, this.invisible.remove(0));
      }
    } else {
      throw new IllegalStateException("Move draw invalid.");
    }
  }

  /**
   * Discards a draw card from the draw pile in a game of limited klondike.
   * @param numTimesRedrawAllowed the num times the player is allowed to redraw.
   * @param drawCount the number of times the player has called discard draw
   */
  public void discardDrawLimited(int numTimesRedrawAllowed, int drawCount) {
    int drawSize = this.invisible.size() + this.visible.size();

    if (drawCount >= ((numTimesRedrawAllowed * drawSize) + 1)
            || numTimesRedrawAllowed == 0) {
      this.discardAndRemove();
    } else {
      this.discardAndRecycle();
    }

  }

  /**
   * The general method for discard a draw card in a general game of klondike.
   * It discards the front-most card in the pile and puts it in the back
   */
  public void discardAndRecycle() {
    if (this.visible.isEmpty()) {
      throw new IllegalStateException("Draw Pile is empty.");
    } else {
      this.invisible.add(this.visible.get(0));
      this.visible.add(this.invisible.get(0));
      this.visible.remove(0);
      this.invisible.remove(0);
    }
  }

  private void discardAndRemove() {
    if (this.visible.isEmpty() && this.invisible.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty.");
    }
    else {
      this.visible.remove(0);
      if (!this.invisible.isEmpty()) {
        this.visible.add(this.invisible.remove(0));
      }    }
  }

  /**
   * Does this draw pile have anymore visible cards?.
   * @return true if the draw pile still has visible cards, false otherwise
   */
  public boolean isDrawVisEmpty() {
    return this.visible.isEmpty();
  }

}
