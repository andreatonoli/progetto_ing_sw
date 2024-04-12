package model;

import view.TuiColors;

public enum Symbols {


    FUNGI(TuiColors.getColor(TuiColors.ANSI_RED) + "\uD83C\uDF44" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    PLANT(TuiColors.getColor(TuiColors.ANSI_GREEN) + "\uD83C\uDF3F" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    ANIMAL(TuiColors.getColor(TuiColors.ANSI_BLUE) + "\uD83D\uDC3A" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    INSECT(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "\uD83E\uDD8B" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    QUILL(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "\uD83E\uDEb6" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    INKWELL(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "\uD83E\uDD43" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    MANUSCRIPT(TuiColors.getColor(TuiColors.ANSI_YELLOW) + "\uD83D\uDCDC" + TuiColors.getColor(TuiColors.ANSI_RESET)),
    EMPTY("✔"),
    NOCORNER("ㅤ"),
    EMPTY_SPACE("ㅤ"),
    CORNER("\u2196");

    String string;
    Symbols(String string){ this.string = string; }

    public static String getString(Symbols symbol){ return symbol.string; }

}
