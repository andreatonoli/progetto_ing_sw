package observer;

import network.messages.Message;

import java.io.Serializable;

public interface Observer extends Serializable {
   void update(Message message);
}
