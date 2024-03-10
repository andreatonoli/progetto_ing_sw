package model;

import java.util.List;

public abstract class Card implements ICard {
   List<Corner> corners;
    /** Card's ID is composed of its type and its card_number value */
    String type;
   int card_number;
}
