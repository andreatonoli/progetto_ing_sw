package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.TuiColors;

public enum Symbols {

    FUNGI(TuiColors.getColor(TuiColors.ANSI_RED) + "F" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "F" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_RED) + "Fungi" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83C\uDF44"
    PLANT(TuiColors.getColor(TuiColors.ANSI_GREEN) + "P" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "P" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_GREEN) + "Plant" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83C\uDF3F"
    ANIMAL(TuiColors.getColor(TuiColors.ANSI_BLUE) + "A" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "A" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLUE) + "Animal" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83D\uDC3A"
    INSECT(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "I" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "I" + TuiColors.getColor(TuiColors.ANSI_RESET),TuiColors.getColor(TuiColors.ANSI_PURPLE) + "Insect" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83E\uDD8B"
    QUILL(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "Q" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "Q" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_YELLOW) + "Quill" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83E\uDEb6"
    INKWELL(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "K" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "K" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_YELLOW) + "Inkwell" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83E\uDD43"
    MANUSCRIPT(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "M" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "M" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_YELLOW) + "Manuscript" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83D\uDCDC"
    EMPTY(TuiColors.getColor(TuiColors.ANSI_WHITE) + "O" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "O" + TuiColors.getColor(TuiColors.ANSI_RESET), "Coverable corner"),
    //"✔"
    NOCORNER(" ", " ", " "),
    EMPTY_SPACE(" ", " ", " "),
    CORNER(TuiColors.getColor(TuiColors.ANSI_WHITE) + "C" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "C" + TuiColors.getColor(TuiColors.ANSI_RESET), "For each corner covered"),
    //"\u2196"
    CARD(TuiColors.getColor(TuiColors.ANSI_WHITE) + "D" + TuiColors.getColor(TuiColors.ANSI_RESET), TuiColors.getColor(TuiColors.ANSI_BLACK) + "D" + TuiColors.getColor(TuiColors.ANSI_RESET), "A generic card");
    //"\u2B1B"

    String string;
    String stringBlack;
    String longString;

    Symbols(String string, String stringBlack, String longString){
        this.string = string;
        this.stringBlack = stringBlack;
        this.longString = longString;
    }

    public static String getString(Symbols symbol){ return symbol.string; }

    public static String getStringBlack(Symbols symbol){ return symbol.stringBlack; }

    public static String getLongString(Symbols symbol){ return symbol.longString; }

}
