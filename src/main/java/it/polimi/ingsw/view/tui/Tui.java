package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.Ui;
import org.fusesource.jansi.AnsiConsole;

import java.io.Console;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Tui implements Ui {
    private ClientInterface client;
    private String error = "";
    private String message = "";
    private PlayerBean player;
    private GameBean game;
    private ArrayList<PlayerBean> players;
    private Scanner scanner;
    private final Object lock = new Object();
    private boolean running;
    private Thread inputThread;
    private boolean end = false;

    public Tui(){
        AnsiConsole.systemInstall();
        scanner = new Scanner(System.in);
        spawnThread();
        this.running = true;
    }

    public void spawnThread(){
        inputThread = new Thread(() -> {
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
    }

    public void handleReconnection(){
        this.inputThread.start();
    }

    /**
     * Starts the TUI instance and begins the login phase
     */
    public void run(){
        try {
            this.printTitle();
            String choice;
            AnsiConsole.out().println("Please specify the following settings. The default value is shown between brackets.");
            String address = askServerAddress();
            AnsiConsole.out().println("Choose your connection method: Write:\n\t[1] RMI\n\t[2] Socket");
            choice = scanner.nextLine();
            while(!choice.equalsIgnoreCase("RMI") && !choice.equalsIgnoreCase("Socket")){
                AnsiConsole.out().println("Wrong input.\nPlease, choose your connection method. Write:\n\t[1] RMI\n\t[2]Socket");
                choice = scanner.nextLine();
            }
            int port = askServerPort(choice);
            if (choice.equalsIgnoreCase("RMI")){
                client = new RMIClient(address, port, this);
                client.login();
            }
            else{
                client = new SocketClient(address, port, this);
                client.login();
            }
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Asks the player's nickname
     */
    @Override
    public void askNickname(){
        String nickname;
        AnsiConsole.out().print("Please insert your username: ");
        nickname = scanner.next();
        client.setNickname(nickname);
    }

    /**
     * Asks the server address the player wants to connect to
     * @return the chosen server address
     */
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
        System.setProperty("java.rmi.server.hostname", host);
        return host;
    }

    /**
     * Asks the player the port of the server he wants to connect to
     * @return the chosen server port
     */
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
    public void selectGame(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        int lobby = -2;
        String choice;
        do {
            AnsiConsole.out().println("Select one of the following options by writing the respective number:\n[1] create a game\n[2] join a game\n[3] reconnect to a game");
            choice = scanner.next();
        } while (!choice.equals("3") && !choice.equals("2") && !choice.equals("1"));
        switch (choice){
            case "1" -> {
                AnsiConsole.out().println("Creating a new game...");
                lobby = -1;
            }
            case "2" -> {
                if(startingGamesId.isEmpty()){
                    AnsiConsole.out().println("There are no lobbies yet");
                }
                else{
                    lobby = getLobby(startingGamesId);
                }
            }
            case "3" -> {
                if (gamesWithDisconnectionsId.isEmpty()) {
                    AnsiConsole.out().println("There are no lobbies with disconnected players");
                }
                else{
                    lobby = getLobby(gamesWithDisconnectionsId);
                }
            }
        }
        client.setOnConnectionAction(lobby, startingGamesId, gamesWithDisconnectionsId);
    }

    public int getLobby(List<Integer> lobbyList) {
        int lobby;
        AnsiConsole.out().println("Select one of the following game's lobby by writing the respective number:");
        for (Integer i : lobbyList){
            AnsiConsole.out().println("Lobby [" + i + "]");
        }
        do{
            try{
                lobby = scanner.nextInt();
            } catch (NumberFormatException | InputMismatchException e){
                AnsiConsole.out().println("Invalid input.\n Insert a valid number");
                lobby = -1;
                scanner = new Scanner(System.in);
            }
        } while (!lobbyList.contains(lobby));
        return lobby;
    }


    @Override
    public void askLobbySize(){
        int lobbySize;
        AnsiConsole.out().println("Select the lobby's capacity (min is " + Server.MIN_PLAYERS_PER_LOBBY + " and max is " + Server.MAX_PLAYERS_PER_LOBBY + " players)");
        do{
            try{
                lobbySize = scanner.nextInt();
            } catch (NumberFormatException | InputMismatchException e){
                    AnsiConsole.out().println("Invalid input.\n Insert a valid number");
                    lobbySize = 0;
                    scanner = new Scanner(System.in);
            }
        } while (lobbySize < Server.MIN_PLAYERS_PER_LOBBY || lobbySize > Server.MAX_PLAYERS_PER_LOBBY);
        client.setLobbySize(lobbySize);
    }

    @Override
    public void askSide(Card starterCard){
        clearConsole();
        this.printStarterCard(starterCard);
        String choice;
        do {
            AnsiConsole.out().println("Which side you want to play?\nPress [f] for front and [b] for back");
            choice = scanner.next();
        } while (!choice.equalsIgnoreCase("b") && !choice.equalsIgnoreCase("f"));
        boolean side = choice.equalsIgnoreCase("f");
        client.placeStarterCard(side, starterCard);
    }

    private void printStarterCard(Card card) {
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
    }

    public String[][] createPrintableCardToPlace(int[] coord, PlayerBean p) {
        String[][] matCard = new String[TuiCostants.ROW][TuiCostants.COLUMN];
        PlayerBoard playerBoard = p.getBoard();
        Card card = playerBoard.getCard(coord);
        //Create a 3x5 rectangle coloured like the card
        for (int i = 0; i < TuiCostants.ROW; i++) {
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                matCard[i][j] = Color.getBackground(card.getColor()) + " " + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        for(CornerEnum corner : CornerEnum.values()){
            if(card.getCornerState(corner).equals(CornerState.NOT_VISIBLE)){
                if(card.getCorner(corner).getSymbol().equals(Symbols.NOCORNER)){
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.NOCORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    Card overCard = playerBoard.getCard(new int[] {coord[0] + corner.getX(), coord[1] + corner.getY()});
                    if (overCard.getCornerSymbol(corner.getOppositePosition()).equals(Symbols.NOCORNER)){
                        matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(overCard.getColor()) + Symbols.getString(Symbols.NOCORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else {
                        matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = TuiColors.getColor(TuiColors.ANSI_GRAY) + Symbols.getString(overCard.getCornerSymbol(corner.getOppositePosition())) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            else{
                matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = TuiColors.getColor(TuiColors.ANSI_GRAY) + Symbols.getString(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        //Add permanent resources (if present)
        int center = 4;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getString(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
                    case 0 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.FUNGI) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    case 1 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.PLANT) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    case 2 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.ANIMAL) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    case 3 -> matCard[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.INSECT) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
                matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.CORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case ITEM -> {
                matCard[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + card.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getString(card.getRequiredItem()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }
    }

    public String[][] createPrintableRetro(Color color){
        String[][] matCard = new String[TuiCostants.ROW][TuiCostants.COLUMN];
        for (int i = 0; i < TuiCostants.ROW; i++) {
            for (int j = 0; j < TuiCostants.COLUMN; j++) {
                matCard[i][j] = Color.getBackground(color) + " " + TuiColors.getColor(TuiColors.ANSI_RESET);
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
                    matCard[i][j] = Color.getBackground(card.getColor()) + " " + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            //aggiungi angoli della carta
            for(CornerEnum corner : CornerEnum.values()){
                if(card.getCorner(corner).getSymbol().equals(Symbols.NOCORNER)){
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = Color.getBackground(card.getColor()) + Symbols.getString(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else{
                    matCard[2*(corner.getY() - 1)/(-2)][8*(corner.getX() + 1)/2] = TuiColors.getColor(TuiColors.ANSI_GRAY) + Symbols.getString(card.getCorner(corner).getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }

            //aggiungi simboli al centro se ci sono
            int center = 4;
            if (card.getSymbols() != null) {
                if(card.getSymbols().size() <= 2){
                    for (int i = 0; i < card.getSymbols().size(); i++) {
                        matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getString(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        center += 1;
                    }
                }
                else{
                    center -= 1;
                    for (int i = 0; i < card.getSymbols().size(); i++) {
                        matCard[1][center] = Color.getBackground(card.getColor()) + Symbols.getString(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
                matCard[i][j] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + " " + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        int centerPoints = 1;
        int centerObject = 6;
        switch (achievement) {
            case AchievementDiagonal ignored3 -> {
                matCard[0][centerPoints] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        matCard[0][centerObject + 1] = Color.getBackground(Color.RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject - 1] = Color.getBackground(Color.RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        matCard[0][centerObject + 1] = Color.getBackground(Color.BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject - 1] = Color.getBackground(Color.BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.PURPLE)+ Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }

            }
            case AchievementL ignored2 -> {
                matCard[0][centerPoints] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        matCard[0][centerObject] = Color.getBackground(Color.RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject + 1] = Color.getBackground(Color.GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        matCard[0][centerObject + 1] = Color.getBackground(Color.RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject] = Color.getBackground(Color.BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        matCard[0][centerObject] = Color.getBackground(Color.GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject - 1] = Color.getBackground(Color.PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        matCard[0][centerObject - 1] = Color.getBackground(Color.BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[1][centerObject] = Color.getBackground(Color.PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        matCard[2][centerObject] = Color.getBackground(Color.PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            case AchievementItem ignored1 -> {
                matCard[0][centerPoints] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbols().getFirst()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                if (achievement.getSymbols().size() < 3) {
                    matCard[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbols().getFirst()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else {
                    matCard[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbols().get(1)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    matCard[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbols().get(2)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            case AchievementResources ignored -> {
                matCard[0][centerPoints] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + achievement.getPoints() + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject - 1] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                matCard[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_YELLOW) + Symbols.getString(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
                            matScoreBoard[s][t][i][j] = "  ";
                        }
                        else{
                            matScoreBoard[s][t][i][j] = " ";
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
            matScoreBoard[1][1][scoreBoardPoints[i][0]][scoreBoardPoints[i][1]] = TuiColors.getColor(TuiColors.ANSI_GOLD) + i / 10 + ((i) - ((i) / 10) * 10) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
        //TODO: DOVREI METTERE LABEL PER COMMON GOLD E PER COMMON RESOURCE?
        //CommonBoard label
        moveCursor(14,TuiCostants.FIRST_COMMONBOARD_COLUMN);
        AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "common board" + TuiColors.getColor(TuiColors.ANSI_RESET));

        //print resource retro card
        printCardMatrix(resourceDeck, 16, TuiCostants.FIRST_COMMONBOARD_COLUMN);

        //print gold deck retro
        printCardMatrix(goldDeck,21, TuiCostants.FIRST_COMMONBOARD_COLUMN);

        //print common resources
        printCardMatrix(commonResource1, 16, TuiCostants.SECOND_COMMONBOARD_COLUMN);
        printCardMatrix(commonResource2, 16, TuiCostants.THIRD_COMMONBOARD_COLUMN);

        //print common golds
        printCardMatrix(commonGold1, 21, TuiCostants.SECOND_COMMONBOARD_COLUMN);
        printCardMatrix(commonGold2, 21, TuiCostants.THIRD_COMMONBOARD_COLUMN);

        //print common achievement
        printCardMatrix(commonAchievement1, 26, TuiCostants.FIRST_COMMONBOARD_COLUMN);
        printCardMatrix(commonAchievement2, 26, TuiCostants.SECOND_COMMONBOARD_COLUMN);

        //Player's hand and private achievement
        //Player's hand label
        moveCursor(2, TuiCostants.FIRST_COMMONBOARD_COLUMN);
        AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your hand" + TuiColors.getColor(TuiColors.ANSI_RESET));

        printCardMatrix(hand1, 4, TuiCostants.FIRST_COMMONBOARD_COLUMN);
        printCardMatrix(hand2, 4, TuiCostants.SECOND_COMMONBOARD_COLUMN);
        printCardMatrix(hand3, 4, TuiCostants.THIRD_COMMONBOARD_COLUMN);

        moveCursor(8, TuiCostants.FIRST_COMMONBOARD_COLUMN);
        //Private achievement label
        AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "your personal achievement" + TuiColors.getColor(TuiColors.ANSI_RESET));

        printCardMatrix(privateAchievement, 10, TuiCostants.FIRST_COMMONBOARD_COLUMN);

        moveCursor(31, 155);
        AnsiConsole.out().print(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "legend" + TuiColors.getColor(TuiColors.ANSI_RESET));
        moveCursor(31, TuiCostants.FIRST_COMMONBOARD_COLUMN);
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
            if(!message.isEmpty() && !end){
                moveCursor(37, 0);
                AnsiConsole.out().println(message);
                message = "";
            }
            if (game.getState().equals(GameState.START)){
                moveCursor(39, 0);
                AnsiConsole.out().println("Waiting for all players to end their setup");
            } else if(game.getState().equals(GameState.END)){
                clearConsole();
                ArrayList<PlayerBean> allPlayers = new ArrayList<>(players);
                allPlayers.add(player);
                String [][][][] scoreTrack = createPrintableScoreBoard(allPlayers);
                printTrack(scoreTrack);
                moveCursor(38, 0);
                AnsiConsole.out().println(message);
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
                    moveCursor(39, 0);
                    if (playerInTurn == null){
                        AnsiConsole.out().println("only one player connected, the other players have two minutes to reconnect.");
                    }
                    else {
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
                String a;
                moveCursor(39, 0);
                AnsiConsole.out().println("Which card do you want to display?");
                AnsiConsole.out().println("Type row number");
                a = input.next();
                clearConsole();
                this.printView();
                if (a.equals("c")) {
                    this.printChat();
                }
                else {
                    do{
                        try {
                            coord[1] = Integer.parseInt(a);
                            break;
                        } catch (NumberFormatException e) {
                            moveCursor(39, 0);
                            AnsiConsole.out().println("Retype row number");
                            a = input.next();
                            clearConsole();
                            this.printView();
                        }
                    } while (true);
                    moveCursor(39, 0);
                    AnsiConsole.out().println("Type column number");
                    a = input.next();
                    clearConsole();
                    this.printView();
                    if (a.equals("c")) {
                        this.printChat();
                    } else {
                        do{
                            try {
                                coord[0] = Integer.parseInt(a);
                                break;
                            } catch (NumberFormatException e) {
                                moveCursor(39, 0);
                                AnsiConsole.out().println("Retype column number");
                                a = input.next();
                                clearConsole();
                                this.printView();
                            }
                        } while (true);
                        if (player.getBoard().getCard(coord) != null){
                            clearConsole();
                            this.printView();
                            this.printCardMatrix(createPrintableCard(player.getBoard().getCard(coord)), 40, 15);
                        }
                        else{
                            error = "There is no card at " + coord[0] + " " + coord[1];
                        }
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
                } while (!a.equals("c") && (Integer.parseInt(a) <= 0 || Integer.parseInt(a) > players.size()));
                if (a.equals("c")) {
                    this.printChat();
                } else {
                    clearConsole();
                    moveCursor(39, 50);
                    AnsiConsole.out().println(Color.getBackground(Color.ORANGE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + players.get(Integer.parseInt(a) - 1).getUsername() + "'s playerboard" + TuiColors.getColor(TuiColors.ANSI_RESET));
                    this.printBoard(players.get(Integer.parseInt(a) - 1));
                }
            }
            case "5" -> {
                if (player.getState().equals(PlayerState.PLAY_CARD)) {
                    String a;
                    do {
                        clearConsole();
                        this.printView();
                        moveCursor(37, 0);
                        AnsiConsole.out().println("Which card do you want to place? [1] [2] [3]");
                        a = input.next();
                    } while (!a.equals("1") && !a.equals("2") && !a.equals("3") && !a.equals("c"));
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
                        else if (b.equals("b")) {
                            cardToPlace.setCurrentSide();
                        }
                        clearConsole();
                        printView();
                        this.printCardMatrix(createPrintableCard(cardToPlace), 37, 0);
                        AnsiConsole.out().println("Where do you want to place it?");
                        AnsiConsole.out().println("Type row number");
                        a = input.next();
                        if (a.equals("c")) {
                            this.printChat();
                        } else {
                            clearConsole();
                            printView();
                            this.printCardMatrix(createPrintableCard(cardToPlace), 37, 0);
                            do{
                                try {
                                    coord[1] = Integer.parseInt(a);
                                    break;
                                } catch (NumberFormatException e) {
                                    clearConsole();
                                    this.printView();
                                    moveCursor(39, 0);
                                    AnsiConsole.out().println("Retype row number");
                                    a = input.next();
                                }
                            } while(true);
                            moveCursor(40, 0);
                            AnsiConsole.out().println("Type column number");
                            a = input.next();
                            if(a.equals("c")){
                                this.printChat();
                            }
                            else{
                                do{
                                    try {
                                        coord[0] = Integer.parseInt(a);
                                        break;
                                    } catch (NumberFormatException e) {
                                        clearConsole();
                                        this.printView();
                                        moveCursor(40, 0);
                                        AnsiConsole.out().println("Retype column number");
                                        a = input.next();
                                    }
                                } while(true);
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
            AnsiConsole.out().println("Select the addressee of the message");
            AnsiConsole.out().println("\t Press [0] to close chat");
            for (int i = 0; i < players.size(); i++) {
                AnsiConsole.out().println("\t Press " + (i + 1) + " to send the message to " + players.get(i).getUsername());
            }
            AnsiConsole.out().println("\t Press " + (players.size() + 1) + " to send a global message");
            String addressee = input.next();
            try {
                u = Integer.parseInt(addressee);
            } catch (NumberFormatException e) {
                u = -1;
            }
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
            moveCursor(45, 50);
            AnsiConsole.out().println("Press [q] to return to the main menu");
        }
    }

    public void printTitle(){
        AnsiConsole.out().print("\n");
        AnsiConsole.out().print(TuiColors.getColor(TuiColors.ANSI_GOLD) +
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

        //Print the grid (COLUMN)
        for (int i = 0; i < TuiCostants.BOARD_DIM; i++) {
            moveCursor(2 * i + 4, 0);
            AnsiConsole.out().print((TuiCostants.BOARD_DIM/2) - i);
        }

        //Print the grid (ROW)
        for (int i = 0; i < TuiCostants.BOARD_DIM; i++) {
            moveCursor(0, 8 * i + 8);
            AnsiConsole.out().print(i - (TuiCostants.BOARD_DIM/2));
        }

        //Print the placed card
        for (Integer i : pBoard.getPositionCardKeys()){
            coord[0] = (i / 1024) - PlayerBoard.OFFSET;
            coord[1] = (i % 1024) - PlayerBoard.OFFSET;
            if (coord[0] < 8 && coord[0] > -8  && coord[1] < 8 && coord[1] > -8){
                printableCard = createPrintableCardToPlace(coord, playerToShow);
                printCardMatrix(printableCard, TuiCostants.STARTER_ROW - (coord[1] * 2), TuiCostants.STARTER_COLUMN + (coord[0] * 8));
            }
        }
    }
    public void printLabel() {
        for (int i = 0; i < 7; i++) {
            moveCursor(TuiCostants.COMMON_ROW + i, TuiCostants.FIRST_COMMONBOARD_COLUMN);
            AnsiConsole.out().print(Symbols.getString(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + player.getBoard().getSymbolCount(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_RESET));
            moveCursor(TuiCostants.COMMON_ROW + i, 155);
            AnsiConsole.out().print(Symbols.getString(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_WHITE) + " -> " + Symbols.getNameString(Symbols.values()[i]) + TuiColors.getColor(TuiColors.ANSI_RESET));
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
    public void askAchievement(Achievement[] choices) {
        AnsiConsole.out().println("[1]");
        this.printAchievement(choices[0]);
        AnsiConsole.out().println("[2]");
        this.printAchievement(choices[1]);
        AnsiConsole.out().println("Choose one of the two Achievements");
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (NumberFormatException | InputMismatchException e){
            choice = 0;
            scanner = new Scanner(System.in);
        }
        while (choice < 1 || choice > 2){
            AnsiConsole.out().println("Wrong input.\nWrite [1] for the first achievement and [2] for the second");
            try {
                choice = scanner.nextInt();
            } catch (NumberFormatException | InputMismatchException e){
                choice = 0;
                scanner = new Scanner(System.in);
            }
        }
        client.chooseAchievement(choices[choice - 1]);
    }

    @Override
    public void askColor(List<Color> colors){
        AnsiConsole.out().println("Choose your color by typing the corresponding number:");
        for (int i = 0; i < colors.size(); i++){
            AnsiConsole.out().println("\t" + (1 + i) + ") " + colors.get(i));
        }
        int c;
        try {
            c = scanner.nextInt();
        } catch (NumberFormatException | InputMismatchException e){
            c = -1;
            scanner = new Scanner(System.in);
        }
        while(c > colors.size() || c < 0){
            AnsiConsole.out().print("Invalid input. Retry: ");
            try {
                c = scanner.nextInt();
            } catch (NumberFormatException | InputMismatchException e){
                c = -1;
                scanner = new Scanner(System.in);
            }
        }
        inputThread.start();
        client.chooseColor(colors.get(c - 1));
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

    @Override
    public void declareWinners(ArrayList<String> winners){
        this.end = true;
        StringBuilder sb = new StringBuilder();
        if (winners.size() == 1){
            message = "\nThe winner is: " + winners.getFirst();
        }
        else{
            message = "\nThe winners are: \n";
            for (String s : winners){
                message = sb.append("\t").append(s).append("\n").toString();
            }
        }
        this.printViewWithCommands(player, game, players);
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