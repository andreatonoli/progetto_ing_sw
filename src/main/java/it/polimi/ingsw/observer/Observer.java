package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Observer interface represents the observer of the observer pattern.
 */
public interface Observer {

   /**
    * Method used to update the observer.
    * @param message is the message to be sent.
    */
   void update(Message message);
}
