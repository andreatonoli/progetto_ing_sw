package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.tui.TuiColors;

/**
 * Symbols enumeration used to indicate the card's symbol.
 */
public enum Symbols {

    FUNGI(TuiColors.getColor(TuiColors.ANSI_RED) + "F" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_RED) + "Fungi" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    PLANT(TuiColors.getColor(TuiColors.ANSI_GREEN) + "P" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GREEN) + "Plant" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    ANIMAL(TuiColors.getColor(TuiColors.ANSI_BLUE) + "A" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLUE) + "Animal" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    INSECT(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "I" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_PURPLE) + "Insect" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    QUILL(TuiColors.getColor(TuiColors.ANSI_GOLD) + "Q" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GOLD) + "Quill" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    INKWELL(TuiColors.getColor(TuiColors.ANSI_GOLD) + "K" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GOLD) + "Inkwell" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    MANUSCRIPT(TuiColors.getColor(TuiColors.ANSI_GOLD) + "M" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GOLD) + "Manuscript" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    /**
     * Represents a corner without symbols in it.
     */
    EMPTY(TuiColors.getColor(TuiColors.ANSI_GRAY) + " " + TuiColors.getColor(TuiColors.ANSI_RESET), "Coverable corner"),

    /**
     * Represents a corner of the card where the user cannot place another card over.
     */
    NOCORNER(" ", " "),

    /**
     * Indicates the Corner condition on a gold card.
     */
    CORNER(TuiColors.getColor(TuiColors.ANSI_WHITE) + "C" + TuiColors.getColor(TuiColors.ANSI_RESET), "For each corner covered"),

    /**
     * Indicates a generic card that can be used to complete an objective.
     */
    CARD(TuiColors.getColor(TuiColors.ANSI_WHITE) + " " + TuiColors.getColor(TuiColors.ANSI_RESET), "A generic card");

    private final String string;
    private final String nameString;

    /**
     * Constructor of the Symbols enumeration.
     * @param string short string to show the symbol on the cards.
     * @param longString string that better describes the symbol to the user.
     */
    Symbols(String string, String longString){
        this.string = string;
        this.nameString = longString;
    }

    public static String getString(Symbols symbol){ return symbol.string; }

    public static String getNameString(Symbols symbol){ return symbol.nameString; }

}
