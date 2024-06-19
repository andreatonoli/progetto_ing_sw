package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.enums.Symbols;

import java.io.Serializable;

public class Corner implements Serializable {

    /**
     * indicates the symbol contained in the corner.
     */
    private final Symbols symbol;

    /**
     * indicate the state of the corner.
     */
    private CornerState state;

    /**
     * Corner constructor. If the symbol is a NOCORNER it sets the state to NOT_VISIBLE to hide the corner. In every other
     * case the state is VISIBLE.
     * @param symbol symbol contained in the corner.
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
     * getter of the symbol inside the corner.
     * @return the symbol contained in the corner.
     */
    public Symbols getSymbol(){
        return this.symbol;
    }

    /**
     * getter of the corner state.
     * @return the current state of the corner.
     */
    public CornerState getState(){
        return this.state;
    }

    /**
     * Sets the state of the corner.
     * @param state current state of the corner.
     */
    public void setState(CornerState state){
        this.state = state;
    }
}
