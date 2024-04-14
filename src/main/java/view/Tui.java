package view;

import model.*;

import java.util.Arrays;

public class Tui {
    private static final int ROW = 3;
    private static final int COLUMN = 11;
    private int[][] mat_corner = new int[][]{new int[]{0,1}, new int[]{0,9}, new int[]{2,9}, new int[]{2,1}};
    public String askNickname() {
//        System.in
        return null;
    }

    public void printTitle(){
        System.out.print("\n" +
                "\n" +
                " ▄████████  ▄██████▄  ████████▄     ▄████████ ▀████    ▐████▀      ███▄▄▄▄      ▄████████     ███     ███    █▄     ▄████████    ▄████████  ▄█        ▄█     ▄████████ \n" +
                "███    ███ ███    ███ ███   ▀███   ███    ███   ███▌   ████▀       ███▀▀▀██▄   ███    ███ ▀█████████▄ ███    ███   ███    ███   ███    ███ ███       ███    ███    ███ \n" +
                "███    █▀  ███    ███ ███    ███   ███    █▀     ███  ▐███         ███   ███   ███    ███    ▀███▀▀██ ███    ███   ███    ███   ███    ███ ███       ███▌   ███    █▀  \n" +
                "███        ███    ███ ███    ███  ▄███▄▄▄        ▀███▄███▀         ███   ███   ███    ███     ███   ▀ ███    ███  ▄███▄▄▄▄██▀   ███    ███ ███       ███▌   ███        \n" +
                "███        ███    ███ ███    ███ ▀▀███▀▀▀        ████▀██▄          ███   ███ ▀███████████     ███     ███    ███ ▀▀███▀▀▀▀▀   ▀███████████ ███       ███▌ ▀███████████ \n" +
                "███    █▄  ███    ███ ███    ███   ███    █▄    ▐███  ▀███         ███   ███   ███    ███     ███     ███    ███ ▀███████████   ███    ███ ███       ███           ███ \n" +
                "███    ███ ███    ███ ███   ▄███   ███    ███  ▄███     ███▄       ███   ███   ███    ███     ███     ███    ███   ███    ███   ███    ███ ███▌    ▄ ███     ▄█    ███ \n" +
                "████████▀   ▀██████▀  ████████▀    ██████████ ████       ███▄       ▀█   █▀    ███    █▀     ▄████▀   ████████▀    ███    ███   ███    █▀  █████▄▄██ █▀    ▄████████▀  \n" +
                "                                                                                                                   ███    ███                                         \n" +
                "\n");
    }

    public void printCard(Card card){
        //creazione matrice vuota e bordi a destra e sinistra
        String [][] mat = new String[ROW][COLUMN];
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COLUMN; j++){
                mat[i][j] = Symbols.getString(Symbols.EMPTY_SPACE);
            }
        }
        switch(card.getColor()){
            case WHITE -> {
                mat[0][0] = "⎸";
                mat[1][0] = "⎸";
                mat[2][0] = "⎸";
                mat[0][10] = "⎹";
                mat[1][10] = "⎹";
                mat[2][10] = "⎹";
            }
            case RED -> {
                mat[0][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][0] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[0][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][10] = TuiColors.getColor(TuiColors.ANSI_RED) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case BLUE -> {
                mat[0][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][0] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[0][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][10] = TuiColors.getColor(TuiColors.ANSI_BLUE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case GREEN -> {
                mat[0][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][0] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[0][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][10] = TuiColors.getColor(TuiColors.ANSI_GREEN) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case PURPLE -> {
                mat[0][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][0] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎸" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[0][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][10] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + "⎹" + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }


        //aggiungi angoli della carta
        if (card.isBack()){
            for (int i = 0; i < card.getBack().getCorners().length; i++){
                    mat[mat_corner[i][0]][mat_corner[i][1]] = Symbols.getString(card.getBack().getCorners()[i].getSymbol());
            }
        }
        else{
            for (int i = 0; i < card.getCorners().length; i++){
                mat[mat_corner[i][0]][mat_corner[i][1]] = Symbols.getString(card.getCorners()[i].getSymbol());
            }
        }

        //aggiungi simboli al centro se ci sono
        int center = 5;
        if (card.getSymbols() != null) {
            for (int i = 0; i < card.getSymbols().size(); i++) {
                mat[1][center] = Symbols.getString(card.getSymbols().get(i));
                center += center;
            }
        }

        //aggiungi punti se ci sono
        int topCenter = 5;
        if(card.getPoints() != 0){
            switch(card.getCondition()){
                case NOTHING -> mat[0][topCenter] = String.valueOf(card.getPoints());
                case CORNER -> {
                    mat[0][topCenter - 1] = String.valueOf(card.getPoints());
                    mat[0][topCenter] = " | ";
                    mat[0][topCenter + 1] = Symbols.getString(Symbols.CORNER);
                }
                case ITEM -> {
                    mat[0][topCenter - 1] = String.valueOf(card.getPoints());
                    mat[0][topCenter] = " | ";
                    mat[0][topCenter + 1] = Symbols.getString(card.getRequiredItem());
                }
            }
        }

        //aggiungi costo se c'è
        int costSum = Arrays.stream(card.getCost()).sum();
        int bottomCenter = 5 - costSum / 2;
        for(int i = 0; i < card.getCost().length; i++){
            for(int j = 0; j < card.getCost()[i]; j++){
                switch (i){
                    case 0 -> mat[2][bottomCenter] = Symbols.getString(Symbols.FUNGI);
                    case 1 -> mat[2][bottomCenter] = Symbols.getString(Symbols.PLANT);
                    case 2 -> mat[2][bottomCenter] = Symbols.getString(Symbols.ANIMAL);
                    case 3 -> mat[2][bottomCenter] = Symbols.getString(Symbols.INSECT);

                }
                bottomCenter += 1;
            }
        }

        //stampa i bordi superiori e inferiori e la matrice
        switch(card.getColor()){
            case WHITE -> System.out.println("＿＿＿＿＿＿＿＿＿");
            case RED -> System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + "＿＿＿＿＿＿＿＿＿" + TuiColors.getColor(TuiColors.ANSI_RESET));
            case BLUE -> System.out.println(TuiColors.getColor(TuiColors.ANSI_BLUE) + "＿＿＿＿＿＿＿＿＿" + TuiColors.getColor(TuiColors.ANSI_RESET));
            case GREEN -> System.out.println(TuiColors.getColor(TuiColors.ANSI_GREEN) + "＿＿＿＿＿＿＿＿＿" + TuiColors.getColor(TuiColors.ANSI_RESET));
            case PURPLE -> System.out.println(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "＿＿＿＿＿＿＿＿＿" + TuiColors.getColor(TuiColors.ANSI_RESET));
        }
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COLUMN; j++){
                System.out.print(mat[i][j]);
            }
            System.out.print("\n");
        }
        switch(card.getColor()){
            case WHITE -> System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
            case RED -> System.out.println(TuiColors.getColor(TuiColors.ANSI_RED) + "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾" + TuiColors.getColor(TuiColors.ANSI_RESET));
            case BLUE -> System.out.println(TuiColors.getColor(TuiColors.ANSI_BLUE) + "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾" + TuiColors.getColor(TuiColors.ANSI_RESET));
            case GREEN -> System.out.println(TuiColors.getColor(TuiColors.ANSI_GREEN) + "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾" + TuiColors.getColor(TuiColors.ANSI_RESET));
            case PURPLE -> System.out.println(TuiColors.getColor(TuiColors.ANSI_PURPLE) + "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾" + TuiColors.getColor(TuiColors.ANSI_RESET));
        }


    }

    public void printCard(Achievement achievement) {
        //creazione matrice vuota e bordi a destra e sinistra
        String[][] mat = new String[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                mat[i][j] = Symbols.getString(Symbols.EMPTY_SPACE);
            }
        }
        mat[0][0] = "⎸";
        mat[1][0] = "⎸";
        mat[2][0] = "⎸";
        mat[0][10] = "⎹";
        mat[1][10] = "⎹";
        mat[2][10] = "⎹";

        int centerPoints = 2;
        int centerObject = 7;
        switch (achievement) {
            case AchievementDiagonal achievementDiagonal -> {
                mat[0][centerPoints] = String.valueOf(achievement.getPoints());
                switch(achievement.getColor()){
                    case RED -> {
                        mat[0][centerObject - 1] = TuiColors.getColor(TuiColors.ANSI_RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        mat[0][centerObject - 1] = TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        mat[0][centerObject - 1] = TuiColors.getColor(TuiColors.ANSI_GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        mat[0][centerObject - 1] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }

            }
            case AchievementL achievementL -> {
                mat[0][centerPoints] = String.valueOf(achievement.getPoints());
                switch(achievement.getColor()){
                    case RED -> {
                        mat[0][centerObject] = TuiColors.getColor(TuiColors.ANSI_RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        mat[0][centerObject] = TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_RED) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        mat[0][centerObject] = TuiColors.getColor(TuiColors.ANSI_GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_GREEN) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        mat[0][centerObject] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = TuiColors.getColor(TuiColors.ANSI_PURPLE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            case AchievementItem achievementItem -> {
                mat[0][centerPoints] = String.valueOf(achievement.getPoints());
                mat[2][centerObject - 1] = Symbols.getString(achievement.getSymbols().get(0));
                mat[2][centerObject + 1] = Symbols.getString(achievement.getSymbols().get(1));
                if (achievement.getSymbols().size() == 3) {
                    mat[1][centerObject] = Symbols.getString(achievement.getSymbols().get(2));
                }
            }
            case AchievementResources achievementResources -> {
                mat[0][centerPoints] = String.valueOf(achievement.getPoints());
                mat[1][centerObject] = Symbols.getString(achievement.getSymbol());
                mat[2][centerObject - 1] = Symbols.getString(achievement.getSymbol());
                mat[2][centerObject + 1] = Symbols.getString(achievement.getSymbol());
            }
            case null, default -> {
            }
        }
        //stampa i bordi superiori e inferiori e la matrice
        System.out.println("＿＿＿＿＿＿＿＿＿");
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                System.out.print(mat[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
    }
}