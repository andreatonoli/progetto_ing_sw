package model;

import view.TuiColors;

public enum Symbols {

    FUNGI(TuiColors.getColor(TuiColors.ANSI_RED) + "F" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83C\uDF44"
    PLANT(TuiColors.getColor(TuiColors.ANSI_GREEN) + "P" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83C\uDF3F"
    ANIMAL(TuiColors.getColor(TuiColors.ANSI_BLUE) + "A" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83D\uDC3A"
    INSECT(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "I" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83E\uDD8B"
    QUILL(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "Q" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83E\uDEb6"
    INKWELL(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "K" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83E\uDD43"
    MANUSCRIPT(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "M" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //"\uD83D\uDCDC"
    EMPTY("O"),
    //"✔"
    NOCORNER(" "),
    EMPTY_SPACE(" "),
    CORNER("C"),
    //"\u2196"
    CARD("D");
    //"\u2B1B"
    String string;
    Symbols(String string){ this.string = string; }

    public static String getString(Symbols symbol){ return symbol.string; }

}
