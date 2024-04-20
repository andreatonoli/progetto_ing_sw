package Controller;

import model.Game;
import network.messages.Message;
import network.server.Connection;
import observer.Observer;
import view.Ui;

public class ClientController implements Observer {
    private Ui view;
    private String username;
    private Connection connection;
    private Game game; //IDK

    public ClientController(Ui view, String username){
        this.view = view;
        this.username = username;
    }
    /*
        Client -> sendMessage(view) -> Conenction -> login -> newClientController(view)
        ClientController -> join(Controller)
        Controller -> getGame() -> Observable -> add((Observer)ClientController) ->
        drawCard() -> notifyAll(PORCODDUE HO PESCATO)
     */
    @Override
    public void update(Message message) {

    }
}
