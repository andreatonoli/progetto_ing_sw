package model;

public interface ICard {

    //come facciamo a sapere che lato della carta ritornare?
    public default Card getSide(Card card) {

    }

    public default void setSide(Card card) {

    }

    public default Card flipSide(Card card) {

    }

    public default void placeCard(Card card) {

    }

    public default int getPoint(Card card) {

    }
}
