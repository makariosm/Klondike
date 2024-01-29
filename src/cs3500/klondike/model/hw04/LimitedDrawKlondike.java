package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.AbstractKlondike;

import java.util.ArrayList;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of LimitedDrawKlondike will have a one-argument
 * constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class LimitedDrawKlondike extends AbstractKlondike {

  private final int numTimesRedrawAllowed;
  private int drawCount;


  /**
   * Constructs a BasicKlondike instance with a specified random seed.
   *
   * @param randSeed the random seed to use
   */
  public LimitedDrawKlondike(int randSeed, int numTimesRedrawAllowed) {
    super(randSeed);
    this.cascades = new ArrayList<>();
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Invalid number of redraws allowed.");

    } else {
      this.numTimesRedrawAllowed = numTimesRedrawAllowed;
      this.drawCount = 0;
    }
  }

  /**
   * Constructs a BasicKlondike instance with a default random seed.
   * @param numTimesRedrawAllowed the number of times the player is allowed to go through
   *                             the drawpile while discarding
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();
    this.cascades = new ArrayList<>();
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Invalid number of redraws allowed.");
    } else {
      this.numTimesRedrawAllowed = numTimesRedrawAllowed;
      this.drawCount = 0;
    }
  }

  private void gameNotStartedWarning() {
    if (this.isGameOver) {
      throw new IllegalStateException("Game not started yet.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discardDraw() throws IllegalStateException {
    gameNotStartedWarning();
    this.drawCount += 1;
    this.drawPile.discardDrawLimited(this.numTimesRedrawAllowed, this.drawCount);
  }


}
