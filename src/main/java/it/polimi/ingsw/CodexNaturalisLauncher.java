package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.Tui;

import java.util.Scanner;

public class CodexNaturalisLauncher {
    public static void main(String[] args){
        if (args.length != 0){
            if (args[0].equals("0")){
                new Server();
            }
            else {
                new Client();
            }
        }
        else{
            System.out.println("Press [0] to start a server\nPress any other number to start a Client");
            Tui.clearConsole();
            Scanner scan = new Scanner(System.in);
            int choice = scan.nextInt();
            if (choice == 0){
                new Server();
            }
            else{
                new Client();
            }
        }
    }
}
