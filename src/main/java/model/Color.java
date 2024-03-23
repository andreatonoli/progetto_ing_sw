package model;
public enum Color {
    RED,
    GREEN,
    BLUE,
    PURPLE,
    WHITE;
    Color associatedColor;
    Color(){
        setAssociatedColor();
    }
    private void setAssociatedColor(){
        RED.associatedColor = GREEN;
        GREEN.associatedColor = PURPLE;
        BLUE.associatedColor = RED;
        PURPLE.associatedColor = BLUE;
    }
    public Color getAssociatedColor(Color color){
        return color.associatedColor;
    }
}
