package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class used to test the behaviors of the Klondike Model.
 */
public class KlondikeModelTests {

  KlondikeModel model;
  List<Card> deck;
  List<String> rigged1;
  List<String> rigged2;
  List<String> rigged3;
  List<String> rigged4;
  List<Card> riggedDeck1;
  List<Card> riggedDeck2;
  List<Card> riggedDeck3;
  List<Card> riggedDeck4;
  List<Card> riggedDeck5;
  List<Card> riggedDeck6;

  @Before
  public void init() {
    model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    deck = model.getDeck();
    rigged1 = new ArrayList<>(Arrays.asList("A♣", "2♢", "3♣", "A♢", "2♠", "3♠", "A♠", "2♣", "3♢"));
    rigged2 = new ArrayList<>(Arrays.asList("A♣", "2♢", "A♠", "A♢", "2♠", "3♠", "3♣", "2♣", "3♢"));
    rigged3 = new ArrayList<>(Arrays.asList("A♣", "2♢", "A♢", "2♠", "A♠", "2♣"));
    rigged4 = new ArrayList<>(Arrays.asList("A♣", "2♢", "A♢", "2♣"));
    riggedDeck1 = new ArrayList<>(Arrays.asList(
            new BasicCard(1, BasicCard.Suits.Hearts),
            new BasicCard(2, BasicCard.Suits.Hearts),
            new BasicCard(1, BasicCard.Suits.Diamonds),
            new BasicCard(2, BasicCard.Suits.Diamonds)));
    riggedDeck2 = new ArrayList<>(Arrays.asList(
            new BasicCard(1, BasicCard.Suits.Clubs),
            new BasicCard(3, BasicCard.Suits.Clubs),
            new BasicCard(2, BasicCard.Suits.Clubs),
            new BasicCard(1, BasicCard.Suits.Diamonds),
            new BasicCard(2, BasicCard.Suits.Spades),
            new BasicCard(3, BasicCard.Suits.Spades),
            new BasicCard(2, BasicCard.Suits.Diamonds),
            new BasicCard(3, BasicCard.Suits.Diamonds),
            new BasicCard(1, BasicCard.Suits.Spades)));
    riggedDeck3 = new ArrayList<>(Arrays.asList(
            new BasicCard(1, BasicCard.Suits.Clubs),
            new BasicCard(3, BasicCard.Suits.Clubs),
            new BasicCard(2, BasicCard.Suits.Clubs),
            new BasicCard(1, BasicCard.Suits.Diamonds),
            new BasicCard(2, BasicCard.Suits.Diamonds),
            new BasicCard(3, BasicCard.Suits.Spades),
            new BasicCard(2, BasicCard.Suits.Spades),
            new BasicCard(3, BasicCard.Suits.Diamonds),
            new BasicCard(1, BasicCard.Suits.Spades)));
    riggedDeck4 = new ArrayList<>(Arrays.asList(
            new BasicCard(1, BasicCard.Suits.Clubs),
            new BasicCard(3, BasicCard.Suits.Clubs),
            new BasicCard(1, BasicCard.Suits.Diamonds),
            new BasicCard(2, BasicCard.Suits.Diamonds),
            new BasicCard(3, BasicCard.Suits.Spades),
            new BasicCard(2, BasicCard.Suits.Spades),
            new BasicCard(2, BasicCard.Suits.Clubs),
            new BasicCard(3, BasicCard.Suits.Diamonds),
            new BasicCard(1, BasicCard.Suits.Spades)));
    riggedDeck5 = new ArrayList<>(Arrays.asList(
            new BasicCard(1, BasicCard.Suits.Hearts),
            new BasicCard(1, BasicCard.Suits.Diamonds),
            new BasicCard(2, BasicCard.Suits.Hearts),
            new BasicCard(2, BasicCard.Suits.Diamonds)));
    riggedDeck6 = new ArrayList<>(Arrays.asList(
            new BasicCard(1, BasicCard.Suits.Hearts),
            new BasicCard(2, BasicCard.Suits.Hearts),
            new BasicCard(3, BasicCard.Suits.Hearts)));
  }

  private List<Card> makeDeck(List<String> rigged) {
    init();
    List<Card> loCards = new ArrayList<>();
    for (int i = 0; i < deck.size(); i++) {
      for (int j = 0; j < rigged.size(); j++) {
        if (this.deck.get(i).toString().equals(rigged.get(j))) {
          loCards.add(deck.get(i));
        }
      }
    }
    return loCards;
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileSameSuit() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged1);
    model.startGame(riggedDeck, false, 3, 1);
    model.movePile(0, 1, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileSameNumber() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged1);
    model.startGame(riggedDeck, false, 3, 1);
    model.movePile(1, 1, 2);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveSameColor() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.movePile(1, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTooManyCards() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.movePile(0, 2, 1);
    model.movePile(0, 2, 1);
  }


  @Test(expected = IllegalStateException.class)
  public void testMovingUncoveredCards() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.movePile(1, 2, 2);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToEmptyPile() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.movePile(0, 1, 1);
    model.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawNoDrawCards() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged3);
    model.startGame(riggedDeck, false, 3, 1);
    model.moveDraw(0);
  }


  @Test(expected = IllegalStateException.class)
  public void testMoveToFoundationNotAceCard() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged1);
    model.startGame(riggedDeck, false, 3, 1);
    model.moveToFoundation(1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToFoundationAceOnAce() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.moveToFoundation(0, 1);
    model.moveDrawToFoundation(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawNoDrawCardsToFoundation() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged3);
    model.startGame(riggedDeck, false, 3, 1);
    model.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToFoundationNonAce() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.moveDrawToFoundation(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testKingToAce() {
    init();
    model.startGame(model.getDeck(), false, 3, 1);
    model.moveDraw(0);
  }

  @Test
  public void testIsGameOver() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged3);
    model.startGame(riggedDeck, false, 3, 1);
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMovingInvalidToEmptyCascade() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged2);
    model.startGame(riggedDeck, false, 3, 1);
    model.moveToFoundation(0, 0);
    model.movePile(1, 1, 0);
  }


  @Test
  public void testDiscardDrawRecycle() {
    init();

    List<Card> riggedDeck = this.makeDeck(rigged4);
    model.startGame(riggedDeck, false, 2, 1);
    model.discardDraw();
    Assert.assertEquals(riggedDeck.get(3), model.getDrawCards().get(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testWrongSuitFoundation() {
    init();
    List<Card> riggedDeck = this.makeDeck(rigged4);
    model.startGame(riggedDeck, false, 2, 1);
    model.moveToFoundation(0, 0);
    model.moveDrawToFoundation(0);
  }

  @Test
  public void testToString() {
    init();
    Card card = new BasicCard(1, BasicCard.Suits.Clubs);
    Assert.assertEquals("A♣", card.toString());
  }

  @Test
  public void getCardAt() {
    init();
    model.startGame(riggedDeck1, false, 2, 1);
    Assert.assertEquals(model.getCardAt(0, 0).toString(), "A♡");
  }

  @Test
  public void testDiscardEntireDrawPile() {
    init();
    model.startGame(riggedDeck2, false, 3, 3);
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    Assert.assertEquals(this.model.getDrawCards().toString(), "[2♢, 3♢, A♠]");
  }

  @Test
  public void testMoveDrawToEmptyFoundation() {
    init();
    model.startGame(riggedDeck2, false, 3, 3);
    model.discardDraw();
    model.discardDraw();
    model.moveDrawToFoundation(0);
    Assert.assertEquals(model.getCardAt(0).toString(), "A♠");
  }

  @Test
  public void testMoveDrawToNonEmptyFoundation() {
    init();
    model.startGame(riggedDeck3, false, 3, 3);
    model.discardDraw();
    model.discardDraw();
    model.moveDrawToFoundation(0);
    model.moveDrawToFoundation(0);
    Assert.assertEquals(model.getCardAt(0).toString(), "2♠");
  }

  @Test
  public void testMoveDrawWithValidInputsToEmptyPile() {
    init();
    model.startGame(riggedDeck4, false, 3, 3);
    model.moveToFoundation(0, 0);
    model.moveDrawToFoundation(0);
    Assert.assertEquals(model.getCardAt(0).toString(), "2♣");
  }

  @Test
  public void testToStringBasic() {
    init();
    TextualView view = new KlondikeTextualView(model);
    model.startGame(riggedDeck1, false, 2, 1);
    Assert.assertFalse(model.isCardVisible(1,0));
    Assert.assertEquals(view.toString(), "Draw: 2♢\n"
            + "Foundation: <none>, <none>\n" + " A♡  ?\n" + "    A♢");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumPiles() {
    init();
    model.startGame(riggedDeck1, false, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumDraw() {
    init();
    model.startGame(riggedDeck1, false, 2, 0);
  }

  @Test
  public void testShuffle() {
    init();
    model = new BasicKlondike(1);
    model.startGame(riggedDeck1, true, 2, 1);
    Assert.assertFalse(riggedDeck1.get(2).equals(this.deck.get(2)));
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveRightValueWrongSuit() {
    init();
    model.startGame(riggedDeck5, false, 2, 1);
    model.movePile(0, 1, 1);
  }

  @Test
  public void testIsGameOverTrue() {
    init();
    model.startGame(riggedDeck6, false, 2, 1);
    Assert.assertTrue(model.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtInvalid() {
    init();
    model.startGame(riggedDeck6, false, 2, 1);
    model.getCardAt(0, 1);
  }

  @Test
  public void testGetCardAtValid() {
    init();
    model.startGame(riggedDeck6, false, 2, 1);
    Assert.assertEquals("A♡", model.getCardAt(0, 0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testGameAlreadyStarted() {
    init();
    model.startGame(riggedDeck6, false, 2, 1);
    model.startGame(riggedDeck6, false, 2, 1);
  }

  @Test
  public void testValidMoveDrawToFoundation() {
    init();
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠"));
    List<Card> riggedDeck = this.makeDeck(rigged);
    model.startGame(riggedDeck, false, 1, 1);
    model.moveDrawToFoundation(0);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testMultipleMoves() {
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "A♢", "A♣"));
    List<Card> riggedDeck = this.makeDeck(rigged);
    model.startGame(riggedDeck, false, 2, 1);
    model.moveToFoundation(0, 0);
    model.moveToFoundation(1, 1);
    model.moveToFoundation(1, 2);
    model.moveDrawToFoundation(3);
    Assert.assertEquals(model.getScore(), 4);
  }

  @Test
  public void testManyRedraw() {
    List<String> rigged = new ArrayList<>(Arrays.asList("A♡", "A♠", "A♢", "A♣"));
    List<Card> riggedDeck = this.makeDeck(rigged);
    model.startGame(riggedDeck, false, 2, 1);
    for (int i = 0; i < 20; i++) {
      model.discardDraw();
    }
    Assert.assertFalse(model.isGameOver());
  }


}


