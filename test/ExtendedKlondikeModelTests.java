package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Class to represent the tests for the whitehead klondike game.
 */
public class ExtendedKlondikeModelTests {

  KlondikeModel whiteheadModel;
  KlondikeModel noDrawModel;
  KlondikeModel limDrawModel;
  KlondikeModel limDrawModel2;
  List<String> rigged1;
  List<String> rigged2;
  List<String> rigged3;
  List<String> rigged4;
  List<String> rigged5;
  List<String> rigged6;
  List<String> rigged7;
  List<String> rigged8;
  List<String> rigged9;
  List<String> rigged10;
  List<String> rigged11;
  List<String> rigged12;
  List<String> rigged13;
  List<String> rigged14;
  List<String> rigged15;
  List<String> invalidRigged;


  @Before
  public void init() {
    whiteheadModel = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    noDrawModel = new LimitedDrawKlondike(0);
    limDrawModel = new LimitedDrawKlondike(1);
    limDrawModel2 = KlondikeCreator.create(KlondikeCreator.GameType.LIMITED);
    rigged1 = new ArrayList<>(Arrays.asList("2♣", "A♣", "A♢", "2♢"));
    rigged2 = new ArrayList<>(Arrays.asList("A♣", "2♣", "A♢", "2♢"));
    rigged3 = new ArrayList<>(Arrays.asList("A♣", "2♣", "2♠", "A♠"));
    rigged4 = new ArrayList<>(Arrays.asList("2♣", "A♠", "A♣", "2♠"));
    rigged5 = new ArrayList<>(Arrays.asList("A♣", "2♣", "3♣", "A♢", "2♢", "3♢"));
    rigged6 = new ArrayList<>(Arrays.asList("A♣", "A♢", "3♣", "2♢", "2♠", "3♠", "A♠", "2♣", "3♢"));
    rigged7 = new ArrayList<>(Arrays.asList("A♣", "A♠", "2♠", "2♣"));
    rigged8 = new ArrayList<>(Arrays.asList("3♣", "2♣", "A♣", "A♢", "2♢", "3♠", "A♠", "2♠", "3♢"));
    rigged9 = new ArrayList<>(Arrays.asList("3♣", "2♠", "A♣", "A♢", "2♢", "3♠", "A♠", "2♣", "3♢"));
    rigged10 = new ArrayList<>(Arrays.asList("2♣", "A♣", "2♢", "A♢"));
    rigged11 = new ArrayList<>(Arrays.asList("2♣", "A♠", "2♠", "A♣"));
    rigged12 = new ArrayList<>(Arrays.asList("2♣", "A♠", "2♠", "A♣"));
    rigged13 = new ArrayList<>(
            Arrays.asList("3♣", "2♣", "A♣", "A♢", "2♢", "3♠", "A♠", "2♠", "3♢"));
    rigged14 = new ArrayList<>(
            Arrays.asList("A♣", "2♣", "3♣", "4♣", "5♣", "6♣", "7♣", "8♣", "9♣",
                    "10♣", "J♣", "Q♣", "K♣", "A♣", "2♣", "3♣", "4♣", "5♣", "6♣", "7♣", "8♣", "9♣",
                    "10♣", "J♣", "Q♣", "K♣"));
    rigged15 = new ArrayList<>(Arrays.asList("3♣", "2♠", "A♣", "A♠", "3♠", "2♣"));
    invalidRigged = new ArrayList<>(Arrays.asList("A♣", "2♣", "3♣", "A♢", "2♢"));
  }

  private List<Card> makeRiggedDeck(List<Card> deck, List<String> loCards) {
    List<Card> riggedDeck = new ArrayList<>();
    for (int i = 0; i < loCards.size(); i++) {
      riggedDeck.add(getCard(deck, loCards.get(i)));
    }
    return riggedDeck;
  }

  private Card getCard(List<Card> deck, String s) {
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
    Assert.assertFalse(noDrawModel.isGameOver());
  }

  @Test
  public void testDiscardDrawLimitedValid() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged1);
    limDrawModel.startGame(riggedDeck, false, 2, 1);
    limDrawModel.discardDraw();
    Assert.assertEquals(limDrawModel.getDrawCards().size(), 1);
  }

  @Test
  public void testSizeAfterLimRedraw() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged5);
    limDrawModel.startGame(riggedDeck, false, 2, 3);
    limDrawModel.discardDraw();
    limDrawModel.discardDraw();
    limDrawModel.discardDraw();
    Assert.assertEquals(limDrawModel.getDrawCards().size(), 3);
  }

  @Test
  public void testSizeAtFinalRefresh() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged5);
    limDrawModel2.startGame(riggedDeck, false, 2, 3);
    for (int i = 0; i < 7; i++) {
      limDrawModel2.discardDraw();
    }
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 2);
  }

  @Test
  public void testSizeAtFinalRefreshPlusOne() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged5);
    limDrawModel2.startGame(riggedDeck, false, 2, 3);
    for (int i = 0; i < 8; i++) {
      limDrawModel2.discardDraw();
    }
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 1);
  }

  @Test
  public void testSizeAfterFinalRefresh() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged5);
    limDrawModel2.startGame(riggedDeck, false, 2, 3);
    for (int i = 0; i < 9; i++) {
      limDrawModel2.discardDraw();
    }
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 0);
  }

  @Test
  public void testDisplayAfterRedraw() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged5);
    List<String> strings = new ArrayList<>(Arrays.asList("A♢", "2♢", "3♢"));
    List<String> strings1 = new ArrayList<>(Arrays.asList("2♢", "3♢"));
    List<String> strings2 = new ArrayList<>(Arrays.asList("3♢"));
    List<Card> riggedList = this.makeRiggedDeck(limDrawModel.getDeck(), strings);
    List<Card> riggedList1 = this.makeRiggedDeck(limDrawModel.getDeck(), strings1);
    List<Card> riggedList2 = this.makeRiggedDeck(limDrawModel.getDeck(), strings2);
    limDrawModel2.startGame(riggedDeck, false, 2, 3);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 3);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 3);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 3);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 3);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 3);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 3);
    Assert.assertEquals(limDrawModel2.getDrawCards(), riggedList);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 2);
    Assert.assertEquals(limDrawModel2.getDrawCards(), riggedList1);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 1);
    Assert.assertEquals(limDrawModel2.getDrawCards(), riggedList2);
    limDrawModel2.discardDraw();
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 0);

  }

  @Test(expected = IllegalStateException.class)
  public void testExceptionThrownWhenDrawPileEmpty() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged5);
    limDrawModel2.startGame(riggedDeck, false, 2, 3);
    for (int i = 0; i < 10; i++) {
      limDrawModel2.discardDraw();
    }
    Assert.assertEquals(limDrawModel2.getDrawCards().size(), 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawBeforeStartGame() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged1);
    limDrawModel.discardDraw();
    Assert.assertTrue(limDrawModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRedrawsAllowed() {
    KlondikeModel limModel = new LimitedDrawKlondike(-1);
    Assert.assertTrue(limModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileWhiteheadRightNumberWrongSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged1);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testMoveNonKingToEmptyCascadeWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged2);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveToFoundation(0, 0);
    whiteheadModel.movePile(1, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveNonKingInvalidBuildToEmptyCascadeWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged2);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveToFoundation(0, 0);
    whiteheadModel.movePile(1, 2, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileInvalidBuild() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged3);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveToFoundation(0, 0);
    whiteheadModel.movePile(1, 2, 0);
  }

  @Test
  public void testMovePileSameColorWrongSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged4);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testStartGame() {
    whiteheadModel.startGame(whiteheadModel.getDeck(), false, 2, 1);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testValidMovePile() {
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged4);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testValidMovePileToEmptyPile() {
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged3);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveToFoundation(0, 0);
    whiteheadModel.movePile(1, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDeckStartGame() {
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), invalidRigged);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    Assert.assertTrue(whiteheadModel.isGameOver());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumPiles() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged1);
    whiteheadModel.startGame(riggedDeck, false, 0, 1);
    Assert.assertTrue(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumDraw() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged1);
    whiteheadModel.startGame(riggedDeck, false, 2, 0);
    Assert.assertTrue(whiteheadModel.isGameOver());
  }


  @Test(expected = IllegalStateException.class)
  public void testMovePileWrongNumberWrongSuitWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged6);
    whiteheadModel.startGame(riggedDeck, false, 3, 1);
    whiteheadModel.movePile(0, 1, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileRightNumberWrongSuitWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged6);
    whiteheadModel.startGame(riggedDeck, false, 3, 1);
    whiteheadModel.movePile(1, 1, 2);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testValidMoveSameColorWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged7);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(0, 1, 1);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTooManyCards() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged6);
    whiteheadModel.startGame(riggedDeck, false, 3, 1);
    whiteheadModel.movePile(0, 3, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveInvalidBuildValidSrcToDest() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged4);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 2, 0);
  }

  @Test
  public void testValidBuildValidMoveMultipleCardsWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged8);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 2, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testMoveMultipleCardAfterStart() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged13);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 2, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveInvalidBuildNotSameSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged9);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 2, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSrcIndexUnder0Whitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged9);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(-1, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNumCardsUnder0Whitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged9);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, -1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDestIndexUnder0Whitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged9);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 1, -1);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSrcIndexWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged9);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(2, 1, 0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDestIndexWhitehead() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged9);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.movePile(1, 1, 2);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromEmptyPile() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged7);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveToFoundation(0, 0);
    whiteheadModel.movePile(0, 1, 1);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }


  @Test(expected = IllegalStateException.class)
  public void testMoveDrawNoDrawCards() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged3);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveDrawToFoundation(0);
    whiteheadModel.moveDraw(0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testMoveDrawToEmptyPile() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged3);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveToFoundation(0, 0);
    whiteheadModel.moveDraw(0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testMoveDrawValidSameSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged11);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveDraw(0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testMoveDrawValidSameColor() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged12);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveDraw(0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawRightNumberWrongSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel.getDeck(), rigged10);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveDraw(0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawWrongNumberRightSuit() {
    init();
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged4);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    whiteheadModel.moveDraw(0);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testKlondikeCreatorBasic() {
    Assert.assertTrue(KlondikeCreator.create(KlondikeCreator.GameType.BASIC)
            instanceof BasicKlondike);
  }

  @Test
  public void testKlondikeCreatorLimited() {
    Assert.assertTrue(KlondikeCreator.create(KlondikeCreator.GameType.LIMITED)
            instanceof LimitedDrawKlondike);
  }

  @Test
  public void testKlondikeCreatorWhitehead() {
    Assert.assertTrue(KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD)
            instanceof WhiteheadKlondike);
  }

  @Test
  public void testMultiRunSameSuitStartGame() {
    List<Card> riggedDeck = this.makeRiggedDeck(whiteheadModel.getDeck(), rigged14);
    whiteheadModel.startGame(riggedDeck, false, 2, 1);
    Assert.assertFalse(whiteheadModel.isGameOver());
  }

  @Test
  public void testMultipleMovesFlowingTogether() {
    List<Card> riggedDeck = this.makeRiggedDeck(limDrawModel2.getDeck(), rigged15);
    limDrawModel2.startGame(riggedDeck, false, 2, 1);
    limDrawModel2.moveToFoundation(1, 0);
    limDrawModel2.moveDraw(1);
    limDrawModel2.discardDraw();
    limDrawModel2.discardDraw();
    limDrawModel2.moveDrawToFoundation(1);
    limDrawModel2.moveToFoundation(1, 1);
    limDrawModel2.moveDrawToFoundation(1);
    limDrawModel2.moveDrawToFoundation(0);
    limDrawModel2.moveToFoundation(0, 0);
    Assert.assertTrue(limDrawModel2.isGameOver());
  }
}

