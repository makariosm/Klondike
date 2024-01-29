package cs3500.klondike;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class used for testing specifically for the examplar assignment.
 */
public class ExamplarControllerTests {

  KlondikeModel model;
  List<Card> basicDeck;


  @Before
  public void init() {
    model = new BasicKlondike();
    basicDeck = model.getDeck();
  }

  /**
   * Makes a rigged deck based on the specific strings that I pass into it.
   *
   * @param rigged the list of strings that represent the cards I want
   * @return the rigged list of cards (rigged deck)
   */
  private List<Card> makeRiggedDeck(List<String> rigged) {
    init();
    List<Card> loCards = new ArrayList<>();
    for (int j = 0; j < rigged.size(); j++) {
      for (int i = 0; i < basicDeck.size(); i++) {
        if (this.basicDeck.get(i).toString().equals(rigged.get(j))) {
          loCards.add(basicDeck.get(i));
        }
      }
    }
    return loCards;
  }

  // Making a valid move from draw pile
  @Test
  public void TestInvalidMoveDrawToPile() {
    init();
    Reader in = new StringReader("md 1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 1, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void TestValidMoveDrawToFoundation() {
    init();
    Reader in = new StringReader("mdf 1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 1, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  // Testing a valid input move from cascade to cascade
  @Test
  public void testValidMovePile() {
    init();
    Reader in = new StringReader("mpp 1 1 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  // Making an invalid move from draw pile (specifically bad index)
  @Test
  public void testInvalidMoveDrawIndex() {
    init();
    Reader in = new StringReader("md 1 -1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  // Inputting an invalid index for a cascade pile
  @Test
  public void testInvalidDestinationIndex() {
    init();
    Reader in = new StringReader("mpp 1 2 3 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  // Checking how a non-number input in handled
  @Test
  public void testNonNumberIndex() {
    init();
    Reader in = new StringReader("md k q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 3, 1);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  // Inputting valid indices but unallowable play from cascade to cascade
  @Test
  public void testGoodIndicesInvalidMovePile() {
    init();
    Reader in = new StringReader("md 1 mpp 1 1 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }


  //Checking that the game is displayed after quitting
  @Test
  public void testGameStateAfterQuit() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 2);
    Assert.assertTrue(out.toString().contains("State of game when quit:"));
  }

  // Checking the proper quitting final full message
  @Test
  public void testQuitFinalFullMessage() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 3, 2);
    Assert.assertTrue(out.toString().contains("Game quit!\n" + "State of game when quit:"));
  }

  // Checking that the score gets outputted when the game ends
  @Test
  public void testScoreOutput() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 2);
    Assert.assertTrue(out.toString().contains("\nScore: 0"));
  }

}


