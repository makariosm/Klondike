package cs3500.klondike.model.hw02;

/**
 * This interface represents a general playing card that can be used to play Klondike.
 * This (essentially empty) interface marks the idea of cards.  You will need to
 * implement this interface in order to use your model.
 *
 * <p>The only behavior guaranteed by this class is its {@link Card#toString()} method,
 * which will render the card as specified in the assignment.
 *
 * <p>In particular, you <i>do not</i> know what implementation of this interface is
 * used by the Examplar wheats and chaffs, so your tests must be defined sufficiently
 * broadly that you do not rely on any particular constructors or methods of cards.
 */
public interface Card {

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   *
   * @return the formatted card
   */
  String toString();

  /**
   * Can the given card go onto the card in the pile?.
   * @param given the given Card checked against the card in the bottom of the pile
   * @return whether the card can be placed on the other card in a cascade
   */
  boolean cardOntoCardPile(BasicCard given);

  /**
   * Can the given card go onto the card in the foundation pile?.
   * @param given the given Card checked against the card in the bottom of the foundation
   * @return whether the card can be placed on the other card in a foundation
   */
  public boolean cardOntoCardFoundation(Card given);

  /**
   * Is this card's value equal to the value passed in?.
   * @param val the value passed in
   * @return whether the card's value is equal to the value passed in
   */
  public boolean isCardValue(int val);

  /**
   * Is suit passed in the card's suit?.
   * @param suits the suit passed in
   * @return whether the card's value is equal to the value passed in
   */
  boolean isSuit(BasicCard.Suits suits);

  /**
   * Gets the value of the card.
   * @return the integer value of the card
   */
  int getValue();

  /**
   * Is this card the same suit as that card?.
   * @param card the other card for which you are checking the suit of
   * @return true if they have the same suit, false otherwise
   */
  boolean isSameSuit(Card card);

  /**
   * Can the given card go onto the card in the pile in a game of whitehead Klondike?.
   * @param card the card checked against the card in the bottom of the pile
   * @return whether the card can be placed on the other card in a cascade
   */
  boolean cardOntoCardPileSameColor(Card card);
}
