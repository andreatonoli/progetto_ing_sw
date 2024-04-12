package view;

import network.client.*;
import java.io.PrintStream;
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
        //TODO: Stampa del titolo
        String choice;
        String username = askNickname();
        out.println("Please specify the following settings. The default value is shown between brackets.");
        String address = askServerAddress();
        int port = askServerPort();
        out.println("Please, choose your connection method:\n\tRMI\n\tSocket");
        choice = scanner.nextLine();
        while(!choice.equalsIgnoreCase("RMI") && !choice.equalsIgnoreCase("Socket")){
            out.println("Wrong input.\nPlease, choose your connection method. Press:\n\tRMI\n\tSocket");
            choice = scanner.nextLine();
        }
        if (choice.equalsIgnoreCase("RMI")){
            //probabilmente va salvata l'istanza di client
            new RMIClient(username, address, port, this);
        }
        else{
            new SocketClient();
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
        String defaultAddress = "LocalHost";
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
    public int askServerPort() {
        String port;
        String defaultPort = "1234";
        System.out.print("Enter the server port [" + defaultPort + "]: ");
        port = scanner.nextLine();
        if (port.isEmpty()) {
            port = defaultPort;
        }
        return Integer.parseInt(port);
    }
}