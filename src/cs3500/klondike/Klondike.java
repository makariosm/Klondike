package cs3500.klondike;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * The entry level class to start a game of Klondike.
 */
public class Klondike {

  /**
   * The entry level main method that uses the command line arguments to start the Klondike game.
   *
   * @param args the array of arguments passed in from the command line
   */
  public static void main(String[] args) {

    if (args.length == 0) {
      throw new IllegalArgumentException("No command line given");
    }

    KlondikeTextualController ktc = new KlondikeTextualController(new InputStreamReader(System.in),
            System.out);

    List<Integer> defaultVals = new ArrayList<>(Arrays.asList(7, 3));

    KlondikeModel model;

    switch (args[0]) {

      case "whitehead":
        model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
        for (int i = 1; i < args.length; i++) {
          defaultVals.set(i - 1, Integer.parseInt(args[i]));
        }

        try {
          ktc.playGame(model, model.getDeck(), true, defaultVals.get(0), defaultVals.get(1));
        } catch (Exception ignored) {
        }
        break;

      case "basic":

        model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        for (int i = 1; i < args.length; i++) {
          defaultVals.set(i - 1, Integer.parseInt(args[i]));
        }

        try {
          ktc.playGame(model, model.getDeck(), true, defaultVals.get(0), defaultVals.get(1));
        } catch (Exception ignored) {
        }
        break;

      case "limited":
        if (args.length < 2) {
          throw new IllegalArgumentException("Limited must be followed by integer");
        }
        model = new LimitedDrawKlondike(Integer.parseInt(args[1]));
        for (int i = 2; i < args.length; i++) {
          defaultVals.set(i - 2, Integer.parseInt(args[i]));
        }

        try {
          ktc.playGame(model, model.getDeck(), true, defaultVals.get(0), defaultVals.get(1));
        } catch (IllegalStateException e) {
          throw new IllegalArgumentException("Bad arguments cannot start game.");
        }

        break;

      default:
        throw new IllegalArgumentException("Invalid game type");
    }


  }
}
