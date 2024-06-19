package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.tui.TuiColors;

public enum Symbols {

    FUNGI(TuiColors.getColor(TuiColors.ANSI_RED) + "F" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "F" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_RED) + "Fungi" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    PLANT(TuiColors.getColor(TuiColors.ANSI_GREEN) + "P" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "P" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GREEN) + "Plant" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    ANIMAL(TuiColors.getColor(TuiColors.ANSI_BLUE) + "A" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "A" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLUE) + "Animal" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    INSECT(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "I" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "I" + TuiColors.getColor(TuiColors.ANSI_RESET),TuiColors.getColor(TuiColors.ANSI_PURPLE) + "Insect" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    QUILL(TuiColors.getColor(TuiColors.ANSI_GOLD) + "Q" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "Q" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GOLD) + "Quill" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    INKWELL(TuiColors.getColor(TuiColors.ANSI_GOLD) + "K" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "K" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GOLD) + "Inkwell" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    MANUSCRIPT(TuiColors.getColor(TuiColors.ANSI_GOLD) + "M" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "M" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GOLD) + "Manuscript" + TuiColors.getColor(TuiColors.ANSI_RESET)),

    EMPTY(TuiColors.getColor(TuiColors.ANSI_WHITE) + "O" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "O" + TuiColors.getColor(TuiColors.ANSI_RESET), "Coverable corner"),

    NOCORNER(" ", " ", " "),
    EMPTY_SPACE(" ", " ", " "),
    CORNER(TuiColors.getColor(TuiColors.ANSI_WHITE) + "C" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "C" + TuiColors.getColor(TuiColors.ANSI_RESET), "For each corner covered"),
    CARD(TuiColors.getColor(TuiColors.ANSI_WHITE) + "D" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "D" + TuiColors.getColor(TuiColors.ANSI_RESET), "A generic card");

    private final String string;
    private final String stringBlack;
    private final String nameString;

    Symbols(String string, String stringBlack, String longString){
        this.string = string;
        this.stringBlack = stringBlack;
        this.nameString = longString;
    }

    public static String getString(Symbols symbol){ return symbol.string; }

    public static String getStringBlack(Symbols symbol){ return symbol.stringBlack; }

    public static String getNameString(Symbols symbol){ return symbol.nameString; }

}
