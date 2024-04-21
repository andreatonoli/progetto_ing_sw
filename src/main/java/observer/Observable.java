package observer;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import network.messages.Message;

public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }
    public void notifyAll(Message message) {
        for (Observer obs : observers) {
            obs.update(message);
        }
    }
    public void notify(Observer obs, Message message){
        obs.update(message);
    }
}
