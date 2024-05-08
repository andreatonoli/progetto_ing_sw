package it.polimi.ingsw.network.client;

import java.util.Scanner;

import it.polimi.ingsw.view.Tui;

public class Client{
    public Client(){
        System.out.println("Premi [0] per giocare con la TUI.\nPremi un qualsiasi altro numero per giocare con la GUI");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        if (choice == 0){
            new Tui().run();
        }
        else{
            new Tui().run(); //TODO: mettere la gui
        }
    }
}
