package it.polimi.ingsw;

import it.polimi.ingsw.view.Tui;

import java.util.Scanner;

public class ClientLauncher {
    public static void main (String[] args){
        System.out.println("Premi [0] per giocare con la TUI.\nPremi [1] per giocare con la GUI");
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
