package cs3500.klondike.view;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private Appendable out;
  // ... any other fields you need

  /**
   * Constructs a new KlondikeTextualView with the specified KlondikeModel.
   *
   * @param model the KlondikeModel to render
   * @param out   the output for the model that the view will display to
   */
  public KlondikeTextualView(KlondikeModel model, Appendable out) {
    this.model = model;
    this.out = out;
  }

  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
  }

  /**
   * Returns a string representation of the Klondike game based on the current state of the model.
   *
   * @return a string representation of the game
   */
  public String toString() {
    return this.makeDrawPile() + "\n" + this.makeFoundationPile() + "\n" + this.makeCascadePile();
  }

  private String makeDrawPile() {
    List<Card> loDrawCards = model.getDrawCards();
    String drawPile = "Draw: ";

    for (int i = 0; i < loDrawCards.size(); i++) {
      if (i == loDrawCards.size() - 1) {
        drawPile += loDrawCards.get(i).toString();
      } else {
        drawPile += loDrawCards.get(i).toString() + ", ";
      }
    }

    return drawPile;
  }

  private String foundationToString(Card card) {
    if (Objects.isNull(card)) {
      return "<none>";
    } else {
      return card.toString();
    }
  }


  private String makeFoundationPile() {
    String foundationPile = "Foundation: ";

    for (int i = 0; i < model.getNumFoundations(); i++) {
      if (i == model.getNumFoundations() - 1) {
        foundationPile += foundationToString(model.getCardAt(i));
      } else {
        foundationPile += foundationToString(model.getCardAt(i)) + ", ";

      }
    }
    return foundationPile;
  }

  private String cascadeCardToString(int column, int row) {
    // how an empty cascade should behave
    if (model.getPileHeight(column) == 0) {
      if (row == 0) {
        return "  X";
      } else {
        return "   ";
      }
    } else if (row >= model.getPileHeight(column)) {
      return "   ";
    } else if (!model.isCardVisible(column, row)) {
      return "  ?";
    } else if (model.isCardVisible(column, row)) {
      if (model.getCardAt(column, row).toString().length() == 3) {
        return model.getCardAt(column, row).toString();
      } else {
        return " " + model.getCardAt(column, row).toString();
      }
    } else {
      return "   ";
    }
  }

  private String makeCascadePile() {
    StringBuilder cascadePile = new StringBuilder();

    for (int row = 0; row < model.getNumRows(); row++) {
      for (int column = 0; column < model.getNumPiles(); column++) {
        if (column == model.getNumPiles() - 1 && row != model.getNumRows() - 1) {
          cascadePile.append(this.cascadeCardToString(column, row)).append("\n");
        } else {
          cascadePile.append(this.cascadeCardToString(column, row));
        }
      }
    }
    return cascadePile.toString();
  }


  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() {
    try {
      out.append(this.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Cannot render the game properly.");
    }
  }
}

