package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * The class representing tests that specifically test to make sure the controller properly passes.
 * on inputs to the model.
 */
public class ControllerModelConnectionTests {

  KlondikeModel mock;
  KlondikeController controller;
  StringReader in;
  StringBuilder out;

  @Before
  public void init() {
    out = new StringBuilder();
    mock = new MockKlondikeModel(out);
  }

  @Test
  public void testMPPInputs() {
    in = new StringReader("mpp 1 1 1 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades),
                    new BasicCard(1, BasicCard.Suits.Hearts)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertEquals(out.toString(),
            "numPiles = 1 numDraw = 1\nsrcPile = 0 numCards = 1 destPile = 0\n");
  }

  @Test
  public void testMDInputs() {
    in = new StringReader("md 1 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades),
                    new BasicCard(1, BasicCard.Suits.Hearts)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertEquals(out.toString(),
            "numPiles = 1 numDraw = 1\ndestPile = 0\n");
  }

  @Test
  public void testMPFInputs() {
    in = new StringReader("mpf 2 2 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades),
                    new BasicCard(1, BasicCard.Suits.Hearts)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertEquals(out.toString(),
            "numPiles = 1 numDraw = 1\nsrcPile = 1 foundationPile = 1\n");
  }

  @Test
  public void testMDFInputs() {
    in = new StringReader("mdf 2 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades),
                    new BasicCard(1, BasicCard.Suits.Hearts)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertEquals(out.toString(),
            "numPiles = 1 numDraw = 1\nfoundationPile = 1\n");
  }

  @Test
  public void testDDInputs() {
    in = new StringReader("dd q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades),
                    new BasicCard(1, BasicCard.Suits.Hearts)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertEquals(out.toString(),
            "numPiles = 1 numDraw = 1\nDiscarded\n");
  }

  @Test(expected = IllegalStateException.class)
  public void testMyModelVSController() {
    in = new StringReader("mpp");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertFalse(mock.isGameOver());
  }

  @Test
  public void testController() {
    in = new StringReader("mpp 1 k 2 3 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    List<Card> deck =
            new ArrayList<>(Arrays.asList(new BasicCard(1, BasicCard.Suits.Spades)));
    controller.playGame(mock, deck, false, 1, 1);
    Assert.assertFalse(mock.isGameOver());
  }


}
