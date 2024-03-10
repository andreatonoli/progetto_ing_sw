package model;

import java.util.List;

public abstract class Card implements ICard {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type;
    protected int card_number;
}
