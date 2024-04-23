package view;

import network.server.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

//public class Tui implements Ui{
//
//    public String readInput(){
//        Scanner scanner = new Scanner(System.in);
//        return scanner.nextLine();
//    }
//    @Override
//    public String askNickname() throws RemoteException{
//        String nickname;
//        System.out.println("input username: ");
//        nickname = readInput();
//        return nickname;
//    }
//
//    @Override
//    public VirtualServer askServerInfo() throws RemoteException{
//
//        String defaultPort = "1234";
//        String defaultAddress = "LocalHost";
//        String port;
//        String host;
//        System.out.println("please specify the following settings. the default value is shown between brackets.");
//        System.out.print("Enter the server address [" + defaultAddress + "]: ");
//        String address = readInput();
//        if (address.isEmpty()) {
//            host= defaultAddress;
//        }
//        else {
//            host = address;
//        }
//        System.out.print("Enter the server port [" + defaultPort + "]: ");
//        port = readInput();
//        if (port.isEmpty()) {
//            port = defaultPort;
//        }
//
//        return null;
//    }
//
//
//}