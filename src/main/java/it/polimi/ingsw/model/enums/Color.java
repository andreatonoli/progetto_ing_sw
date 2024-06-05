package it.polimi.ingsw.model.enums;

public enum Color {
    RED(1, "\033[0;101m"),
    GREEN(3, "\033[0;102m"),
    BLUE(0, "\033[0;104m"),
    PURPLE(2, "\033[0;105m"),
    WHITE(4, "\033[48;5;252m"),
    YELLOW(5, "\033[43m"),
    ORANGE(6, "\033[48;5;208m");
    private final Integer associatedIndex;
    private final String background;

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
