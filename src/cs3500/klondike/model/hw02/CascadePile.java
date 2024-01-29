package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cascade pile in the Klondike solitaire game.
 * Cascade piles are used to build sequences of cards in descending order and alternating colors.
 */
public class CascadePile extends AbstractPile {

  /**
   * Constructs a cascade pile with the given invisible and visible cards.
   *
   * @param invisible the list of cards meant to stay invisible in this pile
   *                  (index is zero-based, last card in list is last card and first to become
   *                  visible if needed)
   * @param visible   the list of cards meant to stay visible in this pile
   *                  (index is zero-based, last card in list is the one at the very bottom
   *                  of the cascade pile)
   */
  public CascadePile(List<Card> invisible, List<Card> visible) {
    super(invisible, visible);
  }

  /**
   * Returns a string representation of this cascade pile.
   *
   * @param loCascades the list of cascades you want to represent as a string
   * @return the full cascade pile in string form
   */
  public String toString(List<CascadePile> loCascades) {
    List<CascadePile> cascades = new ArrayList<>(loCascades);

    String cascadePile = "";
    for (int i = 1; i < Math.pow(loCascades.size(), 2); i++) {
      for (CascadePile cascade : cascades) {
        if (i <= loCascades.size() && cascade.invisible.isEmpty()
                && cascade.visible.isEmpty()) {
          cascadePile += "  X";
          if (i % loCascades.size() == 0) {
            cascadePile += "\n";
          }
        } else if (!cascade.invisible.isEmpty()) {
          cascadePile += "  ?";
          cascade.invisible.remove(0);
          i++;
          if (i % loCascades.size() == 0) {
            cascadePile += "\n";
          }
        } else if (!cascade.visible.isEmpty()) {
          if (cascade.visible.get(0).toString().length() == 3) {
            cascadePile += cascade.visible.get(0).toString();
            cascade.visible.remove(0);
            if (i % loCascades.size() == 0) {
              cascadePile += "\n";
            }
          } else {
            cascadePile += " " + cascade.visible.get(0).toString();
            cascade.visible.remove(0);
            if (i % loCascades.size() == 0) {
              cascadePile += "\n";
            }
          }
        } else {
          cascadePile += "   ";
          if (i % loCascades.size() == 0) {
            cascadePile += "\n";
          }
        }
      }
    }
    return cascadePile;
  }

}
