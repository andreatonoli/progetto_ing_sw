package view;

public enum TuiColors {
    ANSI_RESET("\u001B[0m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_BLACK("\u001B[30m");

    String color;

    TuiColors(String color){ this.color = color; }

    public static String getColor(TuiColors tuiColor){ return tuiColor.color; }
}
