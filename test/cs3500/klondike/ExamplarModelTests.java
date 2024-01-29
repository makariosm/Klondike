package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class used for the Examplar assignment to test possible bugs.
 */
public class ExamplarModelTests {

  KlondikeModel model;
  List<Card> deck;
  List<String> rigged1;
  List<String> rigged2;
  List<String> rigged3;
  List<String> rigged4;

  @Before
  public void init() {
    model = new BasicKlondike();
    deck = model.getDeck();
    rigged1 = new ArrayList<>(Arrays.asList("A♣", "2♢", "3♣", "A♢", "2♠", "3♠", "A♠", "2♣", "3♢"));
    rigged2 = new ArrayList<>(Arrays.asList("A♣", "2♢", "A♠", "A♢", "2♠", "3♠", "3♣", "2♣", "3♢"));
    rigged3 = new ArrayList<>(Arrays.asList("A♣", "2♢", "A♢", "2♠", "A♠", "2♣"));
    rigged4 = new ArrayList<>(Arrays.asList("A♣", "2♢", "A♢", "2♣"));
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

}


