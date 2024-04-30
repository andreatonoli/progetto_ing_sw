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
import java.util.*;

public class Tui implements Ui{
    private static final int ROW = 3;
    private static final int COLUMN = 9;
    private static final int PLAYERBOARD_DIM = 11;
    private static final int SCOREBOARD_ROW = 9;
    private static final int SCOREBOARD_COLUMN = 5;
    //private String topBorder = "＿＿＿＿＿＿＿";
    //private String bottomBorder = "‾‾‾‾‾‾‾‾‾‾‾";
    //private String topBorderLong = "＿＿＿＿";
    //private String bottomBorderLong = "‾‾‾‾‾‾‾";
    //private String[][] matPlayerBoard = new String[PLAYERBOARD_DIM][PLAYERBOARD_DIM];
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
        scanner.skip("\n");
        System.out.println("Do you want to flip the card?");
        String choice = scanner.next();
        while (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")){
            System.out.println("Incorrect input \nDo you want to flip the card?\nType your answer");
            choice = scanner.nextLine();
        }
        return choice.equalsIgnoreCase("yes");
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
        int[] coord = playerBoard.getCardCoordinates(card);
        int[] coordCover = new int[2];

        for(CornerEnum corner : CornerEnum.values()){
            if(card.getCorner(corner).getState() == CornerState.NOT_VISIBLE){
                coordCover[0] = coord[0] + corner.getX();
                coordCover[1] = coord[1] + corner.getY();
                if(playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getState().equals(CornerState.OCCUPIED)){
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else{
                matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        //aggiungi simboli al centro se ci sono
        int center = 4;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                center += 1;
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
        for(CornerEnum corner : CornerEnum.values()){
            matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
        }

        //aggiungi simboli al centro se ci sono
        int center = 4;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                center += 1;
            }
        }

        if(!card.isBack()){
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
        }

        if(!card.isBack()){
            //aggiungi costo se c'è
            if(card.getCost() != null){
                int costSum = Arrays.stream(card.getCost()).sum();
                int bottomCenter = 4 - costSum / 2;
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
                        matCard[0][centerObject - 1] = Color.getBackground(Color.RED) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.RED) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.RED) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.BLUE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.BLUE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.BLUE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.GREEN) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.GREEN) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.GREEN) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.PURPLE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.PURPLE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.PURPLE)+ Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }

            }
            case AchievementL achievementL -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        matCard[0][centerObject] = Color.getBackground(Color.RED) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.RED) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.GREEN) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        matCard[0][centerObject] = Color.getBackground(Color.BLUE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.BLUE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.RED) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        matCard[0][centerObject] = Color.getBackground(Color.GREEN) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.GREEN) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.PURPLE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        matCard[0][centerObject] = Color.getBackground(Color.PURPLE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.PURPLE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.BLUE) + Symbols.getStringBlack(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            case AchievementItem achievementItem -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(0)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(1)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                if (achievement.getSymbols().size() == 3) {
                    matCard[1][centerObject] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(2)) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
        String[][][][] matPlayerBoard = new String[2][2][PLAYERBOARD_DIM][PLAYERBOARD_DIM];
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

            for(CornerEnum corner : CornerEnum.values()){
                if(card.getCorner(corner).getState().equals(CornerState.NOT_VISIBLE)){
                    coordCover[0] = coord[0] + corner.getX();
                    coordCover[1] = coord[1] + corner.getY();
                    if(playerBoard.getCard(coordCover) != null && playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getState().equals(CornerState.OCCUPIED)){
                        if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                            matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                        else{
                            matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getSymbol()) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                    }
                    else{
                        if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                            matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                        else{
                            matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                    }
                }
                else if(card.getCorner(corner).getState() == CornerState.OCCUPIED){
                    if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                        matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
                else{
                    if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                        matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        matPlayerBoard[(corner.getY() - 1)/(-2)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
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

    public String[][][][] createPrintableScoreBoard(ArrayList<Player> players){
        String[][][][] matScoreBoard = new String[3][3][SCOREBOARD_ROW][SCOREBOARD_COLUMN];
        int[][] scoreBoardPoints = new int[][]{{8, 1}, {8, 2}, {8, 3}, {7, 4}, {7, 3}, {7, 2}, {7, 1}, {6, 0}, {6, 1}, {6, 2}, {6, 3}, {5, 4}, {5, 3}, {5, 2}, {5, 1}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {3, 4}, {3, 2}, {3, 0}, {2, 0}, {1, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 4}, {2, 4}, {1, 2}};
        int[][] scoreBoardPosition = new int[][]{{0, 0}, {2, 0}, {0, 2}, {2, 2}};

        for(int i = 0; i < SCOREBOARD_ROW; i++){
            for(int j = 0; j < SCOREBOARD_COLUMN; j++){
                for(int t = 0; t < 3; t++){
                    for(int s = 0; s < 3;  s++){
                        if(s == 1){
                            matScoreBoard[s][t][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + Symbols.getString(Symbols.EMPTY_SPACE);
                        }
                        else{
                            matScoreBoard[s][t][i][j] = Symbols.getString(Symbols.EMPTY_SPACE);
                        }
                    }
                }
            }
        }
        matScoreBoard[1][1][0][0] = Symbols.getString(Symbols.INSECT) + " ";
        matScoreBoard[1][1][0][4] = " " + Symbols.getString(Symbols.ANIMAL);
        matScoreBoard[1][1][8][0] = Symbols.getString(Symbols.FUNGI) + " ";
        matScoreBoard[1][1][8][4] = " " + Symbols.getString(Symbols.PLANT);


        for(int i = 0; i < scoreBoardPoints.length; i++){
            matScoreBoard[1][1][scoreBoardPoints[i][0]][scoreBoardPoints[i][1]] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + String.valueOf(i / 10) + String.valueOf((i) - ((i)/10)*10) + TuiColors.getColor(TuiColors.ANSI_RESET);
        }

        //per il momento viene inserito il numero del player poi verrà stampato il colore del token
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getPoints() > 29){
                matScoreBoard[scoreBoardPosition[i][0]][scoreBoardPosition[i][1]][scoreBoardPoints[29][0]][scoreBoardPoints[29][1]] = String.valueOf(i + 1);
            }
            else{
                matScoreBoard[scoreBoardPosition[i][0]][scoreBoardPosition[i][1]][scoreBoardPoints[players.get(i).getPoints()][0]][scoreBoardPoints[players.get(i).getPoints()][1]] = String.valueOf(i + 1);
            }
        }
        return matScoreBoard;
    }

    public String[][] createPrintableChat(ArrayList<String> chat){
        String[][] matChat = new String[7][2];

        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 2; j++){
                matChat[i][j] = "";
            }
        }

        for(int i = 0; i < chat.size(); i++){
            matChat[i][0] = chat.get(i).splitWithDelimiters(":", 2)[0];
            matChat[i][1] = chat.get(i).splitWithDelimiters(":", 2)[2];
        }

        return matChat;
    }

    @Override
    public void printView(PlayerBoard pBoard, Card[] hand, String username, GameBoard gameBoard, ArrayList<Player> players, ArrayList<String> messages){
        String[][] commonResource1 = this.createPrintableCard(gameBoard.getCommonResource()[0]);
        String[][] commonResource2 = this.createPrintableCard(gameBoard.getCommonResource()[1]);
        String[][] commonGold1 = this.createPrintableCard(gameBoard.getCommonGold()[0]);
        String[][] commonGold2 = this.createPrintableCard(gameBoard.getCommonGold()[1]);
        String[][] commonAchievement1 = this.createPrintableAchievement(gameBoard.getCommonAchievement()[0]);
        String[][] commonAchievement2 = this.createPrintableAchievement(gameBoard.getCommonAchievement()[1]);
        String[][][][] scoreBoard = this.createPrintableScoreBoard(players);
        String[][] chat = this.createPrintableChat(messages);
        String[][][][] playerBoard = this.createPrintablePlayerBoard(pBoard);
        String[][] hand1 = this.createPrintableCard(pBoard, hand[0]);
        String[][] hand2 = this.createPrintableCard(pBoard, hand[1]);
        String[][] hand3 = this.createPrintableCard(pBoard, hand[2]);

        this.printTitle();

        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "commonboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.print(" ".repeat((2*COLUMN) + 6 - 11));
        System.out.print("      ");
        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "scoreboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.print(" ".repeat((5*SCOREBOARD_COLUMN) + 1 - 10));
        System.out.print("      ");
        System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "chat" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.println();

        int q = 0;
        int k = 6;
        boolean r = true;
        boolean g = false;
        boolean a = false;
        boolean divider;
        int height = 9;
        for(int i = 0; i < SCOREBOARD_ROW; i++){
            divider = true;
            for(int t = 0; t < 3; t++){
                if(q < ROW && r){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(commonResource1[q][j]);
                    }
                    System.out.print("      ");
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(commonResource2[q][j]);
                    }
                    System.out.print("      ");
                    q += 1;
                }
                else if(q < ROW && g){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(commonGold1[q][j]);
                    }
                    System.out.print("      ");
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(commonGold2[q][j]);
                    }
                    System.out.print("      ");
                    q += 1;
                }
                else if(q < ROW && a){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(commonAchievement1[q][j]);
                    }
                    System.out.print("      ");
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(commonAchievement2[q][j]);
                    }
                    System.out.print("      ");
                    q += 1;
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                    System.out.print("      ");
                    System.out.print(" ".repeat(COLUMN));
                    System.out.print("      ");
                    q = 0;
                    if(r){
                        r = false;
                        g = true;
                    }
                    else if(g){
                        g = false;
                        a = true;
                    }
                    else if(a){
                        a = false;
                    }
                }
                if(t == 0 && divider){
                    System.out.print("‾".repeat(26));
                    divider = false;
                    t = -1;
                }
                else{
                    System.out.print("|");
                    for(int j = 0; j < SCOREBOARD_COLUMN; j++){
                        for(int s = 0; s < 3; s++){
                            System.out.print(scoreBoard[s][t][i][j]);
                        }
                        System.out.print("|");
                    }
                }
                System.out.print("      ");
                if(k >= 0){
                    System.out.print(chat[k][0] + ": " + chat[k][1]);
                    k -= 1;
                }
                System.out.println();
            }
        }
        System.out.print(" ".repeat((2*COLUMN) + 6 + 6));
        System.out.println("‾".repeat(26));
        System.out.println();

        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your playerboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.print(" ".repeat(6*(PLAYERBOARD_DIM) - 14 - username.length()));
        System.out.print("      ");
        System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your hand" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.println();

        for(int i = 0; i < PLAYERBOARD_DIM; i++){

            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                System.out.print(playerBoard[0][0][i][j]);
                System.out.print(playerBoard[0][1][i][j]);
            }
            System.out.print("      ");

            //if(2*i == height - 2){
            //    System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your symbol's count" + TuiColors.getColor(TuiColors.ANSI_RESET));
            //    System.out.print("      ");
            //    System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "legend" + TuiColors.getColor(TuiColors.ANSI_RESET));
            //}

            Symbols[] symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL, Symbols.INSECT, Symbols.MANUSCRIPT, Symbols.INKWELL, Symbols.QUILL, Symbols.EMPTY, Symbols.CORNER, Symbols.CARD};
            if(2*i >= height && 2*i < height + 10){
                if(2*i > height + 6){
                    System.out.print(" ".repeat(19));
                    System.out.print("      ");
                    System.out.print(Symbols.getString(symbols[2*i - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[2*i - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
                else{
                    System.out.print(Symbols.getLongString(symbols[2*i - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + ": " + pBoard.getSymbolCount(symbols[2*i - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                    int precLine = Symbols.getLongString(symbols[2*i - height]).length() + 2 + String.valueOf(pBoard.getSymbolCount(symbols[2*i - height])).length() - 11;
                    System.out.print(" ".repeat(19 + 6 - precLine));
                    System.out.print(Symbols.getString(symbols[2*i - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[2*i - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
            }

            if(hand1 != null){
                if(2*i < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[2*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
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
                System.out.print("      ");
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
                System.out.print("      ");
            }
            System.out.println();

            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                System.out.print(playerBoard[1][0][i][j]);
                System.out.print(playerBoard[1][1][i][j]);
            }
            System.out.print("      ");

            if(2*i + 1 == height - 2){
                System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your symbol's count" + TuiColors.getColor(TuiColors.ANSI_RESET));
                System.out.print("      ");
                System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "legend" + TuiColors.getColor(TuiColors.ANSI_RESET));
            }

            if(2*i + 1 >= height && 2*i + 1 < height + 10){
                if(2*i + 1 > height + 6){
                    System.out.print(" ".repeat(19));
                    System.out.print("      ");
                    System.out.print(Symbols.getString(symbols[2*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[2*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
                else{
                    System.out.print(Symbols.getLongString(symbols[2*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + ": " + pBoard.getSymbolCount(symbols[2*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                    int precLine = Symbols.getLongString(symbols[2*i + 1 - height]).length() + 2 + String.valueOf(pBoard.getSymbolCount(symbols[2*i + 1 - height])).length() - 11;
                    System.out.print(" ".repeat(19 + 6 - precLine));
                    System.out.print(Symbols.getString(symbols[2*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[2*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
            }

            if(hand1 != null){
                if(2*i + 1 < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[2*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
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
                System.out.print("      ");
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
                System.out.print("      ");
            }
            System.out.println();
        }
    }

    public void printViewWithCommands(PlayerBoard pBoard, Card[] hand, String username, GameBoard gameBoard, ArrayList<Player> players, ArrayList<String> messages){

        this.printView(pBoard, hand, username, gameBoard, players, messages);

        Scanner input = new Scanner(System.in);
        int[] coord = new int[2];
        while(true){
            System.out.println("Press [1] to view a card from your hand");
            System.out.println("Press [2] to view a card from your board");
            System.out.println("Press [3] to view another player's board");
            System.out.println("Press [4] to place a card");
            int choice = input.nextInt();
            this.clearConsole();
            this.printView(pBoard, hand, username, gameBoard, players, messages);
            if(choice == 1){
                System.out.println("Which card do you want to display? [1] [2] [3]");
                int h = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(h != 1 && h != 2 && h != 3){
                    System.out.println("Which card do you want to display? [1] [2] [3]");
                    h = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                String[][] frontHand = this.createPrintableCard(hand[h-1]);
                hand[h-1].setCurrentSide();
                String[][] backHand = this.createPrintableCard(hand[h-1]);
                hand[h-1].setCurrentSide();
                for(int i = 0; i < ROW; i++){
                    if(i == 0){
                        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                    }
                    else{
                        System.out.print("       ");
                    }
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(frontHand[i][j]);
                    }
                    System.out.print("      ");
                    if(i == 0){
                        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                    }
                    else{
                        System.out.print("      ");
                    }
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(backHand[i][j]);
                    }
                    System.out.println();
                }
                System.out.println();
            }
            else if(choice == 2){
                System.out.println("Which card do you want to display?");
                System.out.println("Type row number");
                coord[0] = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(coord[0] > (Integer) PLAYERBOARD_DIM){
                    System.out.println("Retype row number");
                    coord[0] = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                System.out.println("Type column number");
                coord[1] = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(coord[1] > (Integer) PLAYERBOARD_DIM){
                    System.out.println("Retype column number");
                    coord[1] = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                this.printCardFromPlayerBoard(pBoard, coord);
            }
            else if(choice == 3){
                ArrayList<Player> othersPlayers = new ArrayList<>();
                for(Player p : players){
                    if(!p.getUsername().equals(username)){
                        othersPlayers.add(p);
                    }
                }
                System.out.println("Which player's do you want to display?");
                for(int i = 0; i < othersPlayers.size(); i++){
                    System.out.println("\t Press " + String.valueOf(i+1) + " to display " + othersPlayers.get(i).getUsername() + "'s board");
                }
                int u = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(u > othersPlayers.size()){
                    System.out.println("Which player's do you want to display?");
                    for(int i = 0; i < othersPlayers.size(); i++){
                        System.out.println("\t Press " + i+1 + " to display " + othersPlayers.get(i).getUsername() + "'s board");
                    }
                    u = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + othersPlayers.get(u-1).getUsername() + "'s playerboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
                System.out.println();
                this.printPlayerBoard(othersPlayers.get(u-1).getPlayerBoard());
            }
            else if(choice ==  4){
                System.out.println("Which card do you want to place? [1] [2] [3]");
                int p = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(p != 1 && p != 2 && p != 3){
                    System.out.println("Which card do you want to place? [1] [2] [3]");
                    p = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                System.out.println("Which side do you want to place? [f] for front [b] for back");
                System.out.println();
                String[][] front = this.createPrintableCard(hand[p-1]);
                hand[p-1].setCurrentSide();
                String[][] back = this.createPrintableCard(hand[p-1]);
                hand[p-1].setCurrentSide();
                for(int i = 0; i < ROW; i++){
                    if(i == 0){
                        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                    }
                    else{
                        System.out.print("       ");
                    }
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(front[i][j]);
                    }
                    System.out.print("      ");
                    if(i == 0){
                        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                    }
                    else{
                        System.out.print("      ");
                    }
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(back[i][j]);
                    }
                    System.out.println();
                }
                char s = input.next().charAt(0);
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(s != 'f' && s != 'b'){
                    System.out.println("Which side do you want to place? [f] for front [b] for back");
                    for(int i = 0; i < ROW; i++){
                        if(i == 0){
                            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        }
                        else{
                            System.out.print("       ");
                        }
                        for(int j = 0; j < COLUMN; j++){
                            System.out.print(front[i][j]);
                        }
                        System.out.print("      ");
                        if(i == 0){
                            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        }
                        else{
                            System.out.print("      ");
                        }
                        for(int j = 0; j < COLUMN; j++){
                            System.out.print(back[i][j]);
                        }
                        System.out.println();
                    }
                    s = input.next().charAt(0);
                }
                if(s == 'f'){
                    this.printCard(hand[p-1]);
                }
                else if(s == 'b'){
                    hand[p-1].setCurrentSide();
                    this.printCard(hand[p-1]);
                    hand[p-1].setCurrentSide();
                }
                System.out.println("Where do you want to place it?");
                System.out.println("Type row number");
                coord[0] = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(coord[0] > (Integer) PLAYERBOARD_DIM){
                    System.out.println("Retype row number");
                    coord[0] = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                System.out.println("Type column number");
                coord[1] = input.nextInt();
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                while(coord[1] > (Integer) PLAYERBOARD_DIM){
                    System.out.println("Retype column number");
                    coord[1] = input.nextInt();
                    this.clearConsole();
                    this.printView(pBoard, hand, username, gameBoard, players, messages);
                }
                for(int i = 0; i < players.size(); i++){
                    if(players.get(i).getUsername().equals(username)){
                        Player player = players.get(i);
                    }
                }
                this.clearConsole();
                this.printView(pBoard, hand, username, gameBoard, players, messages);
                System.out.println("QUI IL CONTROLLER DEVE AVER PIAZZATO LA CARTA");
                System.out.println();
            }
            else{
                this.clearConsole();
            }
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
            System.out.println("Card not existing at this coordinates");
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
            System.out.println("Card not existing");
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

    public void printPlayerBoard(PlayerBoard playerBoard){
        String [][][][] mat = this.createPrintablePlayerBoard(playerBoard);
        //System.out.println(topBorderLong.repeat(PLAYERBOARD_DIM + 1));
        for(int i = 0; i < PLAYERBOARD_DIM; i++){
            for(int t = 0; t < 2; t++){
                for(int j = 0; j < PLAYERBOARD_DIM; j++) {
                    //System.out.print("|");
                    for (int s = 0; s < 2; s++) {
                            System.out.print(mat[t][s][i][j]);
                    }
                }
                //System.out.println("|");
                System.out.println();
            }
            //System.out.println(bottomBorderLong.repeat(PLAYERBOARD_DIM));
        }
    }

    public void clearConsole(){
        try{
            final String os = System.getProperty("os.name");
            if(os.contains("window")){
                Runtime.getRuntime().exec("cls");
            }
            else{
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e){
            System.out.println("error");
        }
    }

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