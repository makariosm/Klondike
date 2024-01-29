package cs3500.klondike.model.hw02;

import java.util.List;

/**
 * The class to represent a foundation pile.
 */
public class FoundationPile extends AbstractPile {

  /**
   * The constructor for a foundation pile.
   * @param cards the list of cards in a foundation pile
   */
  public FoundationPile(List<Card> cards) {

    super(cards);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (this.visible.isEmpty()) {
      return "<none>";
    }
    else {
      return this.visible.get(this.visible.size() - 1).toString();
    }

  }


  /**
   * Gives the list of foundations into its representative string equivalent.
   *
   * @param foundations the list of foundations
   * @return the string representation of the foundations
   */
  public String toStringFoundations(List<FoundationPile> foundations) {
    String foundationPile = "Foundation: ";
    for (FoundationPile foundation : foundations) {
      if (foundations.indexOf(foundation) == foundations.size() - 1) {
        foundationPile += foundation.toString();
      } else {
        foundationPile += foundation.toString() + ", ";
      }
    }
    return foundationPile;
  }

}
