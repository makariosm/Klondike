package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class representing the testing for the Examplar assignment.
 */
public class ExamplarExtendedModelTests {

  KlondikeModel limDrawModel;
  KlondikeModel whiteHeadModel;
  List<Card> deck;
  KlondikeModel noDrawModel;
  List<String> rigged1;
  List<String> rigged2;
  List<String> rigged3;
  List<String> rigged4;


  @Before
  public void init() {
    limDrawModel = new LimitedDrawKlondike(1);
    whiteHeadModel = new WhiteheadKlondike();
    deck = limDrawModel.getDeck();
    noDrawModel = new LimitedDrawKlondike(0);
    rigged1 = new ArrayList<>(Arrays.asList("2♣", "A♣", "A♢", "2♢"));
    rigged2 = new ArrayList<>(Arrays.asList("A♣", "2♣", "A♢", "2♢"));
    rigged3 = new ArrayList<>(Arrays.asList("A♣", "2♣", "A♠", "2♠"));
    rigged4 = new ArrayList<>(Arrays.asList("2♣", "A♣", "A♠", "2♠"));
  }

  private List<Card> makeRiggedDeck(List<Card> deck, List<String> loCards) {
    List<Card> riggedDeck = new ArrayList<>();
    for (int i = 0; i < loCards.size(); i++) {
      riggedDeck.add(addCard(deck, loCards.get(i)));
    }
    return riggedDeck;
  }

  private Card addCard(List<Card> deck, String s) {
    for (int i = 0; i < deck.size(); i++) {
      if (s.equals(deck.get(i).toString())) {
        return deck.get(i);
      }
    }
    throw new IllegalArgumentException("Not real card");
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawLimitedInvalid() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(noDrawModel.getDeck(), rigged1);
    noDrawModel.startGame(riggedDeck, false, 2, 1);
    noDrawModel.discardDraw();
    noDrawModel.discardDraw();
    Assert.assertFalse(whiteHeadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileWhiteheadRightNumberWrongSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteHeadModel.getDeck(), rigged1);
    whiteHeadModel.startGame(riggedDeck, false, 2, 1);
    whiteHeadModel.movePile(1, 1, 0);
    Assert.assertFalse(whiteHeadModel.isGameOver());
  }

  @Test
  public void testMoveToNonKingToEmptyCascade() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteHeadModel.getDeck(), rigged2);
    whiteHeadModel.startGame(riggedDeck, false, 2, 1);
    whiteHeadModel.moveToFoundation(0,0);
    whiteHeadModel.movePile(1,1,0);
    Assert.assertFalse(whiteHeadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileInvalidBuild() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteHeadModel.getDeck(), rigged3);
    whiteHeadModel.startGame(riggedDeck, false, 2, 1);
    whiteHeadModel.moveToFoundation(0,0);
    whiteHeadModel.movePile(1,2,0);
  }

  @Test
  public void testMovePileSameColorWrongSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteHeadModel.getDeck(), rigged4);
    whiteHeadModel.startGame(riggedDeck, false, 2, 1);
    whiteHeadModel.movePile(1,1,0);
    Assert.assertFalse(whiteHeadModel.isGameOver());
  }

  @Test
  public void testDiscardDrawLimitedValid() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged1);
    limDrawModel.startGame(riggedDeck, false, 2, 1);
    limDrawModel.discardDraw();
    Assert.assertEquals(limDrawModel.getDrawCards().size(), 1);
  }


}
