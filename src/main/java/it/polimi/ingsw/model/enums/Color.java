package it.polimi.ingsw.model.enums;

public enum Color {
    //RED(1, "\u001B[41m" + "  " + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //GREEN(3, "\u001B[42m" + "  " + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //BLUE(0, "\u001B[44m" + "  " + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //PURPLE(2, "\u001B[45m" + "  " + TuiColors.getColor(TuiColors.ANSI_RESET)),
    //WHITE(4, "\u001B[47m" + "  " + TuiColors.getColor(TuiColors.ANSI_RESET));
    RED(1, "\u001B[0;101m"),
    GREEN(3, "\u001B[0;102m"),
    BLUE(0, "\u001B[0;104m"),
    PURPLE(2, "\u001B[0;105m"),
    WHITE(4, "\u001B[48;5;252m"),
    YELLOW(5, "\u001B[43m"),
    ORANGE(6, "\u001B[48;5;208m");
    Integer associatedIndex;
    String background;

    Color(Integer associatedIndex, String background){
        this.associatedIndex = associatedIndex;
        this.background = background;
    }

    public static Color getAssociatedColor(Color color){
        for (Color c : Color.values()){
            if (color.associatedIndex == c.ordinal()){
                return c;
            }
        }
        return Color.WHITE;
    }

    public static String getBackground(Color color){ return color.background; }
}
