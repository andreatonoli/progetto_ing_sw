package model;

import java.util.ArrayList;

public interface Achievement {
    void calcPoints(Player player);
    int getPoints();
    Color getColor();

    ArrayList<Symbols> getSymbols();
    Symbols getSymbol();

}
