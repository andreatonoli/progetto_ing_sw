package view;

import model.card.*;
import model.enums.Color;
import model.enums.CornerEnum;
import model.enums.CornerState;
import model.enums.Symbols;
import model.player.PlayerBoard;
import network.client.Client;
import network.client.RMIClient;
import network.client.SocketClient;
import network.server.Server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Tui implements Ui{
    private static final int ROW = 3;
    private static final int COLUMN = 9;
    private static final int PLAYERBOARD_DIM = 11;
    //private String topBorder = "＿＿＿＿＿＿＿";
    //private String bottomBorder = "‾‾‾‾‾‾‾‾‾‾‾";
    //private String topBorderLong = "＿＿＿＿";
    //private String bottomBorderLong = "‾‾‾‾‾‾‾";
    //private String[][] matPlayerBoard = new String[PLAYERBOARD_DIM][PLAYERBOARD_DIM];
    private String[][][][] matPlayerBoard = new String[2][2][PLAYERBOARD_DIM][PLAYERBOARD_DIM];
    private String padding = "  ";
    private Scanner scanner;
    private Client client;
    private PrintStream out;

    public Tui(){
        scanner = new Scanner(System.in);
        out = System.out;
    }

    /**
     * Starts the TUI instance and begins the login phase
     */
    public void run(){
        try {
            this.printTitle();
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
    public int selectGame(int freeLobbies){
        System.out.println("Select one of the following game's lobby by writing the respective number");
        int i;
        for (i = 0; i < freeLobbies; i++){
            System.out.println("Lobby " + i);
        }
        System.out.println("Lobby " + i + "(create new game)");
        int choice = scanner.nextInt();
        while (choice > freeLobbies || choice < 0){
            System.out.print("Invalid input.\nInsert the lobby number: ");
            choice = scanner.nextInt();
        }
        return choice;
    }

    public int setLobbySize(){
        System.out.println("Select the lobby's capacity (min is " + Server.MIN_PLAYERS_PER_LOBBY + " and max is " + Server.MAX_PLAYERS_PER_LOBBY + " players)");
        int lobbySize = scanner.nextInt();
        while (lobbySize < Server.MIN_PLAYERS_PER_LOBBY || lobbySize > Server.MAX_PLAYERS_PER_LOBBY){
            System.out.println("Invalid input.\n Insert a valid number");
            lobbySize = scanner.nextInt();
        }
        return lobbySize;
    }

    @Override
    public boolean askToFlip(){
        System.out.println("Do you want to flip the card?");
        String choice = scanner.nextLine();
        while (!choice.equalsIgnoreCase("yes") || !choice.equalsIgnoreCase("no")){
            System.out.println("Incorrect input:\nDo you want to flip the card?\nType your answer");
            choice = scanner.nextLine();
        }
        if (choice.equalsIgnoreCase("yes")){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void showText(String text){
        System.out.println(text);
    }

    public String[][] createPrintableCard(PlayerBoard playerBoard, Card card) {
        String[][] matCard = new String[ROW][COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                matCard[i][j] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        //switch (card.getColor()) {
        //    case WHITE -> {
        //        mat[0][0] = "⎸";
        //        mat[1][0] = "⎸";
        //        mat[2][0] = "⎸";
        //        mat[0][10] = "⎹";
        //        mat[1][10] = "⎹";
        //        mat[2][10] = "⎹";
        //    }
        //    case RED -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //    case BLUE -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //    case GREEN -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //    case PURPLE -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //}


        //aggiungi angoli della carta
        int[] coordCover = playerBoard.getCardCoordinates(card);
        //if(card.isBack()){
        //    card = card.getBack();
        //}

        for(CornerEnum corner : CornerEnum.values()){
            if(card.getCorner(corner).getState() == CornerState.NOT_VISIBLE){
                coordCover[0] = corner.getX();
                coordCover[1] = corner.getY();
                if(playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getState().equals(CornerState.OCCUPIED)){
                    matCard[(corner.getY()-1)/-2][(corner.getX()-1)/2] = Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    matCard[(corner.getY()-1)/-2][(corner.getX()-1)/2] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else{
                matCard[(corner.getY()-1)/-2][(corner.getX()-1)/2] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

        }

        //if (card.isBack()) {
        //    if(card.getBack().getCorners()[0].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED){
        //            matCard[0][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getBack().getCorners()[1].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED){
        //            matCard[0][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getBack().getCorners()[2].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED){
        //            matCard[2][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[2][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getBack().getCorners()[3].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED){
        //            matCard[2][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[2][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //}
        //else {
        //    if(card.getCorners()[0].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED){
        //            matCard[0][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getCorners()[1].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED){
        //            matCard[0][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getCorners()[2].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED){
        //            matCard[2][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[2][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getCorners()[3].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED){
        //            matCard[2][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //    }
        //    else{
        //        matCard[2][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //}

        //aggiungi simboli al centro se ci sono
        int center = 4;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                center += center;
            }
        }

        //aggiungi punti se ci sono
        int topCenter = 4;
        if (card.getPoints() != 0) {
            switch (card.getCondition()) {
                case NOTHING -> {
                    if(card.getPoints() != 0){
                        matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
                case CORNER -> {
                    matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.CORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                case ITEM -> {
                    matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getRequiredItem()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
        }

        //aggiungi costo se c'è
        if(card.getCost() != null){
            int bottomCenter = 4 - card.getCost().length / 2 + 1;
            for (int i = 0; i < card.getCost().length; i++) {
                for (int j = 0; j < card.getCost()[i]; j++) {
                    switch (i) {
                        case 0 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.FUNGI) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 1 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.PLANT) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 2 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.ANIMAL) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 3 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.INSECT) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    bottomCenter += 1;
                }
            }
        }
        return matCard;
    }

    public String[][] createPrintableCard(Card card) {
        String[][] matCard = new String[ROW][COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                matCard[i][j] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        //switch (card.getColor()) {
        //    case WHITE -> {
        //        mat[0][0] = "⎸";
        //        mat[1][0] = "⎸";
        //        mat[2][0] = "⎸";
        //        mat[0][10] = "⎹";
        //        mat[1][10] = "⎹";
        //        mat[2][10] = "⎹";
        //    }
        //    case RED -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //    case BLUE -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //    case GREEN -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //    case PURPLE -> {
        //        mat[0][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[0][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[1][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        mat[2][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //}


        //aggiungi angoli della carta
        //if(card.isBack()){
        //    card = card.getBack();
        //}

        for(CornerEnum corner : CornerEnum.values()){
            matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() - 1)/(-2)] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        }

        //if (card.isBack()) {
        //    if(card.getBack().getCorners()[0].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED){
        //            matCard[0][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getBack().getCorners()[1].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED){
        //            matCard[0][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getBack().getCorners()[2].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED){
        //            matCard[2][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[2][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getBack().getCorners()[3].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED){
        //            matCard[2][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[2][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //}
        //else {
        //    if(card.getCorners()[0].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED){
        //            matCard[0][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getCorners()[1].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] += 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED){
        //            matCard[0][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[0][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[0][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getCorners()[2].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] += 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED){
        //            matCard[2][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        coordCover = player.getPlayerBoard().getCardCoordinates(card);
        //    }
        //    else{
        //        matCard[2][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
//
        //    if(card.getCorners()[3].getState() == CornerState.NOT_VISIBLE){
        //        coordCover[0] -= 1;
        //        coordCover[1] -= 1;
        //        if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED){
        //            matCard[2][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //        else{
        //            matCard[2][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //        }
        //    }
        //    else{
        //        matCard[2][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        //    }
        //}

        //aggiungi simboli al centro se ci sono
        int center = 4;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                center += center;
            }
        }

        //aggiungi punti se ci sono
        int topCenter = 4;
        if (card.getPoints() != 0) {
            switch (card.getCondition()) {
                case NOTHING -> {
                    if(card.getPoints() != 0){
                        matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
                case CORNER -> {
                    matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.CORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                case ITEM -> {
                    matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getRequiredItem()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
        }

        //aggiungi costo se c'è
        if(card.getCost() != null){
            int bottomCenter = 4 - card.getCost().length / 2 + 1;
            for (int i = 0; i < card.getCost().length; i++) {
                for (int j = 0; j < card.getCost()[i]; j++) {
                    switch (i) {
                        case 0 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.FUNGI) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 1 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.PLANT) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 2 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.ANIMAL) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 3 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.INSECT) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    bottomCenter += 1;
                }
            }
        }
        return matCard;
    }

    public String[][] createPrintableAchievement(Achievement achievement) {
        String[][] matCard = new String[ROW][COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                matCard[i][j] = Color.getBackground(Color.YELLOW) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
        //mat[0][0] = "⎸";
        //mat[1][0] = "⎸";
        //mat[2][0] = "⎸";
        //mat[0][10] = "⎹";
        //mat[1][10] = "⎹";
        //mat[2][10] = "⎹";

        int centerPoints = 1;
        int centerObject = 6;
        switch (achievement) {
            case AchievementDiagonal achievementDiagonal -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }

            }
            case AchievementL achievementL -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        matCard[0][centerObject] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        matCard[0][centerObject] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        matCard[0][centerObject] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        matCard[0][centerObject] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            case AchievementItem achievementItem -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getStringBlack(achievement.getSymbols().get(0)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getStringBlack(achievement.getSymbols().get(1)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                if (achievement.getSymbols().size() == 3) {
                    matCard[1][centerObject] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getStringBlack(achievement.getSymbols().get(2)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            case AchievementResources achievementResources -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[1][centerObject] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case null, default -> {
            }
        }
        return matCard;
    }

    public String[][][][] createPrintablePlayerBoard(PlayerBoard playerBoard){
        int[] coord;

        for(int i = 0; i < PLAYERBOARD_DIM; i++){
            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                if(i == 0){
                    matPlayerBoard[0][0][i][j] = padding + String.valueOf((j + 1)/10);
                    matPlayerBoard[0][1][i][j] = String.valueOf((j + 1) - ((j + 1)/10)*10) + padding;
                    matPlayerBoard[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
                else if (j == 0) {
                    matPlayerBoard[0][0][i][j] = padding + String.valueOf((i + 1)/10);
                    matPlayerBoard[0][1][i][j] = String.valueOf((i + 1) - ((i + 1)/10)*10) + padding;
                    matPlayerBoard[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
                else{
                    matPlayerBoard[0][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[0][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
            }
        }

        for (Integer i : playerBoard.getPositionCardKeys()) {
            coord = playerBoard.getCardCoordinates(playerBoard.getCardPositon().get(i));
            int[] coordCover = new int[2];
            Card card = playerBoard.getCard(coord);
            if (card.getCorners()[0].getState() == CornerState.NOT_VISIBLE) {
                coordCover[0] = coord[0] - 1;
                coordCover[1] = coord[1] + 1;
                if (playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED) {
                    matPlayerBoard[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorners()[2].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                } else {
                    matPlayerBoard[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if (card.getCorners()[0].getState() == CornerState.OCCUPIED) {
                matPlayerBoard[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else {
                matPlayerBoard[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

            if (card.getCorners()[1].getState() == CornerState.NOT_VISIBLE) {
                coordCover[0] = coord[0] + 1;
                coordCover[1] = coord[1] + 1;
                if (playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED) {
                    matPlayerBoard[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                } else {
                    matPlayerBoard[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if(card.getCorners()[1].getState() == CornerState.OCCUPIED){
                matPlayerBoard[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(Symbols.NOCORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else {
                matPlayerBoard[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

            if(card.getCorners()[3].getState() == CornerState.NOT_VISIBLE){
                coordCover[0] = coord[0] - 1;
                coordCover[1] = coord[1] - 1;
                if (playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED) {
                    matPlayerBoard[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorners()[1].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else {
                    matPlayerBoard[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if(card.getCorners()[3].getState() == CornerState.OCCUPIED){
                matPlayerBoard[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else{
                matPlayerBoard[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

            if(card.getCorners()[2].getState() == CornerState.NOT_VISIBLE){
                coordCover[0] = coord[0] + 1;
                coordCover[1] = coord[1] - 1;
                if (playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED) {
                    matPlayerBoard[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else {
                    matPlayerBoard[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if(card.getCorners()[3].getState() == CornerState.OCCUPIED){
                matPlayerBoard[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(Symbols.NOCORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else{
                matPlayerBoard[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
        return matPlayerBoard;

        //PLAYERBOARD SENZA ANGOLI

        //topBorderLong = "＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿";
        //bottomBorderLong = "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾";

        //for (int i = 0; i < PLAYERBOARD_DIM; i++) {
        //    for (int j = 0; j < PLAYERBOARD_DIM; j++) {
        //        if(i == 0){
        //            matPlayerBoard[i][j] = String.valueOf(j + 1);
        //        }
        //        else if (j == 0) {
        //            matPlayerBoard[i][j] = String.valueOf(i + 1);
        //        }
        //        else{
        //            matPlayerBoard[i][j] = Symbols.getString(Symbols.EMPTY_SPACE);
        //        }
        //    }
        //}

        //for (Integer i : player.getPlayerBoard().getPositionCardKeys()){
        //    coord = player.getPlayerBoard().getCardCoordinates(player.getPlayerBoard().getCardPositon().get(i));
        //    Card card = player.getPlayerBoard().getCard(coord);
        //    matPlayerBoard[PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
        //}

        //System.out.println(topBorderLong);
        //for(int i = 0; i < PLAYERBOARD_DIM; i++){
        //    for(int j = 0; j < PLAYERBOARD_DIM; j++){
        //        System.out.printf("|%2s", matPlayerBoard[i][j]);
        //    }
        //    System.out.println("|");
        //}
        //System.out.println(bottomBorderLong);
    }

    @Override
    public void printView(PlayerBoard pBoard, Card[] hand, String username){
        String[][][][] playerBoard = this.createPrintablePlayerBoard(pBoard);
        String[][] hand1 = this.createPrintableCard(pBoard, hand[0]);
        String[][] hand2 = this.createPrintableCard(pBoard, hand[1]);
        String[][] hand3 = this.createPrintableCard(pBoard, hand[2]);

        System.out.println();
        System.out.print(username + "'s PlayerBoard");
        System.out.print("    ".repeat(PLAYERBOARD_DIM + 1));
        System.out.print("     ");
        System.out.println("  " + username + "'s hand");


        for(int i = 0; i < PLAYERBOARD_DIM; i++){

            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                System.out.print(playerBoard[0][0][i][j]);
                System.out.print(playerBoard[0][1][i][j]);
            }
            System.out.print("     ");

            if(hand1 != null){
                if(2*i < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[2*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("     ");
            }

            if(hand2 != null){
                if(2*i < hand2.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand2[2*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("     ");
            }

            if(hand3 != null){
                if(2*i < hand3.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand3[2*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("     ");
            }
            System.out.println();

            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                System.out.print(playerBoard[1][0][i][j]);
                System.out.print(playerBoard[1][1][i][j]);
            }
            System.out.print("     ");

            if(hand1 != null){
                if(2*i + 1 < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[2*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("     ");
            }

            if(hand2 != null){
                if(2*i + 1 < hand2.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand2[2*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("     ");
            }

            if(hand3 != null){
                if(2*i + 1 < hand3.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand3[2*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("     ");
            }
            System.out.println();
        }
    }

    public void printTitle(){
        System.out.print("\n");
        System.out.print(TuiColors.getColor(TuiColors.ANSI_YELLOW) + " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗\n" +
                "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝\n" +
                "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗\n" +
                "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║\n" +
                "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║\n" +
                " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝\n" +
                "                                                                                                                    \n"
                + TuiColors.getColor(TuiColors.ANSI_RESET));
    }
    @Override
    public void printCardFromPlayerBoard(PlayerBoard playerBoard, int[] coord){
        int[] c = new int[2];
        c[0] = coord[1] - (PLAYERBOARD_DIM / 2) - 1;
        c[1] = (PLAYERBOARD_DIM / 2) + 1 - coord[0];
        this.printCard(playerBoard, playerBoard.getCard(c));
    }

    @Override
    public void printCard(PlayerBoard playerBoard, Card card){
        if(card == null){
            System.out.println("Card not exsisting at this coordinates");
        }
        else{
            String[][] mat = this.createPrintableCard(playerBoard, card);

            //stampa i bordi superiori e inferiori e la matrice
            //switch (card.getColor()) {
            //    case WHITE -> System.out.println(topBorder);
            //    case RED ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case BLUE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_BLUE) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case GREEN ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_GREEN) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case PURPLE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_PURPLE) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //}
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(mat[i][j]);
                }
                System.out.print("\n");
            }
            System.out.print("\n");

            //switch (card.getColor()) {
            //    case WHITE -> System.out.println(bottomBorder);
            //    case RED ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case BLUE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_BLUE) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case GREEN ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_GREEN) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case PURPLE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_PURPLE) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //}
        }

    }

    @Override
    public void printCard(Card card){
        if(card == null){
            System.out.println("Card not exsisting at this coordinates");
        }
        else{
            String[][] mat = this.createPrintableCard(card);

            //stampa i bordi superiori e inferiori e la matrice
            //switch (card.getColor()) {
            //    case WHITE -> System.out.println(topBorder);
            //    case RED ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case BLUE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_BLUE) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case GREEN ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_GREEN) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case PURPLE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_PURPLE) + topBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //}
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(mat[i][j]);
                }
                System.out.print("\n");
            }
            System.out.print("\n");

            //switch (card.getColor()) {
            //    case WHITE -> System.out.println(bottomBorder);
            //    case RED ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case BLUE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_BLUE) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case GREEN ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_GREEN) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    case PURPLE ->
            //            System.out.println(TuiColors.getColor(TuiColors.ANSI_PURPLE) + bottomBorder + TuiColors.getColor(TuiColors.ANSI_RESET));
            //}
        }

    }

    @Override
    public void printAchievement(Achievement achievement){
        String[][] mat = this.createPrintableAchievement(achievement);

        //stampa i bordi superiori e inferiori e la matrice
        //System.out.println(topBorder);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                System.out.print(mat[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        //System.out.println(bottomBorder);
    }


    //inutile probabilmente
    //public void printPlayerBoard(Player player){
    //    String [][][][] mat = this.createPrintablePlayerBoard(player);
//
    //    //System.out.println(topBorderLong.repeat(PLAYERBOARD_DIM + 1));
    //    for(int i = 0; i < PLAYERBOARD_DIM; i++){
    //        for(int t = 0; t < 2; t++){
    //            for(int j = 0; j < PLAYERBOARD_DIM; j++) {
    //                //System.out.print("|");
    //                for (int s = 0; s < 2; s++) {
    //                        System.out.print(mat[t][s][i][j]);
    //                }
    //            }
    //            //System.out.println("|");
    //            System.out.println();
    //        }
    //        //System.out.println(bottomBorderLong.repeat(PLAYERBOARD_DIM));
    //    }
    //}

    //TODO: spostare in TUIHandler e cancellare fino al prossimo merge
    @Override
    public Achievement chooseAchievement(Achievement[] choices) {
        System.out.println("I)");
        this.printAchievement(choices[0]);
        System.out.println("II)");
        this.printAchievement(choices[1]);
        System.out.println("Choose one of the two Achievements");
        int choice = scanner.nextInt();
        while (choice < 1 || choice > 2){
            System.out.println("Wrong input.\nWrite 1 for the first achievement and 2 for the second");
            choice = scanner.nextInt();
        }
        return choices[choice - 1];
    }

}