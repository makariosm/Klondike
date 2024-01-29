package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.AbstractKlondike;
import cs3500.klondike.model.hw02.CascadePile;
import cs3500.klondike.model.hw02.DrawPile;
import cs3500.klondike.model.hw02.FoundationPile;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of WhiteheadKlondike will have a zero-argument
 * (i.e. default) constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class WhiteheadKlondike extends AbstractKlondike {

  private List<List<Card>> cascadePiles;


  public WhiteheadKlondike() {
    super();
    this.cascadePiles = new ArrayList<>();
  }

  private void gameNotStartedWarning() {
    if (this.isGameOver) {
      throw new IllegalStateException("Game not started yet.");
    }
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {

    Utils.startGameExceptions(deck, this.isGameOver, numPiles, numDraw);

    //Check if there are enough cards for the cascade piles and foundation piles.
    int numAces = 0;
    for (Card card : deck) {
      if (card.isCardValue(1)) {
        numAces++;
      }
    }

    this.isGameOver = false;
    this.foundationPiles = new ArrayList<>(numAces);
    this.deck = new ArrayList<>(deck);

    //shuffle if needed

    if (shuffle) {
      Collections.shuffle(this.deck, rand);
    }

    //make foundation piles
    for (Card c : this.deck) {
      if (c.isCardValue(1)) {
        this.foundationPiles.add(new FoundationPile(new ArrayList<>()));
      }
    }



    this.cascadePiles = new ArrayList<>(numPiles);
    this.drawPile = new DrawPile(new ArrayList<>(), new ArrayList<>());

    //Deal all cards into proper cascade piles
    Utils.cascadesMakerWhitehead(this.cascadePiles, this.deck, numPiles);

    //Set up draw pile
    Utils.drawPileMaker(this.deck, drawPile, numDraw);


    this.deck = deck;

  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
    gameNotStartedWarning();
    badArgumentsCheckerMovePile(srcPile, numCards, destPile);

    List<Card> listToBeMoved = new ArrayList<>();
    for (int i = cascadePiles.get(srcPile).size() - 1;
         i >= cascadePiles.get(srcPile).size() - numCards; i--) {
      listToBeMoved.add(0, this.cascadePiles.get(srcPile).get(i));
    }

    if (this.cascadePiles.get(destPile).isEmpty()) {
      if (this.isValidMoveBuild(listToBeMoved)) {
        movePileHelper(srcPile, numCards, destPile, listToBeMoved);
      }
      else {
        throw new IllegalStateException("Move is invalid.");
      }
    } else if (listToBeMoved.size() == 1) {
      movePileConditionsOneCard(srcPile, numCards, destPile, listToBeMoved);
    }
    else {
      movePileConditionsMultipleCards(srcPile, numCards, destPile, listToBeMoved);
    }
  }

  private void movePileConditionsMultipleCards(int srcPile, int numCards, int destPile,
                                               List<Card> listToBeMoved) {
    Card srcCard =
            this.cascadePiles.get(srcPile).get(cascadePiles.get(srcPile).size() - numCards);
    Card destCard = this.cascadePiles.get(destPile).get(cascadePiles.get(destPile).size() - 1);
    if (this.isValidMoveBuild(listToBeMoved)
            && destCard.cardOntoCardPileSameColor(srcCard)) {
      movePileHelper(srcPile, numCards, destPile, listToBeMoved);
    }
    else {
      throw new IllegalStateException("Move is invalid.");
    }
  }

  private void movePileConditionsOneCard(int srcPile, int numCards, int destPile,
                                         List<Card> listToBeMoved) {
    Card srcCard =
            this.cascadePiles.get(srcPile).get(cascadePiles.get(srcPile).size() - numCards);
    Card destCard = this.cascadePiles.get(destPile).get(cascadePiles.get(destPile).size() - 1);
    if (destCard.cardOntoCardPileSameColor(srcCard)) {
      movePileHelper(srcPile, numCards, destPile, listToBeMoved);
    }
    else {
      throw new IllegalStateException("Move is invalid.");
    }
  }

  private void movePileHelper(int srcPile, int numCards, int destPile, List<Card> listToBeMoved) {
    if (cascadePiles.get(srcPile).size() > cascadePiles.get(srcPile).size() - numCards) {
      for (int i = 0; i < numCards; i++) {
        this.cascadePiles.get(srcPile).remove(cascadePiles.get(srcPile).size() - 1);
      }
      for (Card card : listToBeMoved) {
        cascadePiles.get(destPile).add(card);
      }
    }
  }

  private boolean isValidMoveBuild(List<Card> loCards) {

    if (loCards.size() == 1) {
      return true;
    } else {
      for (int index = 0; index <= loCards.size() - 2; index++) {
        if (loCards.get(index).getValue() != (loCards.get(index + 1).getValue() + 1)
                || !loCards.get(index).isSameSuit(loCards.get(index + 1))) {
          return false;
        }
      }
    }
    return true;
  }

  private void badArgumentsCheckerMovePile(int srcPile, int numCards, int destPile) {
    if ((srcPile < 0 || srcPile >= cascadePiles.size())
            || (destPile < 0 || destPile >= cascadePiles.size())
            || (destPile == srcPile)) {
      throw new IllegalArgumentException("Source Pile or Destination Pile Index is Invalid");
    } else if (numCards > cascadePiles.get(srcPile).size() || numCards < 0) {
      throw new IllegalArgumentException("Number of cards you are trying to move is invalid.");
    } else if (cascadePiles.get(srcPile).isEmpty()) {
      throw new IllegalArgumentException("Source pile is empty.");
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    gameNotStartedWarning();
    this.drawPile.whiteheadMoveDraw(destPile, this.cascadePiles.size(), this.cascadePiles);
  }


  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalStateException {
    gameNotStartedWarning();
    badArgumentsCheckerMovePileToFoundation(srcPile, foundationPile);
    CascadePile dummyCascade = new CascadePile(new ArrayList<>(), new ArrayList<>());
    dummyCascade.moveToFoundation(this.foundationPiles.get(foundationPile),
            new ArrayList<>(), this.cascadePiles.get(srcPile));
  }

  private void badArgumentsCheckerMovePileToFoundation(int srcPile, int foundationPile) {
    if ((srcPile < 0 || srcPile >= cascadePiles.size())
            || (foundationPile < 0 || foundationPile >= foundationPiles.size())) {
      throw new IllegalArgumentException("Source Pile or Foundation Pile Index is Wrong");
    }
    else if (this.cascadePiles.get(srcPile).isEmpty()) {
      throw new IllegalStateException("Source pile is empty.");
    }

  }

  @Override
  public int getNumRows() {
    gameNotStartedWarning();
    int biggestRow = 0;
    for (int index = 0; index < this.cascadePiles.size(); index++) {
      if (this.cascadePiles.get(index).size() > biggestRow) {
        biggestRow = this.cascadePiles.get(index).size();
      }
    }
    return biggestRow;
  }

  @Override
  public int getNumPiles() {

    gameNotStartedWarning();
    return this.cascadePiles.size();
  }


  @Override
  public boolean isGameOver() throws IllegalStateException {

    gameNotStartedWarning();
    if (this.drawPile.isDrawVisEmpty() && !(this.canMovePilesToPiles()
            || this.canMovePilesToFoundations())) {
      this.isGameOver = true;
      return true;
    }
    return this.isGameOver;

  }

  private boolean canMovePilesToPile(List<Card> dest) {
    boolean canMoveAny = false;
    for (List<Card> cascade : cascadePiles) {
      for (Card card : cascade) {
        if (!dest.isEmpty()) {
          if (dest.get(dest.size() - 1).cardOntoCardPileSameColor(card)) {
            canMoveAny = true;
          }
        }
      }
    }
    return canMoveAny;
  }

  private boolean canMovePilesToPiles() {
    boolean canMoveAny = false;
    for (List<Card> cascade : this.cascadePiles) {
      if (this.canMovePilesToPile(cascade)) {
        canMoveAny = true;
      }
    }
    return canMoveAny;
  }

  private boolean canMovePilesToFoundations() {
    boolean canMoveAny = false;
    for (FoundationPile foundation : this.foundationPiles) {
      if (Utils.canMovePilesToFoundationWhiteHead(this.cascadePiles, foundation)) {
        canMoveAny = true;
      }
    }
    return canMoveAny;
  }





  @Override
  public int getPileHeight(int pileNum) throws IllegalStateException {

    gameNotStartedWarning();
    if (pileNum < 0 || pileNum >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid Pile Index.");
    } else {
      return this.cascadePiles.get(pileNum).size();
    }
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException {

    gameNotStartedWarning();
    if ((pileNum < 0 || pileNum >= this.cascadePiles.size())
            || (card < 0 || card >= this.getPileHeight(pileNum)
            || !this.isCardVisible(pileNum, card))) {
      throw new IllegalArgumentException(
              "Index of Pile or Card Row is Invalid, or Card is Invisible.");
    }
    if (this.isCardVisible(pileNum, card) && !this.cascadePiles.get(pileNum).isEmpty()) {
      return this.cascadePiles.get(pileNum).get(card);
    } else {
      return null;
    }
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalStateException {

    gameNotStartedWarning();
    if ((pileNum < 0 || pileNum >= this.cascadePiles.size())
            || (card < 0 || card >= this.getPileHeight(pileNum))) {
      throw new IllegalArgumentException("Index of Pile or Card Row is Invalid.");
    }
    else {
      List<Card> thisCascade = this.cascadePiles.get(pileNum);
      return card < thisCascade.size();
    }
  }

}
