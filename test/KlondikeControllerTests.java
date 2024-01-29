package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * The class representing all types of tests for the KlondikeController.
 */
public class KlondikeControllerTests {

  KlondikeModel model;
  List<Card> basicDeck;

  @Before
  public void init() {

    model = new BasicKlondike();
    basicDeck = model.getDeck();
  }

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
  public void TestValidMoveDraw() {
    init();
    Reader in = new StringReader("md 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "2♠", "2♡", "A♠"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
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
    Assert.assertTrue(out.toString().contains("Game quit!" + "\n" + "State of game when quit:"));
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

  @Test(expected = IllegalArgumentException.class)
  public void testNullInput() {
    init();
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(null, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullOutput() {
    init();
    Reader in = new StringReader("mpp 1 1 2");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, null);
  }

  @Test
  public void testLowerCaseQuit() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 2);
    Assert.assertTrue(out.toString().contains("Game quit!\n" + "State of game when quit:"));
  }

  @Test
  public void testCapitalQuit() {
    init();
    Reader in = new StringReader("Q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 2);
    Assert.assertTrue(out.toString().contains("Game quit!\n" + "State of game when quit:"));
  }

  @Test
  public void testStartGameProperly() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 2);
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameImproper() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 0, 2);
    Assert.assertTrue(model.isGameOver());
  }

  // Testing a valid input move from cascade to cascade
  @Test
  public void testValidMovePile1() {
    init();
    Reader in = new StringReader("mpp 1 1 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Error:"));
  }

  @Test
  public void testMovePileCountTransmissions() {
    init();
    Reader in = new StringReader("mpp 1 1 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testQuitMidCommand() {
    init();
    Reader in = new StringReader("mpp 1 1 q 2");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testGameOverCountTransmissions() {
    init();
    Reader in = new StringReader("mpf 1 1 mpf 2 2 mpf 2 3 mdf 4 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "A♢", "A♣"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertTrue(out.toString().contains("\nYou win!"));
  }

  @Test
  public void testValidGameStart() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 2);
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testGameOverOnStart() {
    init();
    Reader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 0, 2);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testValidMoveFromDraw() {
    init();
    Reader in = new StringReader("md 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "2♠", "2♡", "A♠"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  @Test
  public void testInvalidMoveFromDrawBadIndex() {
    init();
    Reader in = new StringReader("md 1 -1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void testValidMoveCascadeToCascade() {
    init();
    Reader in = new StringReader("mpp 1 1 2 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  @Test
  public void testInvalidMoveCascadeToCascadeBadIndices() {
    init();
    Reader in = new StringReader("mpp 1 2 3 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, model.getDeck(), false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void TestInvalidInputValidMoveDrawToFoundation() {
    init();
    Reader in = new StringReader("mdf a 1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 1, 1);
    Assert.assertTrue(out.toString().contains("invalid input"));
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  @Test(expected = IllegalStateException.class)
  public void testEmptyInput() {
    init();
    Reader in = new StringReader(" ");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 1, 1);
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testNullDeck() {
    init();
    Reader in = new StringReader(" ");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    controllerTester.playGame(model, null, false, 1, 1);
  }

  @Test
  public void testGameOverYouWin() {
    init();
    Reader in = new StringReader("mpf 1 1 mpf 2 2 mpf 2 3 mdf 4 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "A♢", "A♣"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertTrue(out.toString().contains("\nYou win!"));
  }

  @Test
  public void testGameOverYouLose() {
    init();
    Reader in = new StringReader("mdf 1 mpf 1 1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("2♡", "A♠", "2♠", "A♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertTrue(out.toString().contains("\nGame over."));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullInputs() {
    KlondikeController controller = new KlondikeTextualController(null, null);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testQuitAsFirstInput() {
    init();
    Reader in = new StringReader("mpp q 1 1 2");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitAsSecondInput() {
    init();
    Reader in = new StringReader("mpp 1 q 1 2");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitAsThirdInput() {
    init();
    Reader in = new StringReader("mpp 1 1 q 2");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testInvalidCommand() {
    init();
    Reader in = new StringReader("mpppp mpp 1 1 q 2");
    StringBuilder out = new StringBuilder();
    KlondikeController controllerTester = new KlondikeTextualController(in, out);
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "2♠", "2♡"));
    List<Card> riggedDeck = this.makeRiggedDeck(rigged);
    controllerTester.playGame(model, riggedDeck, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }



}
