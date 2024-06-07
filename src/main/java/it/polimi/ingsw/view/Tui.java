package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import org.fusesource.jansi.AnsiConsole;

import java.io.Console;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Tui implements Ui{
    private ClientInterface client;
    private String error = "";
    private String message = "";
    private PlayerBean player;
    private GameBean game;
    private ArrayList<PlayerBean> players;
    private final Scanner scanner;
    private final Object lock = new Object();
    private boolean running = true;
    private final Thread inputThread = new Thread(() -> {
        Scanner input = new Scanner(System.in);
        while (running) {
            String choice = input.nextLine();
            synchronized (lock) {
                if (choice.equals("q")){
                    printViewWithCommands(player, game, players);
                }
                else{
                    handleInput(choice);
                }
            }
        }
        input.close();
    });

    public Tui(){
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the TUI instance and begins the login phase
     */
    public void run(){
        try {
            AnsiConsole.systemInstall();
            this.printTitle();
            String choice;
            AnsiConsole.out().println("Please specify the following settings. The default value is shown between brackets.");
            String address = askServerAddress();
            AnsiConsole.out().println("Choose your connection method: Write:\n\tI)RMI\n\tII)Socket");
            choice = scanner.nextLine();
            while(!choice.equalsIgnoreCase("RMI") && !choice.equalsIgnoreCase("Socket")){
                AnsiConsole.out().println("Wrong input.\nPlease, choose your connection method. Write:\n\tI)RMI\n\tII)Socket");
                choice = scanner.nextLine();
            }
            int port = askServerPort(choice);
            if (choice.equalsIgnoreCase("RMI")){
                client = new RMIClient(address, port, this);
            }
            else{
                client = new SocketClient(address, port, this);
            }
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        AnsiConsole.systemUninstall();
    }

    /**
     * Asks the player's nickname
     * @return the name chosen by the player
     */
    @Override
    public String askNickname(){
        String nickname;
        AnsiConsole.out().print("Please insert your username: ");
        nickname = scanner.next();
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
        AnsiConsole.out().print("Enter the server address [" + defaultAddress + "]: ");
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
        AnsiConsole.out().print("Enter the server port [" + defaultPort + "]: ");
        port = scanner.nextLine();
        if (port.isEmpty()) {
            port = defaultPort;
        }
        return Integer.parseInt(port);
    }

    @Override
    public int selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        int lobby = -1;
        int choice;
        do {
            AnsiConsole.out().println("Select one of the following options by writing the respective number:\n[1] create a game\n[2] join a game\n[3] reconnect to a game");
            choice = scanner.nextInt();
        } while (choice > 3 || choice <= 0);
        switch (choice){
            case 1 -> AnsiConsole.out().println("Creating a new game...");
            case 2 -> {
                if(startingGamesId.isEmpty()){
                    //TODO: forse ricorsione non è la scelta migliore
                    AnsiConsole.out().println("There are no lobbies yet");
                    lobby = selectGame(startingGamesId, gamesWhitDisconnectionsId);
                }
                else{
                    lobby = getLobby(startingGamesId);
                }
            }
            case 3 -> {
                if (gamesWhitDisconnectionsId.isEmpty()) {
                    AnsiConsole.out().println("There are no lobbies with disconnected players");
                    lobby = selectGame(startingGamesId, gamesWhitDisconnectionsId);
                }
                else{
                    lobby = getLobby(gamesWhitDisconnectionsId);
                }
            }
        }
        return lobby;
    }

    public int getLobby(List<Integer> lobbyList) {
        int lobby;
        AnsiConsole.out().println("Select one of the following game's lobby by writing the respective number:");
        for (Integer i : lobbyList){
            AnsiConsole.out().println("Lobby [" + i + "]");
        }
        lobby = scanner.nextInt();
        while (!lobbyList.contains(lobby)){
            AnsiConsole.out().print("Invalid input.\nInsert the lobby number:");
            lobby = scanner.nextInt();
        }
        return lobby;
    }

    public int setLobbySize(){
        AnsiConsole.out().println("Select the lobby's capacity (min is " + Server.MIN_PLAYERS_PER_LOBBY + " and max is " + Server.MAX_PLAYERS_PER_LOBBY + " players)");
        int lobbySize = scanner.nextInt();
        while (lobbySize < Server.MIN_PLAYERS_PER_LOBBY || lobbySize > Server.MAX_PLAYERS_PER_LOBBY){
            AnsiConsole.out().println("Invalid input.\n Insert a valid number");
            lobbySize = scanner.nextInt();
        }
        return lobbySize;
    }

    @Override
    public boolean askSide(Card starterCard){
        clearConsole();
        this.printStarterCard(starterCard);
        scanner.skip("\n");
        String choice;
        do {
            AnsiConsole.out().println("Which side you want to play?\nPress [f] for front and [b] for back");
            choice = scanner.nextLine();
        } while (!choice.equalsIgnoreCase("b") && !choice.equalsIgnoreCase("f"));
        return choice.equalsIgnoreCase("f");
    }

    private void printStarterCard(Card card) {
        AnsiConsole.systemInstall();
        if(card == null){
            AnsiConsole.out().println("Starter card not existing");
        }
        else{
            String[][] starterFront = this.createPrintableCard(card);
            card.setCurrentSide();
            String[][] starterBack = this.createPrintableCard(card);
            card.setCurrentSide();

            AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
            AnsiConsole.out().print(" ".repeat(TuiCostants.COLUMN + 6 - 7));
            AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
            AnsiConsole.out().println();
            for (int i = 0; i < TuiCostants.ROW; i++) {
                for (int j = 0; j < TuiCostants.COLUMN; j++) {
                    AnsiConsole.out().print(starterFront[i][j]);
                }
                AnsiConsole.out().print("      ");
                for (int j = 0; j < TuiCostants.COLUMN; j++) {
                    AnsiConsole.out().print(starterBack[i][j]);
                }
                AnsiConsole.out().println();
            }
            AnsiConsole.out().println();

        }
        AnsiConsole.systemUninstall();
    }

    public String[][] createPrintableCardToPlace(int[] coord) {
        String[][] matCard = new String[TuiCostants.ROW][TuiCostants.COLUMN];
        PlayerBoard playerBoard = player.getBoard();
        Card card = playerBoard.getCard(coord);
        //Create a 3x5 matrix with the same color of the card //TODO: commenta bene
        for (int i = 0; i < TuiCostants.ROW; i++) {
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                matCard[i][j] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        for(CornerEnum corner : CornerEnum.values()){
            if(card.getCornerState(corner).equals(CornerState.NOT_VISIBLE)){
                //TODO: sistemare e controllare
                if(card.getCorner(corner).getSymbol().equals(Symbols.NOCORNER)){
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    Card overCard = playerBoard.getCard(new int[] {coord[0] + corner.getX(), coord[1] + corner.getY()});
                    if (overCard.getCornerSymbol(corner.getOppositePosition()).equals(Symbols.NOCORNER)){
                        matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(overCard.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else {
                        matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(Color.WHITE) + Symbols.getString(overCard.getCornerSymbol(corner.getOppositePosition())) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            else{
                matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(Color.WHITE) + Symbols.getStringBlack(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        //Add permanent resources (if present)
        int center = 4;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                center += 1;
            }
        }

        //if card gives points when placed display points and condition
        if (card.getPoints() != 0) {
            addConditionAndPoints(matCard, card);
        }

        //add cost (if present)
        if(card.getCost() != null){
            int bottomCenter = 4 - card.getCost().length / 2 + 1;
            addCost(matCard, card, bottomCenter);
        }
        return matCard;
    }

    public void addCost(String[][] matCard, Card card, int bottomCenter) {
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

    public void addConditionAndPoints(String[][] matCard, Card card) {
        int topCenter = 4;
        switch (card.getCondition()) {
            case NOTHING -> {
                if(card.getPoints() != 0){
                    matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + card.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            case CORNER -> {
                matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + card.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.CORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case ITEM -> {
                matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + card.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getRequiredItem()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
    }

    public String[][] createPrintableRetro(Color color){
        String[][] matCard = new String[TuiCostants.ROW][TuiCostants.COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < TuiCostants.ROW; i++) {
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                matCard[i][j] = Color.getBackground(color) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
        return matCard;
    }
    public String[][] createPrintableCard(Card card) {
        String[][] matCard = new String[TuiCostants.ROW][TuiCostants.COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        if (card != null) {
            for (int i = 0; i < TuiCostants.ROW; i++) {
                for (int j = 0; j < TuiCostants.COLUMN; j++) {
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

            if(card.isNotBack()){
                //aggiungi punti se ci sono
                addConditionAndPoints(matCard, card);
            }

            if(card.isNotBack()){
                //aggiungi costo se c'è
                if(card.getCost() != null){
                    int costSum = Arrays.stream(card.getCost()).reduce(0, Integer::sum);
                    int bottomCenter = 4 - costSum / 2;
                    addCost(matCard, card, bottomCenter);
                }
            }
        }
        else{
            //if the card is null create a placeholder to maintain view's layout
            for (int i = 0; i < TuiCostants.ROW; i++) {
                for (int j = 0; j < TuiCostants.COLUMN; j++) {
                    matCard[i][j] = " ";
                }
            }
        }
        return matCard;
    }

    public String[][] createPrintableAchievement(Achievement achievement) {
        String[][] matCard = new String[TuiCostants.ROW][TuiCostants.COLUMN];
        //creazione matrice vuota e bordi a destra e sinistra
        for (int i = 0; i < TuiCostants.ROW; i++) {
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                matCard[i][j] = Color.getBackground(Color.YELLOW) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        int centerPoints = 1;
        int centerObject = 6;
        switch (achievement) {
            case AchievementDiagonal ignored3 -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
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
            case AchievementL ignored2 -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
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
            case AchievementItem ignored1 -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(0)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                if (achievement.getSymbols().size() == 3) {
                    matCard[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(1)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[1][centerObject] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbols().get(2)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            case AchievementResources ignored -> {
                matCard[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[1][centerObject] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case null, default -> {
            }
        }
        return matCard;
    }

    public String[][][][] createPrintableScoreBoard(ArrayList<PlayerBean> players){
        String[][][][] matScoreBoard = new String[3][3][TuiCostants.SCOREBOARD_ROW][TuiCostants.SCOREBOARD_COLUMN];
        int[][] scoreBoardPoints = new int[][]{{8, 1}, {8, 2}, {8, 3}, {7, 4}, {7, 3}, {7, 2}, {7, 1}, {6, 0}, {6, 1}, {6, 2}, {6, 3}, {5, 4}, {5, 3}, {5, 2}, {5, 1}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {3, 4}, {3, 2}, {3, 0}, {2, 0}, {1, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 4}, {2, 4}, {1, 2}};
        int[][] scoreBoardPosition = new int[][]{{0, 0}, {2, 0}, {0, 2}, {2, 2}};

        for(int i = 0; i < TuiCostants.SCOREBOARD_ROW; i++){
            for(int j = 0; j < TuiCostants.SCOREBOARD_COLUMN; j++){
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
            matScoreBoard[1][1][scoreBoardPoints[i][0]][scoreBoardPoints[i][1]] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + i / 10 + ((i) - ((i) / 10) * 10) + TuiColors.getColor(TuiColors.ANSI_RESET);
        }

        for(int i = 0; i < players.size(); i++){
            if (players.get(i).getPionColor() != null){
                if(players.get(i).getPoints() > 29){
                    matScoreBoard[scoreBoardPosition[i][0]][scoreBoardPosition[i][1]][scoreBoardPoints[29][0]][scoreBoardPoints[29][1]] = Color.getBackground(players.get(i).getPionColor()) + (i + 1) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    matScoreBoard[scoreBoardPosition[i][0]][scoreBoardPosition[i][1]][scoreBoardPoints[players.get(i).getPoints()][0]][scoreBoardPoints[players.get(i).getPoints()][1]] = Color.getBackground(players.get(i).getPionColor()) + (i + 1) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
        }
        return matScoreBoard;
    }
    
    public void printView(){
        clearConsole();
        String [][] resourceDeck = createPrintableRetro(game.getResourceDeckRetro());
        String [][] goldDeck = createPrintableRetro(game.getGoldDeckRetro());
        String [][] commonResource1 = createPrintableCard(game.getCommonResources()[0]);
        String [][] commonResource2 = createPrintableCard(game.getCommonResources()[1]);
        String [][] commonGold1 = createPrintableCard(game.getCommonGold()[0]);
        String [][] commonGold2 = createPrintableCard(game.getCommonGold()[1]);
        String [][] commonAchievement1 = createPrintableAchievement(game.getCommonAchievement()[0]);
        String [][] commonAchievement2 = createPrintableAchievement(game.getCommonAchievement()[1]);
        String [][] hand1 = createPrintableCard(player.getHand()[0]);
        String [][] hand2 = createPrintableCard(player.getHand()[1]);
        String [][] hand3 = createPrintableCard(player.getHand()[2]);
        String [][] privateAchievement = createPrintableAchievement(player.getAchievement());
        //TODO: mettere label più specifiche
        //TODO: forse posso mettere le posizioni come costanti
        //CommonBoard label
        moveCursor(14,134);
        AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "common board" + TuiColors.getColor(TuiColors.ANSI_RESET));

        //print resource retro card
        printCardMatrix(resourceDeck, 16, 134);

        //print gold deck retro
        printCardMatrix(goldDeck,21, 134);

        //print common resources
        printCardMatrix(commonResource1, 16, 146);
        printCardMatrix(commonResource2, 16, 158);

        //print common golds
        printCardMatrix(commonGold1, 21, 146);
        printCardMatrix(commonGold2, 21, 158);

        //print common achievement
        printCardMatrix(commonAchievement1, 26, 134);
        printCardMatrix(commonAchievement2, 26, 146);

        //Player's hand and private achievement
        //Player's hand label
        moveCursor(2, 134);
        AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your hand" + TuiColors.getColor(TuiColors.ANSI_RESET));

        printCardMatrix(hand1, 4, 134);
        printCardMatrix(hand2, 4, 146);
        printCardMatrix(hand3, 4, 158);

        moveCursor(8, 134);
        //Private achievement label
        AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your personal achievement" + TuiColors.getColor(TuiColors.ANSI_RESET));

        printCardMatrix(privateAchievement, 10, 134);

        moveCursor(31, 155);
        AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "legend" + TuiColors.getColor(TuiColors.ANSI_RESET));
        moveCursor(31, 134);
        AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "symbol's count" + TuiColors.getColor(TuiColors.ANSI_RESET));
        printLabel();

        printBoard(player);

        //Empty line to divide the commands from the view
        AnsiConsole.out().println();
    }
    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {
        synchronized (lock){
            //model update
            this.player = player;
            this.game = game;
            this.players = players;
            PlayerBean playerInTurn = null;
            String username = player.getUsername();
            this.printView();
            if (!error.isEmpty()){
                moveCursor(37, 0);
                AnsiConsole.out().println(TuiColors.getColor(TuiColors.ANSI_RED) + error + TuiColors.getColor(TuiColors.ANSI_RESET));
                error = "";
            }
            if(!message.isEmpty()){
                moveCursor(37, 0);
                AnsiConsole.out().println(message);
                message = "";
            }
            if (game.getState().equals(GameState.START)){
                moveCursor(39, 0);
                AnsiConsole.out().println("Waiting for all players to end their setup");
            } else if(game.getState().equals(GameState.END)){
                AnsiConsole.out().println("\nGG to everyone");
                this.running = false;
                System.exit(1);
            }
            else {
                if (player.getState().equals(PlayerState.PLAY_CARD)) {
                    playerInTurn = player;
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Play a card");
                } else if (player.getState().equals(PlayerState.DRAW_CARD)) {
                    playerInTurn = player;
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Draw a card");
                } else {
                    for (PlayerBean p : players) {
                        if (!p.getState().equals(PlayerState.NOT_IN_TURN)) {
                            playerInTurn = p;
                        }
                    }
                    if (playerInTurn == null){
                        AnsiConsole.out().println("only one player connected, the other players have one minute to reconnect.");
                    }
                    else {
                        moveCursor(39, 0);
                        AnsiConsole.out().println(playerInTurn.getUsername() + "'s turn. Wait for your turn to play");
                    }
                }
                moveCursor(41, 0);
                AnsiConsole.out().println("Press [1] to view the score track");
                AnsiConsole.out().println("Press [2] to view a card from your hand");
                AnsiConsole.out().println("Press [3] to view a card from your board");
                AnsiConsole.out().println("Press [4] to view another player's board");
                if (playerInTurn != null) {
                    if (playerInTurn.getUsername().equals(username) && playerInTurn.getState().equals(PlayerState.PLAY_CARD)) {
                        AnsiConsole.out().println("Press [5] to place a card");
                    } else if (playerInTurn.getUsername().equals(username) && playerInTurn.getState().equals(PlayerState.DRAW_CARD)) {
                        AnsiConsole.out().println("Press [5] to draw a card");
                    }
                }
                AnsiConsole.out().println("Press [c] anytime to send a message");
            }
        }
    }
    //TODO: Refactor di questa funzione
    //TODO: altezza circa 50 -> larghezza guesso 120/150
    //TODO: togli il q per chiudere dal fondo e mettilo nei vari case
    public void handleInput(String choice){
        Card[] hand = player.getHand();
        Card[] commonResource = game.getCommonResources();
        Color resourceBack = game.getResourceDeckRetro();
        Card[] commonGold = game.getCommonGold();
        Color goldBack = game.getGoldDeckRetro();
        Scanner input = new Scanner(System.in);
        int[] coord = new int[2];
        clearConsole();
        printView();
        switch (choice) {
            case "1" -> {
                clearConsole();
                //TODO: stampa scoreTrack meglio
                ArrayList<PlayerBean> allPlayers = new ArrayList<>(players);
                allPlayers.add(player);
                String [][][][] scoreTrack = createPrintableScoreBoard(allPlayers);
                printTrack(scoreTrack);
            }
            case "2"-> {
                String a;
                do {
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Which card do you want to display? [1] [2] [3]");
                    a = input.next();
                    clearConsole();
                    this.printView();
                } while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("c"));

                if (a.equals("c")) {
                    this.printChat();
                } else {
                    String[][] frontHand = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                    hand[Integer.parseInt(a) - 1].setCurrentSide();
                    String[][] backHand = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                    hand[Integer.parseInt(a) - 1].setCurrentSide();
                    moveCursor(39, 3);
                    AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                    moveCursor(39, 19);
                    AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                    printCardMatrix(frontHand, 41, 3);
                    printCardMatrix(backHand, 41, 19);
                }
            }
            case "3" -> {
                //TODO: mettere controllo che posizione appartenga -> se no torno a menù
                moveCursor(39, 0);
                AnsiConsole.out().println("Which card do you want to display?");
                AnsiConsole.out().println("Type row number");
                String a = input.next();
                clearConsole();
                this.printView();
                while (!a.equals("c") && Integer.parseInt(a) > TuiCostants.PLAYERBOARD_DIM) {
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Retype row number");
                    a = input.next();
                    clearConsole();
                    this.printView();
                }
                if (a.equals("c")) {
                    this.printChat();
                } else {
                    coord[0] = Integer.parseInt(a);
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Type column number");
                    a = input.next();
                    clearConsole();
                    this.printView();
                    while (!a.equals("c") && Integer.parseInt(a) > TuiCostants.PLAYERBOARD_DIM) {
                        moveCursor(39, 0);
                        AnsiConsole.out().println("Retype column number");
                        a = input.next();
                        clearConsole();
                        this.printView();
                    }
                    if (a.equals("c")) {
                        this.printChat();
                    } else {
                        coord[1] = Integer.parseInt(a);
                        clearConsole();
                        this.printView();
                        this.printCardMatrix(createPrintableCard(player.getBoard().getCard(coord)), 40, 15);
                    }
                }
            }
            case "4" -> {
                String a;
                clearConsole();
                this.printView();
                do {
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Which player's do you want to display?");
                    for (int i = 0; i < players.size(); i++) {
                        AnsiConsole.out().println("\t Press " + (i + 1) + " to display " + players.get(i).getUsername() + "'s board");
                    }
                    a = input.next();
                    clearConsole();
                    this.printView();
                } while (!a.equals("c") && (Integer.parseInt(a) <= 0 || Integer.parseInt(a) > players.size()));
                    if (a.equals("c")) {
                    this.printChat();
                } else {
                    clearConsole();
                    moveCursor(1, 50);
                    AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + players.get(Integer.parseInt(a) - 1).getUsername() + "'s playerboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
                    this.printBoard(players.get(Integer.parseInt(a) - 1));
                }
            }
            case "5" -> {
                if (player.getState().equals(PlayerState.PLAY_CARD)) {
                    moveCursor(37, 0);
                    AnsiConsole.out().println("Which card do you want to place? [1] [2] [3]");
                    String a = input.next();
                    clearConsole();
                    this.printView();
                    while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("c")) {
                        AnsiConsole.out().println("Which card do you want to place? [1] [2] [3]");
                        a = input.next();
                        clearConsole();
                        this.printView();
                    }
                    if (a.equals("c")) {
                        this.printChat();
                    } else {
                        clearConsole();
                        this.printView();
                        Card cardToPlace;
                        String b;
                        do {
                            moveCursor(37, 0);
                             cardToPlace = hand[Integer.parseInt(a) - 1];
                            AnsiConsole.out().println("Which side do you want to place? [f] for front [b] for back");
                            AnsiConsole.out().println();
                            //TODO: 1162 - 1171 scrivere metodo che stampa fronte e retro
                            String[][] front = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                            hand[Integer.parseInt(a) - 1].setCurrentSide();
                            String[][] back = this.createPrintableCard(hand[Integer.parseInt(a) - 1]);
                            hand[Integer.parseInt(a) - 1].setCurrentSide();
                            moveCursor(39, 0);
                            AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "front:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            printCardMatrix(front, 39, 8);
                            moveCursor(39, 22);
                            AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "back:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            printCardMatrix(back, 39, 29);
                            b = input.next();
                        } while (!b.equals("f") && !b.equals("b") && !b.equals("c"));

                        if (b.equals("c")) {
                            this.printChat();
                        }
                        else if (b.equals("f")) {
                            clearConsole();
                            printView();
                            this.printCard(hand[Integer.parseInt(a) - 1], 37, 0);
                        }
                        else {
                            //TODO: pensare a come rimuovere printCard
                            clearConsole();
                            printView();
                            cardToPlace.setCurrentSide();
                            this.printCard(cardToPlace, 37, 0);
                        }
                        AnsiConsole.out().println("Where do you want to place it?");
                        AnsiConsole.out().println("Type row number");
                        a = input.next();
                        //TODO: cambiare condizione
                        while (!a.equals("c") && Integer.parseInt(a) > TuiCostants.PLAYERBOARD_DIM) {
                            clearConsole();
                            this.printView();
                            moveCursor(39, 0);
                            AnsiConsole.out().println("Retype row number");
                            a = input.next();
                        }
                        if (a.equals("c")) {
                            this.printChat();
                        } else {
                            clearConsole();
                            printView();
                            this.printCard(cardToPlace, 37, 0);
                            coord[0] = Integer.parseInt(a);
                            moveCursor(40, 0);
                            AnsiConsole.out().println("Type column number");
                            a = input.next();
                            while (!a.equals("c") && Integer.parseInt(a) > TuiCostants.PLAYERBOARD_DIM) {
                                clearConsole();
                                this.printView();
                                moveCursor(40, 0);
                                AnsiConsole.out().println("Retype column number");
                                a = input.next();
                            }
                            if(a.equals("c")){
                                this.printView();
                            }
                            else{
                                coord[1] = Integer.parseInt(a);
                            }
                        }
                        clearConsole();
                        client.placeCard(cardToPlace, coord);
                        AnsiConsole.out().flush();
                    }
                } else {
                    String a;
                    do {
                        moveCursor(37, 0);
                        AnsiConsole.out().println("Do you want to draw from decks or visible cards? [1] for decks [2] for visible cards");
                        a = input.next();
                    } while (!a.equals("1") && !a.equals("2") && !a.equals("c"));
                    if (a.equals("c")) {
                        this.printChat();
                    } else if (a.equals("1")) {
                        clearConsole();
                        printView();
                        moveCursor(37, 0);
                        String[][] rDeck = this.createPrintableRetro(resourceBack);
                        String[][] gDeck = this.createPrintableRetro(goldBack);
                        do {
                            AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "resource deck:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            AnsiConsole.out().print("     ");
                            AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "gold deck:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            AnsiConsole.out().println();
                            printCardMatrix(rDeck, 39, 0);
                            printCardMatrix(gDeck, 39, 21);
                            AnsiConsole.out().println();
                            AnsiConsole.out().println("Do you want to draw from resource deck or gold deck? [r] for resource [g] for gold");
                            a = input.next();
                        } while (!a.equals("r") && !a.equals("g") && !a.equals("c"));
                            if (a.equals("c")) {
                            this.printChat();
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
                        do {
                            clearConsole();
                            printView();
                            moveCursor(37, 0);
                            AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "resource cards:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            moveCursor(37, 32);
                            AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "gold cards:" + TuiColors.getColor(TuiColors.ANSI_RESET) + " ");
                            AnsiConsole.out().println();
                            printCardMatrix(r1, 39, 0);
                            printCardMatrix(r2, 39, 16);
                            printCardMatrix(g1, 39, 32);
                            printCardMatrix(g2, 39, 48);
                            AnsiConsole.out().println();
                            AnsiConsole.out().println("Which card do you want to draw? [1] for first resource [2] for second resource [3] for first gold [4] for second gold");
                            a = input.next();
                        } while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("4") && !a.equals("c"));
                        if (a.equals("c")) {
                            this.printChat();
                        } else {
                            client.drawCardFromBoard(Integer.parseInt(a));
                        }
                    }
                }
            }
            case "c" -> this.printChat();
            default -> AnsiConsole.out().print("");
        }
        moveCursor(45, 70);
        AnsiConsole.out().println("Press [q] to return to the main menu");
    }

    public void printChat(){
        Scanner input = new Scanner(System.in);
        int u;
        clearConsole();
        moveCursor(2, 2);
        AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "chat" + TuiColors.getColor(TuiColors.ANSI_RESET));
        //print chat
        moveCursor(3, 6);
        for (String message : player.getChat()){
            AnsiConsole.out().println(message);
        }
        //Select the receiver of the message
        do{
            moveCursor(37, 0);
            AnsiConsole.out().println("Who do you want to send your message to?");
            AnsiConsole.out().println("\t Press [0] to close chat");
            for (int i = 0; i < players.size(); i++) {
                AnsiConsole.out().println("\t Press " + (i + 1) + " to send the message to " + players.get(i).getUsername());
            }
            AnsiConsole.out().println("\t Press " + (players.size() + 1) + " to send a global message");
            u = input.nextInt();
        } while (u < 0 || u > players.size() + 1);
        if (u == 0){
            printView();
        }
        else {
            //Send the message
            AnsiConsole.out().println("What's the content of your message?");
            //consume the newline
            input.nextLine();
            String m = input.nextLine();
            if (u == players.size() + 1) {
                client.sendChatMessage(m);
            }
            else {
                client.sendChatMessage(players.get(u - 1).getUsername(), m);
            }
            //TODO: potrebbe dare problemi
            moveCursor(45, 50);
            AnsiConsole.out().println("Press [q] to return to the main menu");
        }
    }

    public void printTitle(){
        AnsiConsole.out().print("\n");
        AnsiConsole.out().print(TuiColors.getColor(TuiColors.ANSI_YELLOW) +
                " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗\n" +
                "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝\n" +
                "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗\n" +
                "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║\n" +
                "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║\n" +
                " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝\n" +
                "                                                                                                                    \n"
                + TuiColors.getColor(TuiColors.ANSI_RESET));
    }

    public void printTrack(String[][][][] scoretrack){
        int padding = 75;
        AnsiConsole.out().print(" ".repeat(padding));
        AnsiConsole.out().println("─".repeat(26));
        AnsiConsole.out().print(" ".repeat(padding));
        for (int i = 0; i < TuiCostants.SCOREBOARD_ROW; i++){
            for (int j = 0; j < 3; j++) {
                //print the scoretrack cell
                for (int k = 0; k < TuiCostants.SCOREBOARD_COLUMN; k++) {
                    AnsiConsole.out().print("|");
                    for (int l = 0; l < 3; l++) {
                        AnsiConsole.out().print(scoretrack[l][j][i][k]);
                    }
                }
                AnsiConsole.out().print("|");
                AnsiConsole.out().println();
                AnsiConsole.out().print(" ".repeat(padding));
            }
            AnsiConsole.out().print("─".repeat(26));
            AnsiConsole.out().println();
            AnsiConsole.out().print(" ".repeat(padding));
        }
    }

    public void printCard(Card card, int row, int column){
        String[][] mat = this.createPrintableCard(card);
        printCardMatrix(mat, row, column);
    }

    public void printCardMatrix(String[][] template, int row, int column){
        for (int i = 0; i < TuiCostants.ROW; i++) {
            moveCursor(row + i, column);
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                AnsiConsole.out().print(template[i][j]);
            }
        }
        AnsiConsole.out().print("\n");
    }

    public void printBoard(PlayerBean playerToShow){
        PlayerBoard pBoard = playerToShow.getBoard();
        int[] coord = new int[2];
        String [][] printableCard;

        //Print the grid (TuiCostants.COLUMN)
        for (int i = 0; i < TuiCostants.BOARD_DIM; i++) {
            moveCursor(2 * i + 4, 0);
            AnsiConsole.out().print((TuiCostants.BOARD_DIM/2) - i);
        }

        //Print the grid (TuiCostants.ROW)
        for (int i = 0; i < TuiCostants.BOARD_DIM; i++) {
            moveCursor(0, 8 * i + 8);
            AnsiConsole.out().print(i - (TuiCostants.BOARD_DIM/2));
        }

        //Print the placed card
        for (Integer i : pBoard.getPositionCardKeys()){
            //TODO: limitare da -7 a +7 su x e y
            coord[0] = (i / 1024) - PlayerBoard.OFFSET;
            coord[1] = (i % 1024) - PlayerBoard.OFFSET;
            printableCard = createPrintableCardToPlace(coord);
            printCardMatrix(printableCard, TuiCostants.STARTER_ROW - (coord[1] * 2), TuiCostants.STARTER_COLUMN + (coord[0] * 8));
        }
    }
    public void printLabel() {
        for (int i = 0; i < 7; i++) {
            moveCursor(TuiCostants.COMMON_ROW + i, 134);
            AnsiConsole.out().print(Symbols.getString(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + player.getBoard().getSymbolCount(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_RESET));
            moveCursor(TuiCostants.COMMON_ROW + i, 155);
            AnsiConsole.out().print(Symbols.getString(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getLongString(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_RESET));
        }
    }

    public void printAchievement(Achievement achievement){
        String[][] mat = this.createPrintableAchievement(achievement);

        for (int i = 0; i < TuiCostants.ROW; i++) {
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                AnsiConsole.out().print(mat[i][j]);
            }
            AnsiConsole.out().print("\n");
        }
        AnsiConsole.out().print("\n");
    }
    @Override
    public Achievement chooseAchievement(Achievement[] choices) {
        AnsiConsole.out().println("I)");
        this.printAchievement(choices[0]);
        AnsiConsole.out().println("II)");
        this.printAchievement(choices[1]);
        AnsiConsole.out().println("Choose one of the two Achievements");
        int choice = scanner.nextInt();
        while (choice < 1 || choice > 2){
            AnsiConsole.out().println("Wrong input.\nWrite 1 for the first achievement and 2 for the second");
            choice = scanner.nextInt();
        }
        return choices[choice - 1];
    }

    public Color chooseColor(List<Color> colors){
        AnsiConsole.out().println("Choose your color by typing the corresponding number:");
        for (int i = 0; i < colors.size(); i++){
            AnsiConsole.out().println("\t" + (1 + i) + ") " + colors.get(i));
        }
        int c = scanner.nextInt();
        while(c > colors.size() || c < 0){
            AnsiConsole.out().print("Invalid input. Retry: ");
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
           message = "\nThe winner is: " + winners.getFirst();
        }
        else{
            message = "\nThe winners are: \n";
            for (String s : winners){
                message += "\t" + s + "\n";
            }
        }
        AnsiConsole.out().println(message);
    }

    public void moveCursor(int row, int column){
        Console console = System.console();
        char escCode = 0x1B;
        console.printf("%c[%d;%df", escCode, row, column);
    }
    public static void clearConsole(){
        AnsiConsole.out().println(TuiColors.getColor(TuiColors.ANSI_CLEAR));
        AnsiConsole.out().flush();
    }
}