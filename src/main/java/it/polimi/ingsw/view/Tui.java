package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
//TODO: rendere unico lo stream di output
//TODO: separare il metodo clear aggiungendo flush e mettendo la string nella enum di ColorCli
public class Tui implements Ui{
    private ClientInterface client;
    private static final int ROW = 3;
    private static final int COLUMN = 9;
    private static final int PLAYERBOARD_DIM = 11;
    private static final int SCOREBOARD_ROW = 9;
    private static final int SCOREBOARD_COLUMN = 5;
    private String error = "";
    private final Scanner scanner;
    private final PrintStream out;
    private final Object lock = new Object();
    private boolean running = true;
    private Thread inputThread = new Thread(() -> {
        Scanner input = new Scanner(System.in);
        while (running) {
            String choice = input.nextLine();
            synchronized (lock) {
                handleInput(choice);
            }
        }
        input.close();
    });
    //TODO: TROVA UN MODO MIGLIORE PER FAVORE
    private PlayerBean player;
    private GameBean game;
    private ArrayList<PlayerBean> players;
    private String message;

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
                out.println("Wrong input.\nPlease, choose your connection method. Write:\n\tI)RMI\n\tII)Socket");
                choice = scanner.nextLine();
            }
            int port = askServerPort(choice);
            if (choice.equalsIgnoreCase("RMI")){
                client = new RMIClient(username, address, port, this);
            }
            else{
                client = new SocketClient(username, address, port, this);
            }
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
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
    public boolean askSide(){
        scanner.skip("\n");
        System.out.println("Which side you want to play?\nPress [f] for front and [b] for back");
        String choice = scanner.next();
        while (!choice.equalsIgnoreCase("b") && !choice.equalsIgnoreCase("f")){
            System.out.println("Which side you want to play?\nPress [f] for front and [b] for back");
            choice = scanner.nextLine();
        }
        return choice.equalsIgnoreCase("f");
    }

    public String[][] createPrintableCard(PlayerBoard playerBoard, Card card) {
        String[][] matCard = new String[ROW][COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                matCard[i][j] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
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

    public String[][] createPrintableRetro(Color color){
        String[][] matCard = new String[ROW][COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                matCard[i][j] = Color.getBackground(color) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
        return matCard;
    }
    public String[][] createPrintableCard(Card card) {
        String[][] matCard = new String[ROW][COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        if (card != null) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    matCard[i][j] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            //aggiungi angoli della carta
            for(CornerEnum corner : CornerEnum.values()){
                if(card.getCorner(corner).getSymbol().equals(Symbols.NOCORNER)){
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(Color.WHITE) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }

            //aggiungi simboli al centro se ci sono
            int center = 4;
            if (card.getSymbols() != null) {
                if(card.getSymbols().size() <= 2){
                    for (int i = 0; i < card.getSymbols().size(); i++) {
                        matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        center += 1;
                    }
                }
                else{
                    center -= 1;
                    for (int i = 0; i < card.getSymbols().size(); i++) {
                        matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        center += 1;
                    }
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
                    int costSum = Arrays.stream(card.getCost()).reduce(0, Integer::sum);
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
        }
        else{
            //if the card is null create a placeholder to maintain view's layout
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    matCard[i][j] = " ";
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
                if (achievement.getSymbols().size() == 3) {
                    matCard[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(1)) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
        String[][][][] matPlayerBoard = new String[3][2][PLAYERBOARD_DIM][PLAYERBOARD_DIM];
        int[] coord;

        String padding = "    ";
        for(int i = 0; i < PLAYERBOARD_DIM; i++){
            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                if(i == 0){
                    matPlayerBoard[0][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[0][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][0][i][j] = padding + String.valueOf((j + 1)/10);
                    matPlayerBoard[1][1][i][j] = String.valueOf((j + 1) - ((j + 1)/10)*10) + padding;
                    matPlayerBoard[2][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[2][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
                else if (j == 0) {
                    matPlayerBoard[0][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[0][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][0][i][j] = padding + String.valueOf((i + 1)/10);
                    matPlayerBoard[1][1][i][j] = String.valueOf((i + 1) - ((i + 1)/10)*10) + padding;
                    matPlayerBoard[2][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[2][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
                else{
                    matPlayerBoard[0][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[0][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[2][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    matPlayerBoard[2][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
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
                            matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                        else{
                            matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Color.getBackground(playerBoard.getCard(coordCover).getColor()) + Symbols.getStringBlack(playerBoard.getCard(coordCover).getCorner(corner.getOppositePosition()).getSymbol()) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                    }
                    else{
                        if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                            matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                        else{
                            matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        }
                    }
                }
                else if(card.getCorner(corner).getState() == CornerState.OCCUPIED){
                    if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                        matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
                else{
                    if(corner.equals(CornerEnum.TL) || corner.equals(CornerEnum.BL)){
                        matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        matPlayerBoard[(corner.getY() - 1)/(-1)][(corner.getX() + 1)/2][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
                matPlayerBoard[1][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + " " + TuiColors.getColor(TuiColors.ANSI_RESET);
                matPlayerBoard[1][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + " " + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
        return matPlayerBoard;
    }

    public String[][][][] createPrintableScoreBoard(ArrayList<PlayerBean> players){
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
            if (players.get(i).getPionColor() != null){
                if(players.get(i).getPoints() > 29){
                    matScoreBoard[scoreBoardPosition[i][0]][scoreBoardPosition[i][1]][scoreBoardPoints[29][0]][scoreBoardPoints[29][1]] = Color.getBackground(players.get(i).getPionColor()) + String.valueOf(i + 1) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    matScoreBoard[scoreBoardPosition[i][0]][scoreBoardPosition[i][1]][scoreBoardPoints[players.get(i).getPoints()][0]][scoreBoardPoints[players.get(i).getPoints()][1]] = Color.getBackground(players.get(i).getPionColor()) + String.valueOf(i + 1) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
        }
        return matScoreBoard;
    }

    public String[] createPrintableChat(ArrayList<String> chat){
        String[] matChat = new String[7];

        for(int i = 0; i < 7; i++){
            matChat[i] = "";
        }

        for(int i = 0; i < chat.size(); i++){
            matChat[i] = chat.get(i);
        }

        return matChat;
    }
    @Override
    public void printView(PlayerBean player, GameBean game, ArrayList<PlayerBean> players){
        PlayerBoard pBoard = player.getBoard();
        Card[] hand = player.getHand();
        String username = player.getUsername();
        Card[] commonResource = game.getCommonResources();
        Color resourceBack = game.getResourceDeckRetro();
        Card[] commonGold = game.getCommonGold();
        Color goldBack = game.getGoldDeckRetro();
        Achievement[] commonAchievement = game.getCommonAchievement();
        Achievement privateAchievement = player.getAchievement();
        ArrayList<String> messages = player.getChat();
        String[][] commonResource1 = this.createPrintableCard(commonResource[0]);
        String[][] commonResource2 = this.createPrintableCard(commonResource[1]);
        String[][] rBack = this.createPrintableRetro(resourceBack);
        String[][] commonGold1 = this.createPrintableCard(commonGold[0]);
        String[][] commonGold2 = this.createPrintableCard(commonGold[1]);
        String[][] gBack = this.createPrintableRetro(goldBack);
        String[][] commonAchievement1 = this.createPrintableAchievement(commonAchievement[0]);
        String[][] commonAchievement2 = this.createPrintableAchievement(commonAchievement[1]);
        players.add(player);
        String[][][][] scoreBoard = this.createPrintableScoreBoard(players);
        players.removeLast();
        String[] chat = this.createPrintableChat(messages);
        String[][][][] playerBoard = this.createPrintablePlayerBoard(pBoard);
        String[][] hand1 = this.createPrintableCard(hand[0]);
        String[][] hand2 = this.createPrintableCard(hand[1]);
        String[][] hand3 = this.createPrintableCard(hand[2]);
        String[][] privateAch = this.createPrintableAchievement(privateAchievement);
        
        //this.printTitle();

        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "commonboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.print(" ".repeat((3*COLUMN) + 6 + 6 - 11));
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
                        System.out.print(rBack[q][j]);
                    }
                    System.out.print("      ");
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
                        System.out.print(gBack[q][j]);
                    }
                    System.out.print("      ");
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
                    System.out.print(" ".repeat(COLUMN));
                    System.out.print("      ");
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
                    if(!chat[k].isEmpty()){
                        System.out.print(chat[k]);
                    }
                    k -= 1;
                }
                System.out.println();
            }
        }
        System.out.print(" ".repeat((3*COLUMN) + 6 + 6 + 6));
        System.out.println("‾".repeat(26));
        System.out.println();

        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your playerboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.print(" ".repeat(10*(PLAYERBOARD_DIM) - 14 - username.length()));
        System.out.print("      ");
        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your hand" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.print(" ".repeat(2*COLUMN + 2*6));
        System.out.print("      ");
        System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your personal achievement" + TuiColors.getColor(TuiColors.ANSI_RESET));
        System.out.println();

        for(int i = 0; i < PLAYERBOARD_DIM; i++){
            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                System.out.print(playerBoard[0][0][i][j]);
                System.out.print(playerBoard[0][1][i][j]);
            }
            System.out.print("      ");

            Symbols[] symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL, Symbols.INSECT, Symbols.MANUSCRIPT, Symbols.INKWELL, Symbols.QUILL, Symbols.EMPTY, Symbols.CORNER, Symbols.CARD};
            if(3*i >= height && 3*i < height + 10){
                if(3*i > height + 6){
                    System.out.print(" ".repeat(19));
                    System.out.print("      ");
                    System.out.print(Symbols.getString(symbols[3*i - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[3*i - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
                else{
                    System.out.print(Symbols.getLongString(symbols[3*i - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + ": " + pBoard.getSymbolCount(symbols[3*i - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                    int precLine = Symbols.getLongString(symbols[3*i - height]).length() + 2 + String.valueOf(pBoard.getSymbolCount(symbols[3*i - height])).length() - 11;
                    System.out.print(" ".repeat(19 + 6 - precLine));
                    System.out.print(Symbols.getString(symbols[3*i - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[3*i - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
            }

            if(hand1 != null){
                if(3*i < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[3*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(hand2 != null){
                if(3*i < hand2.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand2[3*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(hand3 != null){
                if(3*i < hand3.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand3[3*i][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(privateAchievement != null){
                if(3*i < privateAch.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(privateAch[3*i][j]);
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

            if(3*i + 1 == height - 2){
                System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your symbol's count" + TuiColors.getColor(TuiColors.ANSI_RESET));
                System.out.print("      ");
                System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "legend" + TuiColors.getColor(TuiColors.ANSI_RESET));
            }

            if(3*i + 1 >= height && 3*i + 1 < height + 10){
                if(3*i + 1 > height + 6){
                    System.out.print(" ".repeat(19));
                    System.out.print("      ");
                    System.out.print(Symbols.getString(symbols[3*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[3*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
                else{
                    System.out.print(Symbols.getLongString(symbols[3*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + ": " + pBoard.getSymbolCount(symbols[3*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                    int precLine = Symbols.getLongString(symbols[3*i + 1 - height]).length() + 2 + String.valueOf(pBoard.getSymbolCount(symbols[3*i + 1 - height])).length() - 11;
                    System.out.print(" ".repeat(19 + 6 - precLine));
                    System.out.print(Symbols.getString(symbols[3*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[3*i + 1 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
            }

            if(hand1 != null){
                if(3*i + 1 < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[3*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(hand2 != null){
                if(3*i + 1 < hand2.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand2[3*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(hand3 != null){
                if(3*i + 1 < hand3.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand3[3*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(privateAch != null){
                if(3*i + 1 < privateAch.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(privateAch[3*i + 1][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }
            System.out.println();

            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                System.out.print(playerBoard[2][0][i][j]);
                System.out.print(playerBoard[2][1][i][j]);
            }
            System.out.print("      ");

            if(3*i + 2 >= height && 3*i + 2 < height + 10){
                if(3*i + 2 > height + 6){
                    System.out.print(" ".repeat(19));
                    System.out.print("      ");
                    System.out.print(Symbols.getString(symbols[3*i + 2 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[3*i + 2 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
                else{
                    System.out.print(Symbols.getLongString(symbols[3*i + 2 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + ": " + pBoard.getSymbolCount(symbols[3*i + 2 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                    int precLine = Symbols.getLongString(symbols[3*i + 2 - height]).length() + 2 + String.valueOf(pBoard.getSymbolCount(symbols[3*i + 2 - height])).length() - 11;
                    System.out.print(" ".repeat(19 + 6 - precLine));
                    System.out.print(Symbols.getString(symbols[3*i + 2 - height]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(symbols[3*i + 2 - height]) + TuiColors.getColor(TuiColors.ANSI_RESET));
                }
            }

            if(hand1 != null){
                if(3*i + 2 < hand1.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand1[3*i + 2][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(hand2 != null){
                if(3*i + 2 < hand2.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand2[3*i + 2][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }

            if(hand3 != null){
                if(3*i + 2 < hand3.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(hand3[3*i + 2][j]);
                    }
                }
                else{
                    System.out.print(" ".repeat(COLUMN));
                }
                System.out.print("      ");
            }
            
            if(privateAch != null){
                if(3*i + 2 < privateAch.length){
                    for(int j = 0; j < COLUMN; j++){
                        System.out.print(privateAch[3*i + 2][j]);
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

    //TODO: Stampare retro carta quando devo piazzarla
    //TODO: aggiungere comando per uscire dal gioco
    //TODO: trova un modo migliore per condividere il model tra print e handle
    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {
        synchronized (lock){
            //model update
            this.player = player;
            this.game = game;
            this.players = players;
            PlayerBean playerInTurn = null;
            String username = player.getUsername();
            this.printView(player, game, players);
            if (!error.isEmpty()){
                System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + error + TuiColors.getColor(TuiColors.ANSI_RESET));
                error = "";
            }
            if(!message.isEmpty()){
                System.out.println(message);
                message = "";
            }
            if (game.getState().equals(GameState.START)){
                System.out.println("Waiting for all players to end their setup");
            } else if(game.getState().equals(GameState.END)){
                System.out.println("\nGG to everyone");
                this.running = false;
            }
            else{
                if (player.getState().equals(PlayerState.PLAY_CARD)){
                    playerInTurn = player;
                    System.out.println("Play a card");
                }
                else if(player.getState().equals(PlayerState.DRAW_CARD)){
                    playerInTurn = player;
                    System.out.println("Draw a card");
                }
                else{
                    for(PlayerBean p : players){
                        if(!p.getState().equals(PlayerState.NOT_IN_TURN)){
                            playerInTurn = p;
                            break;
                        }
                    }
                    System.out.println(playerInTurn.getUsername() + "'s turn. Wait for your turn to play");
                }
                System.out.println();
                System.out.println("Press [1] to view a card from your hand");
                System.out.println("Press [2] to view a card from your board");
                System.out.println("Press [3] to view another player's board");
                if(playerInTurn.getUsername().equals(username) && playerInTurn.getState().equals(PlayerState.PLAY_CARD)){
                    System.out.println("Press [4] to place a card");
                }
                else if(playerInTurn.getUsername().equals(username) && playerInTurn.getState().equals(PlayerState.DRAW_CARD)){
                    System.out.println("Press [4] to draw a card");
                }
                System.out.println("Press [c] anytime to send a message");
            }
            //clearConsole();
            //this.printView(player, game, players);
        }
    }
    //TODO: Refactor di questa funzione
    public void handleInput(String choice){
        PlayerBoard pBoard = player.getBoard();
        Card[] hand = player.getHand();
        Card[] commonResource = game.getCommonResources();
        Color resourceBack = game.getResourceDeckRetro();
        Card[] commonGold = game.getCommonGold();
        Color goldBack = game.getGoldDeckRetro();
        Scanner input = new Scanner(System.in);
        int[] coord = new int[2];
        switch (choice) {
            case "1"-> {
                System.out.println("Which card do you want to display? [1] [2] [3]");
                String a = input.next();
                clearConsole();
                this.printView(player, game, players);
                while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("c")) {
                    System.out.println("Which card do you want to display? [1] [2] [3]");
                    a = input.next();
                    clearConsole();
                    this.printView(player, game, players);
                }
                if (a.equals("c")) {
                    this.printChat(player, game, players);
                } else {
                    String[][] frontHand = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                    hand[Integer.parseInt(a) - 1].setCurrentSide();
                    String[][] backHand = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                    hand[Integer.parseInt(a) - 1].setCurrentSide();
                    for (int i = 0; i < ROW; i++) {
                        if (i == 0) {
                            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        } else {
                            System.out.print("       ");
                        }
                        for (int j = 0; j < COLUMN; j++) {
                            System.out.print(frontHand[i][j]);
                        }
                        System.out.print("      ");
                        if (i == 0) {
                            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        } else {
                            System.out.print("      ");
                        }
                        for (int j = 0; j < COLUMN; j++) {
                            System.out.print(backHand[i][j]);
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
            case "2" -> {
                System.out.println("Which card do you want to display?");
                System.out.println("Type row number");
                String a = input.next();
                clearConsole();
                this.printView(player, game, players);
                while (!a.equals("c") && Integer.parseInt(a) > PLAYERBOARD_DIM) {
                    System.out.println("Retype row number");
                    a = input.next();
                    clearConsole();
                    this.printView(player, game, players);
                }
                if (a.equals("c")) {
                    this.printChat(player, game, players);
                } else {
                    coord[0] = Integer.parseInt(a);
                    System.out.println("Type column number");
                    a = input.next();
                    clearConsole();
                    this.printView(player, game, players);
                    while (!a.equals("c") && Integer.parseInt(a) > PLAYERBOARD_DIM) {
                        System.out.println("Retype column number");
                        a = input.next();
                        clearConsole();
                        this.printView(player, game, players);
                    }
                    if (a.equals("c")) {
                        this.printChat(player, game, players);
                    } else {
                        coord[1] = Integer.parseInt(a);
                        clearConsole();
                        this.printView(player, game, players);
                        this.printCardFromPlayerBoard(pBoard, coord);
                    }
                }
            }
            case "3" -> {
                System.out.println("Which player's do you want to display?");
                for (int i = 0; i < players.size(); i++) {
                    System.out.println("\t Press " + (i + 1) + " to display " + players.get(i).getUsername() + "'s board");
                }
                String a = input.next();
                clearConsole();
                this.printView(player, game, players);
                while (!a.equals("c") && (Integer.parseInt(a) <= 0 || Integer.parseInt(a) > players.size())) {
                    System.out.println("Which player's do you want to display?");
                    for (int i = 0; i < players.size(); i++) {
                        System.out.println("\t Press " + i + 1 + " to display " + players.get(i).getUsername() + "'s board");
                    }
                    a = input.next();
                    clearConsole();
                    this.printView(player, game, players);
                }
                if (a.equals("c")) {
                    this.printChat(player, game, players);
                } else {
                    clearConsole();
                    this.printView(player, game, players);
                    System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + players.get(Integer.parseInt(a) - 1).getUsername() + "'s playerboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
                    System.out.println();
                    this.printPlayerBoard(players.get(Integer.parseInt(a) - 1).getBoard());
                }
            }
            case "4" -> {
                if (player.getState().equals(PlayerState.PLAY_CARD)) {
                    System.out.println("Which card do you want to place? [1] [2] [3]");
                    String a = input.next();
                    clearConsole();
                    this.printView(player, game, players);
                    while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("c")) {
                        System.out.println("Which card do you want to place? [1] [2] [3]");
                        a = input.next();
                        clearConsole();
                        this.printView(player, game, players);
                    }
                    if (a.equals("c")) {
                        this.printChat(player, game, players);
                    } else {
                        Card cardToPlace = hand[Integer.parseInt(a) - 1];
                        System.out.println("Which side do you want to place? [f] for front [b] for back");
                        System.out.println();
                        String[][] front = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                        hand[Integer.parseInt(a) - 1].setCurrentSide();
                        String[][] back = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                        hand[Integer.parseInt(a) - 1].setCurrentSide();
                        for (int i = 0; i < ROW; i++) {
                            if (i == 0) {
                                System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            } else {
                                System.out.print("       ");
                            }
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(front[i][j]);
                            }
                            System.out.print("      ");
                            if (i == 0) {
                                System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            } else {
                                System.out.print("      ");
                            }
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(back[i][j]);
                            }
                            System.out.println();
                        }
                        String b = input.next();
                        clearConsole();
                        this.printView(player, game, players);
                        while (!b.equals("f") && !b.equals("b") && !b.equals("c")) {
                            System.out.println("Which side do you want to place? [f] for front [b] for back");
                            for (int i = 0; i < ROW; i++) {
                                if (i == 0) {
                                    System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                                } else {
                                    System.out.print("       ");
                                }
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(front[i][j]);
                                }
                                System.out.print("      ");
                                if (i == 0) {
                                    System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                                } else {
                                    System.out.print("      ");
                                }
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(back[i][j]);
                                }
                                System.out.println();
                            }
                            b = input.next();
                        }
                        if (b.equals("c")) {
                            this.printChat(player, game, players);
                        } else if (b.equals("f")) {
                            this.printCard(hand[Integer.parseInt(a) - 1]);
                        } else {
                            cardToPlace.setCurrentSide();
                            //hand[Integer.parseInt(a) - 1].setCurrentSide();
                            this.printCard(cardToPlace);
                            //hand[Integer.parseInt(a) - 1].setCurrentSide();
                        }
                        System.out.println("Where do you want to place it?");
                        System.out.println("Type row number");
                        a = input.next();
                        while (!a.equals("c") && Integer.parseInt(a) > PLAYERBOARD_DIM) {
                            System.out.println("Retype row number");
                            a = input.next();
                            clearConsole();
                            this.printView(player, game, players);
                        }
                        if (a.equals("c")) {
                            this.printChat(player, game, players);
                        } else {
                            coord[0] = Integer.parseInt(a);
                            System.out.println("Type column number");
                            a = input.next();
                            clearConsole();
                            this.printView(player, game, players);
                            while (!a.equals("c") && Integer.parseInt(a) > PLAYERBOARD_DIM) {
                                System.out.println("Retype column number");
                                a = input.next();
                                clearConsole();
                                this.printView(player, game, players);
                            }
                            if(a.equals("c")){
                                this.printView(player, game, players);
                            }
                            else{
                                coord[1] = Integer.parseInt(a);
                                clearConsole();
                                this.printView(player, game, players);
                                this.printCardFromPlayerBoard(pBoard, coord);
                            }
                        }
                        clearConsole();
                        client.placeCard(cardToPlace, coord);
                        System.out.flush();
                    }
                } else {
                    System.out.println("Do you want to draw from decks or visible cards? [1] for decks [2] for visible cards");
                    String a = input.next();
//                    clearConsole();
//                    this.printView(player, game, players);
                    while (!a.equals("1") && !a.equals("2") && !a.equals("c")) {
                        System.out.println("Do you want to draw from decks or visible cards? [1] for decks [2] for visible cards");
                        a = input.next();
//                        clearConsole();
//                        this.printView(player, game, players);
                    }
                    if (a.equals("c")) {
                        this.printChat(player, game, players);
                    } else if (a.equals("1")) {
                        String[][] rDeck = this.createPrintableRetro(resourceBack);
                        String[][] gDeck = this.createPrintableRetro(goldBack);
                        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "resource deck:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        System.out.print("     ");
                        System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "gold deck:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        System.out.println();
                        for (int i = 0; i < ROW; i++) {
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(rDeck[i][j]);
                            }
                            System.out.print(" ".repeat(6 + 14 - COLUMN));
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(gDeck[i][j]);
                            }
                            System.out.println();
                        }
                        System.out.println();
                        System.out.println("Do you want to draw from resource deck or gold deck? [r] for resource [g] for gold");
                        a = input.next();
                        while (!a.equals("r") && !a.equals("g") && !a.equals("c")) {
                            clearConsole();
                            this.printView(player, game, players);
                            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "resource deck:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            System.out.print("     ");
                            System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "gold deck:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            System.out.println();
                            for (int i = 0; i < ROW; i++) {
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(rDeck[i][j]);
                                }
                                System.out.print(" ".repeat(6 + 14 - COLUMN));
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(gDeck[i][j]);
                                }
                                System.out.println();
                            }
                            System.out.println();
                            System.out.println("Do you want to draw from resource deck or gold deck? [r] for resource [g] for gold");
                            a = input.next();
                        }
                        if (a.equals("c")) {
                            this.printChat(player, game, players);
                        } else if (a.equals("r")) {
                            client.drawCard("resource");
                        } else {
                            client.drawCard("gold");
                        }
                    } else {
                        String[][] r1 = this.createPrintableCard(commonResource[0]);
                        String[][] r2 = this.createPrintableCard(commonResource[1]);
                        String[][] g1 = this.createPrintableCard(commonGold[0]);
                        String[][] g2 = this.createPrintableCard(commonGold[1]);
                        System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "resource cards:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        System.out.print(" ".repeat(6 + 6 + 2 * COLUMN - 15));
                        System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "gold cards:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                        System.out.println();
                        for (int i = 0; i < ROW; i++) {
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(r1[i][j]);
                            }
                            System.out.print("      ");
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(r2[i][j]);
                            }
                            System.out.print("       ");
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(g1[i][j]);
                            }
                            System.out.print("      ");
                            for (int j = 0; j < COLUMN; j++) {
                                System.out.print(g2[i][j]);
                            }
                            System.out.println();
                        }
                        System.out.println();
                        System.out.println("Which card do you want to draw? [1] for first resource [2] for second resource [3] for first gold [4] for second gold");
                        a = input.next();
                        while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("4") && !a.equals("c")) {
                            clearConsole();
                            this.printView(player, game, players);
                            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "resource cards:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            System.out.print(" ".repeat(6 + 6 + 2 * COLUMN - 15));
                            System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "gold cards:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            System.out.println();
                            for (int i = 0; i < ROW; i++) {
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(r1[i][j]);
                                }
                                System.out.print("      ");
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(r2[i][j]);
                                }
                                System.out.print("       ");
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(g1[i][j]);
                                }
                                System.out.print("      ");
                                for (int j = 0; j < COLUMN; j++) {
                                    System.out.print(g2[i][j]);
                                }
                                System.out.println();
                            }
                            System.out.println();
                            System.out.println("Which card do you want to draw? [1] for first resource [2] for second resource [3] for first gold [4] for second resource");
                            a = input.next();
                        }
                        if (a.equals("c")) {
                            this.printChat(player, game, players);
                        } else {
                            client.drawCardFromBoard(Integer.parseInt(a));
                        }
                    }
                }
            }
            case "c" -> this.printChat(player, game, players);
            default -> System.out.print("");
        }
    }
    public void printChat(PlayerBean player, GameBean game, ArrayList<PlayerBean> players){
        String username = player.getUsername();
        Scanner input = new Scanner(System.in);
        int u;
        do{
            System.out.println("Who do you want to send your message to?");
            for (int i = 0; i < players.size(); i++) {
                System.out.println("\t Press " + (i + 1) + " to send the message to " + players.get(i).getUsername());
            }
            System.out.println("\t Press " + (players.size() + 1) + " to send a global message");
            u = input.nextInt();
            clearConsole();
            this.printView(player, game, players);
        } while (u <= 0 || u > players.size() + 1);
        System.out.println("What's the content of your message?");
        //consume the newline
        input.nextLine();
        String m = input.nextLine();
        if (u == players.size() + 1) {
            client.sendChatMessage(m);
        }
        else {
            client.sendChatMessage(players.get(u - 1).getUsername(), m);
        }
        clearConsole();
        this.printView(player, game, players);
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
    public void printStarterCard(Card card){
        if(card == null){
            System.out.println("Starter card not existing");
        }
        else{
            String[][] starterFront = this.createPrintableCard(card);
            card.setCurrentSide();
            String[][] starterBack = this.createPrintableCard(card);
            card.setCurrentSide();

            System.out.print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
            System.out.print(" ".repeat(COLUMN + 6 - 7));
            System.out.println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
            System.out.println();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(starterFront[i][j]);
                }
                System.out.print("      ");
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(starterBack[i][j]);
                }
                System.out.println();
            }
            System.out.println();

        }

    }

    @Override
    public void printCard(PlayerBoard playerBoard, Card card){
        if(card == null){
            System.out.println("Card not existing at this coordinates");
        }
        else{
            String[][] mat = this.createPrintableCard(playerBoard, card);

            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(mat[i][j]);
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }

    }

    @Override
    public void printCard(Card card){
        if(card == null){
            System.out.println("Card not existing");
        }
        else{
            String[][] mat = this.createPrintableCard(card);
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(mat[i][j]);
                }
                System.out.print("\n");
            }
            System.out.print("\n");
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

    public void printScoreBoard(ArrayList<PlayerBean> players){
        String[][][][] scoreBoard = this.createPrintableScoreBoard(players);

        System.out.print("‾".repeat(26));
        System.out.println();
        for(int i = 0; i < SCOREBOARD_ROW; i++){
            for(int t = 0; t < 3; t ++){
                System.out.print("|");
                for(int j = 0; j < SCOREBOARD_COLUMN; j++){
                    for(int s = 0; s < 3; s++){
                        System.out.print(scoreBoard[s][t][i][j]);
                    }
                    System.out.print("|");
                }
                System.out.println();
            }
            System.out.print("‾".repeat(26));
            System.out.println();
        }
    }

    public void printGameBoard(Card[] resources, Card[] golds, Achievement[] achievements){
        String[][] r1 = this.createPrintableCard(resources[0]);
        String[][] r2 = this.createPrintableCard(resources[1]);
        String[][] g1 = this.createPrintableCard(golds[0]);
        String[][] g2 = this.createPrintableCard(golds[1]);
        String[][] a1 = this.createPrintableAchievement(achievements[0]);
        String[][] a2 = this.createPrintableAchievement(achievements[1]);

        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COLUMN; j++){
                System.out.print(r1[i][j]);
            }
            System.out.print("      ");
            for(int j = 0; j < COLUMN; j++){
                System.out.print(r2[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COLUMN; j++){
                System.out.print(g1[i][j]);
            }
            System.out.print("      ");
            for(int j = 0; j < COLUMN; j++){
                System.out.print(g2[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COLUMN; j++){
                System.out.print(a1[i][j]);
            }
            System.out.print("      ");
            for(int j = 0; j < COLUMN; j++){
                System.out.print(a2[i][j]);
            }
            System.out.println();
        }
        System.out.println();

    }
    //TODO: controlla se nell'eseguibile funziona
    public static void clearConsole(){
        //try{
        //    final String os = System.getProperty("os.name");
        //    if(os.contains("window")){
        //        Runtime.getRuntime().exec("cls");
        //    }
        //    else{
        //        Runtime.getRuntime().exec("clear");
        //    }
        //}
        //catch (final Exception e){
        //    System.out.println("error");
        //}
        System.out.println(TuiColors.getColor(TuiColors.ANSI_CLEAR));
        System.out.flush();
    }

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
    public Color chooseColor(List<Color> colors){
        System.out.println("Choose your color by typing the corresponding number:");
        for (int i = 0; i < colors.size(); i++){
            System.out.println("\t" + (1 + i) + ") " + colors.get(i));
        }
        int c = scanner.nextInt();
        while(c > colors.size() || c < 0){
            System.out.print("Invalid input. Retry: ");
            c = scanner.nextInt();
        }
        inputThread.start();
        return colors.get(c - 1);
    }
    @Override
    public void setMessage(String message, boolean isError){
        if(isError){
            this.error = message;
        }
        else{
            this.message = message;
        }
    }

    @Override //TODO: occhio al warning
    public void declareWinners(ArrayList<String> winners){
        if (winners.size() == 1){
           message = "The winner is: " + winners.getFirst();
        }
        else{
            message = "The winners are: \n";
            for (String s : winners){
                message += "\t" + s + "\n";
            }
        }
    }
}