package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**
 * The class that represents a controller for the.
 * klondike game that specifically uses text for commands.
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {

  private Readable in;
  private Appendable out;
  private boolean quit;
  private TextualView klondikeView;
  private boolean isGameOver;

  /**
   * The constructor for the KlondikeTextualController.
   *
   * @param r the readable that the controller takes in for inputs
   * @param a the appendable that the controller uses to output
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (Objects.isNull(r) || Objects.isNull(a)) {
      throw new IllegalArgumentException(
              "The controller is not compatible with one of the arguments.");
    } else {
      this.in = r;
      this.out = a;
      this.quit = false;
      this.isGameOver = false;

    }
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck,
                       boolean shuffle, int numPiles, int numDraw) {
    Scanner scan = new Scanner(in);
    int source = 0;
    int dest = 0;
    int numCards = 0;
    List<Integer> inputs = new ArrayList<>(Arrays.asList(source, dest, numCards));

    if (Objects.isNull(model)) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    // start the game
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
      this.klondikeView = new KlondikeTextualView(model, this.out);
    } catch (IllegalArgumentException | IllegalStateException e) {
      throw new IllegalStateException("At least one input is invalid. Cannot start game.");
    }

    while (!this.quit) {
      if (this.isGameWon(model, deck)) {
        this.quit = true;
        break;
      } else if (model.isGameOver()) {
        this.quit = true;
        this.isGameOver = true;
        break;
      }

      //transmit game at the beginning and after every move
      this.transmitGame(model);

      // while the game is still active process inputs
      String currString;
      try {
        currString = scan.next();
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("No more inputs.");
      }
      switch (currString) {
        // move pile case
        case "mpp":
          try {
            for (int i = 0; i < inputs.size(); i++) {
             String stringMPP = this.rereadInputs(scan);
              if (stringMPP.equalsIgnoreCase("q")) {
                this.quit = true;
                break;
              } else {
                inputs.set(i, Integer.valueOf(stringMPP));
              }
            }
            if (this.quit) {
              break;
            }
            source = inputs.get(0) - 1;
            numCards = inputs.get(1);
            dest = inputs.get(2) - 1;
            model.movePile(source, numCards, dest);
          } catch (IllegalArgumentException | IllegalStateException e) {
            this.transmitInvalid(e);
            break;
          }
          break;

        // move draw case
        case "md":
          try {
            String stringMD = this.rereadInputs(scan);
            if (stringMD.equalsIgnoreCase("q")) {
              this.quit = true;
              break;
            } else {
              inputs.set(1, Integer.valueOf(stringMD));
            }
            if (this.quit) {
              break;
            }
            dest = inputs.get(1) - 1;
            model.moveDraw(dest);
          } catch (IllegalArgumentException | IllegalStateException e) {
            this.transmitInvalid(e);
          }
          break;

        // move pile to foundation case
        case "mpf":
          try {
            for (int i = 0; i < 2; i++) {
              String stringMPF = this.rereadInputs(scan);
              if (stringMPF.equalsIgnoreCase("q")) {
                this.quit = true;
                break;
              } else {
                inputs.set(i, Integer.valueOf(stringMPF));
              }
            }
            if (this.quit) {
              break;
            }
            source = inputs.get(0) - 1;
            dest = inputs.get(1) - 1;
            model.moveToFoundation(source, dest);

          } catch (IllegalArgumentException | IllegalStateException e) {
            this.transmitInvalid(e);
          }
          break;

        // move draw to foundation
        case "mdf":
          try {
            String stringMDF = this.rereadInputs(scan);
            if (stringMDF.equalsIgnoreCase("q")) {
              this.quit = true;
              break;
            } else {
              inputs.set(1, Integer.valueOf(stringMDF));
            }
            if (this.quit) {
              break;
            }
            dest = inputs.get(1) - 1;
            model.moveDrawToFoundation(dest);
          } catch (IllegalArgumentException | IllegalStateException e) {
            this.transmitInvalid(e);
          }
          break;

        //discard draw case
        case "dd":
          try {
            model.discardDraw();
          } catch (IllegalStateException e) {
            this.transmitInvalid(e);
          }
          break;

        //quitting case
        case "q":
        case "Q":
          this.quit = true;
          break;

        default:
          this.transmitMessage("Invalid move. Play again. " + currString + " cannot be handled.");
          this.transmitNewLine();
      }

    }

    if (this.isGameWon(model, deck)) {
      klondikeView.render();
      this.transmitMessage("\n");
      this.transmitMessage("You win!");
      this.transmitNewLine();
      this.isGameOver = true;
    } else if (this.isGameOver) {
      this.isGameOver = false;
      klondikeView.render();
      this.transmitNewLine();
      this.transmitMessage("Game over. Score: " + model.getScore());
      this.transmitNewLine();
      this.isGameOver = true;
    } else {
      this.transmitMessage("Game quit!");
      this.transmitNewLine();
      this.transmitMessage("State of game when quit:");
      this.transmitNewLine();
      this.transmitGame(model);
      this.isGameOver = true;
    }
  }

  private void transmitMessage(String message) {
    try {
      out.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private void transmitInvalid(Exception e) {
    this.transmitMessage("Invalid move. Play again. " + e.getMessage());
    this.transmitNewLine();
  }

  private void transmitGame(KlondikeModel model) {
    try {
      klondikeView.render();
      this.transmitNewLine();
      out.append("Score: ").append(String.valueOf(model.getScore()));
      this.transmitNewLine();
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private void transmitNewLine() {
    try {
      out.append("\n");
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private String rereadInputs(Scanner scan) {

    while (scan.hasNext()) {
      String string = scan.next();
      try {
        Integer.parseInt(string);
        return string;
      } catch (NumberFormatException e) {
        if (string.equals("q") || string.equals("Q")) {
          this.quit = true;
          return string;
        } else {
          this.transmitMessage(string + " is an invalid input.\n");
        }
      }
    }
    throw new IllegalStateException("No more inputs. Cannot continue game.");
  }

  private boolean isGameWon(KlondikeModel model, List<Card> deck) {
    try {
      return model.getScore() == deck.size();
    } catch (IllegalStateException e) {
      return false;
    }
  }

}
