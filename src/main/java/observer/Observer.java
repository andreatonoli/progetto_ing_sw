package observer;

import network.messages.Message;

import java.io.Serializable;

public interface Observer {
   void update(Message message);
}
