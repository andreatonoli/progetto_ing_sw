package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

public class ServerLauncher {
    public static void main (String[] args){
        if (args.length < 1){
            System.out.println("Insert ip address as argument");
            return;
        }
        System.setProperty("java.rmi.server.hostname", args[0]);
        new Server();
    }
}
