package view;

import model.Game;
import network.client.*;
import network.server.Server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
//TODO: Mergeare TUI e TUIHandler
public class TuiHandler implements Ui{
    Scanner scanner;
    PrintStream out;
    public TuiHandler(){
        scanner = new Scanner(System.in);
        out = System.out;
    }

    /**
     * Starts the TUI instance and begins the login phase
     */
    public void run(){
        try {
            //TODO: Stampa del titolo
            String choice;
            String username = askNickname();
            out.println("Please specify the following settings. The default value is shown between brackets.");
            String address = askServerAddress();
            out.println("Choose your connection method: Write:\n\tI)RMI\n\tII)Socket");
            choice = scanner.nextLine();
            while(!choice.equalsIgnoreCase("RMI") && !choice.equalsIgnoreCase("Socket")){
                out.println("Wrong input.\nPlease, choose your connection method. Write:\n\tRMI\n\tSocket");
                choice = scanner.nextLine();
            }
            int port = askServerPort(choice);
            if (choice.equalsIgnoreCase("RMI")){
                //probabilmente va salvata l'istanza di client
                new RMIClient(username, address, port, this);
            }
            else{
                new SocketClient(username, address, port, this);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Asks the player's nickname
     * @return the name chosen by the player
     */
    @Override
    public String askNickname(){
        String nickname;
        System.out.print("Please insert your username: ");
        nickname = scanner.nextLine();
        return nickname;
    }

    /**
     * Asks the server address the player wants to connect to
     * @return the chosen server address
     */
    @Override
    public String askServerAddress() {
        String defaultAddress = "localhost";
        String host;
        System.out.print("Enter the server address [" + defaultAddress + "]: ");
        String address = scanner.nextLine();
        if (address.isEmpty()) {
            host = defaultAddress;
        } else {
            host = address;
        }
        return host;
    }

    /**
     * Asks the player the port of the server he wants to connect to
     * @return the chosen server port
     */
    @Override
    public int askServerPort(String connection) {
        String port;
        String defaultPort;
        if (connection.equalsIgnoreCase("RMI")){
            defaultPort = String.valueOf(Server.rmiPort);
        }
        else{
            defaultPort = String.valueOf(Server.socketPort);
        }
        System.out.print("Enter the server port [" + defaultPort + "]: ");
        port = scanner.nextLine();
        if (port.isEmpty()) {
            port = defaultPort;
        }
        return Integer.parseInt(port);
    }
    @Override
    public int selectGame(List<Game> startingGames){
        System.out.println("select one of the following game's lobby by writing the respective number");
        int i;
        for (i = 0; i < startingGames.size(); i++){
            System.out.println("lobby " + i);
        }
        System.out.println("lobby " + (i + 1) + "(create new game)");
        return scanner.nextInt();
    }
}