package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.enums.Symbols;

import java.io.Serializable;

public class Corner implements Serializable {
    //Bisogna anche pensare a come gestire i "non angoli"
    /**
     * indicates the symbol (from the symbols enumeration) contained in the corner
     */
    private Symbols symbol;
    /**
     * indicate the state of the corner
     */
    private CornerState state;

    /**
     * Corner constructor
     * @param symbol is contained in the corner
     * corner's initial state set to visible
     */
    public Corner(Symbols symbol){
        this.symbol = symbol;
        if (symbol == Symbols.NOCORNER){
            this.state = CornerState.NOT_VISIBLE;
        }
        else{
            this.state = CornerState.VISIBLE;
        }
    }

    /**
     * getter to get the symbol value
     * @return the symbol contained in the corner
     */
    public Symbols getSymbol(){
        return this.symbol;
    }

    /**
     * setter to set the symbol value
     * @param symbol is the symbol to set in the corner
     */
    public void setSymbol(Symbols symbol){
        this.symbol = symbol;
    }

    /**
     * getter to get the state of the corner
     * @return the state the corner is currently in
     */
    public CornerState getState(){
        return this.state;
    }

    /**
     * setter to set the state value
     * @param state is the state the corner is currently in
     */
    public void setState(CornerState state){
        this.state = state;
    }
}
