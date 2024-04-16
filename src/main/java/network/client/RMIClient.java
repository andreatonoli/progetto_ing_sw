package network.client;

import java.util.*;

import Controller.Controller;
import network.messages.Message;
import network.server.VirtualServer;
import view.Ui;
import model.Game;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientHandler{
    private String username;
    private Ui view;
    private VirtualServer server;

    public RMIClient(String username, String host, int port, Ui view) throws RemoteException{
        this.username = username;
        this.view = view;
        final String serverName = "GameServer";
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (VirtualServer) registry.lookup(serverName);
            while (server.usernameTaken(this.username)){
                System.out.println("Username is already taken, please choose another: ");
                this.username = view.askNickname();
            }
            server.login(this, this.username);
        } catch (RemoteException | NotBoundException e){
            System.out.println(e.getMessage());
        }
    }

    public int joinGame(List<Controller> startingGames) throws RemoteException{
        return this.view.selectGame(startingGames);
    }

    public int setLobbySize() throws RemoteException{
        return this.view.setLobbySize();
    }

    /**
     * gets the message from the server and updates the view according to the message type
     * @param message sent from the server
     * @throws RemoteException
     */
    @Override
    public void getMessage(Message message) throws RemoteException {
        switch (message.getType()){
            case GENERIC_MESSAGE:
                this.view.showText(message.toString());
                break;
            default:
                break;
        }
    }
}
