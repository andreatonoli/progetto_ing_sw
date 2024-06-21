package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.enums.Symbols;

import java.io.Serializable;

public class Corner implements Serializable {

    /**
     * Indicates the symbol contained in the corner.
     */
    private final Symbols symbol;

    /**
     * Indicate the state of the corner.
     */
    private CornerState state;

    /**
     * Builds the corner, initializing the symbol and the state.
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
     * Getter of the symbol inside the corner.
     * @return the symbol contained in the corner.
     */
    public Symbols getSymbol(){
        return this.symbol;
    }

    /**
     * Getter of the corner state.
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
