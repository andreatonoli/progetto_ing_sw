package it.polimi.ingsw;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.Tui;

import java.util.Scanner;

public class ClientLauncher {
    public static void main (String[] args){
        Tui.clearConsole();
        System.out.println("Press[0] to play the game with TUI.\nPress [1] to play the game with GUI");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        if (choice == 0){
            new Tui().run();
        }
        else{
            Gui.main(args);
        }
    }
}
