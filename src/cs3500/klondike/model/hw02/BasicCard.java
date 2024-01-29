package cs3500.klondike.model.hw02;

import java.util.Objects;

/**
 * This class represents a basic playing card.
 */
public class BasicCard implements Card {

  private final Integer value;
  private final Suits suit;

  /**
   * Enumeration for card suits.
   */
  public enum Suits {
    Clubs('♣'), Spades('♠'), Hearts('♡'), Diamonds('♢');

    private final char suitVal;

    Suits(char suitVal) {
      this.suitVal = suitVal;
    }

    /**
     * Get the suit symbol.
     *
     * @return the symbol representing the suit
     */
    private String getSuit() {
      return String.valueOf(this.suitVal);
    }
  }

  /**
   * Constructs a BasicCard with a specified value and suit.
   *
   * @param value the value of the card (1-13)
   * @param suit  the suit of the card
   * @throws IllegalArgumentException if the value is not in the range [1, 13]
   */
  public BasicCard(Integer value, Suits suit) {
    if (this.validate(value)) {
      this.value = value;
      this.suit = suit;
    } else {
      throw new IllegalArgumentException("Value cannot be lower than 1 or higher than 13");
    }
  }

  /**
   * Validates that a card value is within the valid range [1, 13].
   *
   * @param value the card value to validate
   * @return true if the value is valid, false otherwise
   */
  private boolean validate(int value) {
    return value >= 1 && value <= 13;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    String val = "";
    switch (this.value) {
      case 1:
        val = "A";
        break;
      case 11:
        val = "J";
        break;
      case 12:
        val = "Q";
        break;
      case 13:
        val = "K";
        break;
      default:
        val = this.value.toString();
    }
    return val + this.suit.getSuit();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof BasicCard) {
      BasicCard card = (BasicCard) other;
      return this.toString().equals(card.toString());
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash();
  }

  /**
   * Checks if this card can be placed onto another card in a pile.
   *
   * @param given the card to place this card onto
   * @return true if the placement is valid, false otherwise
   */
  public boolean cardOntoCardPile(BasicCard given) {
    switch (this.suit) {
      case Clubs:
      case Spades:
        return (given.suit == Suits.Hearts || given.suit == Suits.Diamonds)
                && given.value.equals(this.value - 1);
      case Hearts:
      case Diamonds:
        return (given.suit == Suits.Clubs || given.suit == Suits.Spades)
                && given.value.equals(this.value - 1);
      default: return false;
    }
  }

  /**
   * Checks if this card can be placed onto another card in a foundation pile.
   *
   * @param given the card to place this card onto
   * @return true if the placement is valid, false otherwise
   */
  public boolean cardOntoCardFoundation(Card given) {
    switch (this.suit) {
      case Clubs:
        return given.isSuit(Suits.Clubs) && given.getValue() == this.value + 1;
      case Spades:
        return given.isSuit(Suits.Spades) && given.getValue() == this.value + 1;
      case Hearts:
        return given.isSuit(Suits.Hearts) && given.getValue() == this.value + 1;
      case Diamonds:
        return given.isSuit(Suits.Diamonds) && given.getValue() == this.value + 1;
      default:
        return false;
    }
  }

  /**
   * Checks if this card has a specific value.
   *
   * @param val the value to check
   * @return true if this card has the specified value, false otherwise
   */
  public boolean isCardValue(int val) {
    return this.value == val;
  }

  /**
   * Checks if this card has a specific suit.
   *
   * @param suit the suit to check
   * @return true if this card has the specified suit, false otherwise
   */
  public boolean isSuit(Suits suit) {
    return this.suit == suit;
  }

  /**
   * {@inheritDoc}
   */
  public int getValue() {
    return this.value;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isSameSuit(Card card) {
    return card.isSuit(this.suit);
  }


  /**
   * {@inheritDoc}
   */
  public boolean cardOntoCardPileSameColor(Card card) {
    switch (this.suit) {
      case Clubs:
      case Spades:
        return (card.isSuit(Suits.Clubs) || card.isSuit(Suits.Spades))
                && card.getValue() ==  this.value - 1;
      case Hearts:
      case Diamonds:
        return (card.isSuit(Suits.Hearts) || card.isSuit(Suits.Diamonds))
                && card.getValue() == this.value - 1;
      default: return false;
    }
  }
}

