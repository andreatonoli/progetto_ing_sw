package model;

import java.util.ArrayList;
import java.io.Serializable;

public interface Achievement extends Serializable {
    void calcPoints(Player player);
    int getPoints();
    Color getColor();

    ArrayList<Symbols> getSymbols();
    Symbols getSymbol();

}
