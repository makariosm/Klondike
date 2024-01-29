package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The abstract class that represents a general game of Klondike.
 */
public abstract class AbstractKlondike implements KlondikeModel {

  protected List<CascadePile> cascades;
  protected List<FoundationPile> foundationPiles;
  protected DrawPile drawPile;
  protected boolean isGameOver;
  protected List<Card> deck;
  protected final Random rand;


  /**
   * Constructs a general klondike game.
   */
  public AbstractKlondike() {
    this.foundationPiles = new ArrayList<>();
    this.drawPile = new DrawPile(new ArrayList<>(), new ArrayList<>());
    this.isGameOver = true;
    this.deck = this.getDeck();
    this.rand = new Random();
    this.cascades = new ArrayList<>();
  }


  /**
   * Constructs a general klondike game, but with a specific seed to predictably shuffle cards.
   * @param randSeed the random seed that helps make shuffling the cards predictable
   */
  public AbstractKlondike(int randSeed) {
    this.foundationPiles = new ArrayList<>();
    this.drawPile = new DrawPile(new ArrayList<>(), new ArrayList<>());
    this.isGameOver = true;
    this.deck = this.getDeck();
    this.rand = new Random(randSeed);
    this.cascades = new ArrayList<>();
  }

  /**
   * Return a valid and complete deck of cards for a game of Klondike.
   * There is no restriction imposed on the ordering of these cards in the deck.
   * The validity of the deck is determined by the rules of the specific game in
   * the classes implementing this interface.  This method may be called as often
   * as desired.
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<Card> getDeck() {
    List<Card> newDeck = new ArrayList<>();
    for (int i = 1; i < 14; i++) {
      newDeck.add(new BasicCard(i, BasicCard.Suits.Hearts));
      newDeck.add(new BasicCard(i, BasicCard.Suits.Diamonds));
      newDeck.add(new BasicCard(i, BasicCard.Suits.Clubs));
      newDeck.add(new BasicCard(i, BasicCard.Suits.Spades));
    }
    return newDeck;
  }

  /**
   * <p>Deal a new game of Klondike.
   * The cards to be used and their order are specified by the the given deck,
   * unless the {@code shuffle} parameter indicates the order should be ignored.</p>
   *
   * <p>This method first verifies that the deck is valid. It deals cards in rows
   * (left-to-right, top-to-bottom) into the characteristic cascade shape
   * with the specified number of rows, followed by (at most) the specified number of
   * draw cards. When {@code shuffle} is {@code false}, the {@code deck} must be used in
   * order and the 0th card in {@code deck} is used as the first card dealt.
   * There will be as many foundation piles as there are Aces in the deck.</p>
   *
   * <p>A valid deck must consist cards that can be grouped into equal-length,
   * consecutive runs of cards (each one starting at an Ace, and each of a single
   * suit).</p>
   *
   * <p>This method should have no side effects other than configuring this model
   * instance, and should work for any valid arguments.</p>
   *
   * @param deck     the deck to be dealt
   * @param shuffle  if {@code false}, use the order as given by {@code deck},
   *                 otherwise use a randomly shuffled order
   * @param numPiles number of piles to be dealt
   * @param numDraw  maximum number of draw cards available at a time
   * @throws IllegalStateException    if the game has already started
   * @throws IllegalArgumentException if the deck is null or invalid,
   *                                  a full cascade cannot be dealt with the given sizes,
   *                                  or another input is invalid
   */
  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {

    Utils.startGameExceptions(deck, this.isGameOver, numPiles, numDraw);

    //Check if there are enough cards for the cascade piles and foundation piles.
    int numAces = 0;
    for (Card card : deck) {
      if (card.isCardValue(1)) {
        numAces++;
      }
    }

    this.isGameOver = false;
    this.deck = new ArrayList<>(deck);
    this.foundationPiles = new ArrayList<>(numAces);

    //shuffle if needed

    if (shuffle) {
      Collections.shuffle(this.deck, rand);
    }

    foundationPiles = new ArrayList<>(numAces);
    for (Card c : this.deck) {
      if (c.isCardValue(1)) {
        foundationPiles.add(new FoundationPile(new ArrayList<>()));
      }
    }

    this.cascades = new ArrayList<>(numPiles);
    this.drawPile = new DrawPile(new ArrayList<>(), new ArrayList<>());

    for (int i = 0; i < numPiles; i++) {
      this.cascades.add(new CascadePile(new ArrayList<>(), new ArrayList<>()));
    }

    //Deal all cards into proper cascade piles
    Utils.cascadesMaker(this.cascades, this.deck);

    //Set up draw pile
    Utils.drawPileMaker(this.deck, this.drawPile, numDraw);
    this.deck = deck;
  }

  /**
   * Moves the requested number of cards from the source pile to the destination pile,
   * if allowable by the rules of the game.
   *
   * @param srcPile  the 0-based index (from the left) of the pile to be moved
   * @param numCards how many cards to be moved from that pile
   * @param destPile the 0-based index (from the left) of the destination pile for the
   *                 moved cards
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid, if the pile
   *                                  numbers are the same, or there are not enough cards to move
   *                                  from the srcPile to the destPile (i.e. the move is not
   *                                  physically possible)
   * @throws IllegalStateException    if the move is not allowable (i.e. the move is not
   *                                  logically possible)
   */
  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
    gameNotStartedWarning();
    if ((srcPile < 0 || srcPile >= cascades.size())
            || (destPile < 0 || destPile >= cascades.size())
            || (destPile == srcPile)) {
      throw new IllegalArgumentException("Source Pile or Destination Pile Index is Wrong");
    } else {
      this.cascades.get(srcPile).movePile(numCards, this.cascades.get(destPile),
              this.deck.size(), this.getNumFoundations());
    }
  }

  /**
   * Moves the topmost draw-card to the destination pile.  If no draw cards remain,
   * reveal the next available draw cards
   *
   * @param destPile the 0-based index (from the left) of the destination pile for the
   *                 card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if destination pile number is invalid
   * @throws IllegalStateException    if there are no draw cards, or if the move is not
   *                                  allowable
   */
  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    gameNotStartedWarning();
    if (destPile < 0 || destPile >= cascades.size()) {
      throw new IllegalArgumentException("Destination Pile Index is Wrong");
    } else if (this.drawPile.visible.isEmpty()) {
      throw new IllegalStateException("No draw cards");
    } else {
      this.drawPile.movePile(1, this.cascades.get(destPile),
              this.deck.size(), this.getNumFoundations());
      if (!this.drawPile.invisible.isEmpty()) {
        this.drawPile.visible.add(
                this.drawPile.visible.size() - 1, this.drawPile.invisible.remove(0));
      }
    }
  }

  /**
   * Moves the top card of the given pile to the requested foundation pile.
   *
   * @param srcPile        the 0-based index (from the left) of the pile to move a card
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                       place the card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid
   * @throws IllegalStateException    if the source pile is empty or if the move is not
   *                                  allowable
   */
  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalStateException {
    gameNotStartedWarning();
    if ((srcPile < 0 || srcPile >= cascades.size())
            || (foundationPile < 0 || foundationPile >= foundationPiles.size())) {
      throw new IllegalArgumentException("Source Pile or Foundation Pile Index is Wrong");
    } else {
      this.cascades.get(srcPile).moveToFoundation(this.foundationPiles.get(foundationPile),
              this.cascades.get(srcPile).invisible, this.cascades.get(srcPile).visible);
    }
  }

  /**
   * Moves the topmost draw-card directly to a foundation pile.
   *
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                       place the card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   * @throws IllegalStateException    if there are no draw cards or if the move is not
   *                                  allowable
   */

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    gameNotStartedWarning();
    if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
      throw new IllegalArgumentException("Foundation Pile Index is Wrong.");
    } else {
      this.drawPile.moveToFoundation(this.foundationPiles.get(foundationPile),
              this.drawPile.visible);
      if (!this.drawPile.invisible.isEmpty()) {
        this.drawPile.visible.add(
                this.drawPile.invisible.remove(0));
      }
    }
  }

  /**
   * Discards the topmost draw-card.
   *
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalStateException if move is not allowable
   */
  @Override
  public void discardDraw() {
    gameNotStartedWarning();
    this.drawPile.discardAndRecycle();
  }

  /**
   * Checks if the game has not started yet and throws an exception if it has.
   *
   * @throws IllegalStateException if the game has already started
   */
  private void gameNotStartedWarning() {
    if (this.isGameOver) {
      throw new IllegalStateException("Game not started yet.");
    }
  }

  private boolean canMovePilesToPiles() {
    boolean canMoveAny = false;
    for (CascadePile cascade : this.cascades) {
      if (cascade.canMovePilesToPile(this.cascades, cascade)) {
        canMoveAny = true;
      }
    }
    return canMoveAny;
  }

  private boolean canMovePilesToFoundations() {
    boolean canMoveAny = false;
    for (FoundationPile foundation : this.foundationPiles) {
      if (foundation.canMovePilesToFoundation(this.cascades, foundation)) {
        canMoveAny = true;
      }
    }
    return canMoveAny;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getNumRows() {
    gameNotStartedWarning();
    int biggestRow = 0;
    for (CascadePile cascade : cascades) {
      if (cascade.visible.size() + cascade.invisible.size() > biggestRow) {
        biggestRow = cascade.visible.size() + cascade.invisible.size();
      }
    }
    return biggestRow;
  }

  /**
   * Returns the number of piles for this game.
   *
   * @return the number of piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumPiles() {
    gameNotStartedWarning();
    return this.cascades.size();
  }


  /**
   * Signal if the game is over or not.  A game is over if there are no more
   * possible moves to be made, or draw cards to be used (or discarded).
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public boolean isGameOver() throws IllegalStateException {
    gameNotStartedWarning();
    if (this.getDrawCards().isEmpty() && !(this.canMovePilesToPiles()
            || this.canMovePilesToFoundations())) {
      this.isGameOver = true;
    }
    return this.isGameOver;
  }


  /**
   * Returns the number of cards in the specified pile.
   *
   * @param pileNum the 0-based index (from the left) of the pile
   * @return the number of cards in the specified pile
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if pile number is invalid
   */
  public int getPileHeight(int pileNum) {
    gameNotStartedWarning();
    if (pileNum < 0 || pileNum >= this.cascades.size()) {
      throw new IllegalArgumentException("Invalid Pile Index.");
    } else {
      return this.cascades.get(pileNum).visible.size()
              + this.cascades.get(pileNum).invisible.size();
    }
  }

  /**
   * Returns whether the card at the specified coordinates is face-up or not.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return whether the card at the given position is face-up or not
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  public boolean isCardVisible(int pileNum, int card) {
    gameNotStartedWarning();
    if ((pileNum < 0 || pileNum >= this.cascades.size())
            || (card < 0 || card >= this.getPileHeight(pileNum))) {
      throw new IllegalArgumentException("Index of Pile or Card Row is Invalid.");
    } else {
      CascadePile thisCascade = this.cascades.get(pileNum);
      return card > thisCascade.invisible.size() - 1;
    }
  }

  /**
   * Returns the maximum number of visible cards in the draw pile.
   *
   * @return the number of visible cards in the draw pile
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumDraw() {
    gameNotStartedWarning();
    return this.drawPile.visible.size();
  }


  /**
   * Return the current score, which is the sum of the values of the topmost cards
   * in the foundation piles.
   *
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getScore() {
    gameNotStartedWarning();
    int score = 0;
    for (FoundationPile foundation : foundationPiles) {
      score += foundation.visible.size();
    }
    return score;
  }

  /**
   * Returns the card at the specified coordinates, if it is visible.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public Card getCardAt(int pileNum, int card) {
    gameNotStartedWarning();
    if ((pileNum < 0 || pileNum >= this.cascades.size())
            || (card < 0 || card >= this.getPileHeight(pileNum)
            || !this.isCardVisible(pileNum, card))) {
      throw new IllegalArgumentException(
              "Index of Pile or Card Row is Invalid, or Card is Invisible.");
    }
    if (this.isCardVisible(pileNum, card) && !this.cascades.get(pileNum).visible.isEmpty()) {
      return this.cascades.get(pileNum).visible.get(
              card - this.cascades.get(pileNum).invisible.size());
    } else {
      return null;
    }
  }

  /**
   * Returns the card at the top of the specified foundation pile.
   *
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   */
  @Override
  public Card getCardAt(int foundationPile) throws IllegalStateException {
    gameNotStartedWarning();
    if (foundationPile < 0 || foundationPile >= this.foundationPiles.size()) {
      throw new IllegalArgumentException("Index of Foundation Pile is Invalid.");
    }
    List<Card> thisFoundationVis = this.foundationPiles.get(foundationPile).visible;
    if (thisFoundationVis.isEmpty()) {
      return null;
    } else {
      return thisFoundationVis.get(thisFoundationVis.size() - 1);
    }
  }

  /**
   * Returns the currently available draw cards.
   * There should be at most {@link KlondikeModel#getNumDraw} cards (the number
   * specified when the game started) -- there may be fewer, if cards have been removed.
   * NOTE: Users of this method should not modify the resulting list.
   *
   * @return the ordered list of available draw cards (i.e. first element of this list
   *         is the first one to be drawn)
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    gameNotStartedWarning();
    return new ArrayList<>(drawPile.visible);
  }

  /**
   * Return the number of foundation piles in this game.
   *
   * @return the number of foundation piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumFoundations() throws IllegalStateException {
    gameNotStartedWarning();
    return foundationPiles.size();
  }
}
