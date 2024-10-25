package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.network.messages.Message;

/**
 * Observable class represents the observable object of the observer pattern.
 */
public class Observable {

    /** list of observers */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Method used to add an observer to the list.
     * @param obs is the observer to be added.
     */
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    /**
     * Method used to remove an observer from the list.
     * @param obs is the observer to be removed.
     */
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /**
     * Method used to notify all the observers of the changes happened on the model.
     * @param message is the message to be sent.
     */
    public void notifyAll(Message message) {
        for (Observer obs : observers) {
            obs.update(message);
        }
    }
}
