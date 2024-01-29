package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * The class that creates instances of different versions of Klondike.
 */
public class KlondikeCreator {

  /**
   * GameType is one of: BASIC,LIMITED,WHITEHEAD.
   * The different possible versions of klondike
   */
  public enum GameType {
    BASIC, LIMITED, WHITEHEAD;

  }

  /**
   * Creates an instance of the desired klondike game.
   * @param type the desired version of klondike
   * @return the instance of the klondike model that it took in
   */
  public static KlondikeModel create(GameType type) {

    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(2);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("Not a valid game type.");
    }
  }
}
