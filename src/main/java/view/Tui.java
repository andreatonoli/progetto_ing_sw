package view;

import model.*;

import java.util.Arrays;

public class Tui {
    private static final int ROW = 3;
    private static final int COLUMN = 9;
    private static final int PLAYERBOARD_DIM = 11;
    private String topBorder = "＿＿＿＿＿＿＿";
    private String bottomBorder = "‾‾‾‾‾‾‾‾‾‾‾";
    private String topBorderLong = "＿＿＿＿";
    private String bottomBorderLong = "‾‾‾‾‾‾‾";
    private String[][] matPlayerBoard = new String[PLAYERBOARD_DIM][PLAYERBOARD_DIM];
    private String[][][][] mat = new String[2][2][PLAYERBOARD_DIM][PLAYERBOARD_DIM];
    private String padding = "  ";

    public String askNickname() {
//        System.in
        return null;
    }

    public void printTitle(){
        System.out.print(" ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗\n" +
                "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝\n" +
                "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗\n" +
                "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║\n" +
                "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║\n" +
                " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝\n" +
                "                                                                                                                    \n" +
                "\n");
    }

    public void printCard(Player player, Card card) {
        if (card == null) {
            System.out.println("Card not exsisting at this coordinates");
        }
        else {
            //creazione matrice vuota e bordi a destra e sinistra
            String[][] mat = new String[ROW][COLUMN];
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    mat[i][j] = Color.getBackground(card.getColor()) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
            int[] coordCover = player.getPlayerBoard().getCardCoordinates(card);
            if (card.isBack()) {
                if(card.getBack().getCorners()[0].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] -= 1;
                    coordCover[1] += 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED){
                        mat[0][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[0][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[0][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }

                if(card.getBack().getCorners()[1].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] += 1;
                    coordCover[1] += 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED){
                        mat[0][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[0][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[0][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }

                if(card.getBack().getCorners()[2].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] += 1;
                    coordCover[1] -= 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED){
                        mat[2][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[2][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[2][8] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }

                if(card.getBack().getCorners()[3].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] -= 1;
                    coordCover[1] -= 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED){
                        mat[2][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[2][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[2][0] = Color.getBackground(card.getBack().getColor()) + Symbols.getStringBlack(card.getBack().getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else {
                if(card.getCorners()[0].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] -= 1;
                    coordCover[1] += 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED){
                        mat[0][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[0][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[0][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }

                if(card.getCorners()[1].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] += 1;
                    coordCover[1] += 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED){
                        mat[0][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[0][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[0][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }

                if(card.getCorners()[2].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] += 1;
                    coordCover[1] -= 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED){
                        mat[2][8] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[2][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    coordCover = player.getPlayerBoard().getCardCoordinates(card);
                }
                else{
                    mat[2][8] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }

                if(card.getCorners()[3].getState() == CornerState.NOT_VISIBLE){
                    coordCover[0] -= 1;
                    coordCover[1] -= 1;
                    if(player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED){
                        mat[2][0] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    else{
                        mat[2][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
                else{
                    mat[2][0] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }

            //aggiungi simboli al centro se ci sono
            int center = 4;
            if (card.getSymbols() != null) {
                for (int i = 0; i < card.getSymbols().size(); i++) {
                    mat[1][center] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getSymbols().get(i)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    center += center;
                }
            }

            //aggiungi punti se ci sono
            int topCenter = 4;
            if (card.getPoints() != 0) {
                switch (card.getCondition()) {
                    case NOTHING -> mat[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    case CORNER -> {
                        mat[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.CORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case ITEM -> {
                        mat[0][topCenter - 1] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(card.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[0][topCenter] = Color.getBackground(card.getColor()) + TuiColors.getColor(TuiColors.ANSI_BLACK) + "|" + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[0][topCenter + 1] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getRequiredItem()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }

            //aggiungi costo se c'è
            int costSum = Arrays.stream(card.getCost()).sum();
            int bottomCenter = 4 - costSum / 2;
            for (int i = 0; i < card.getCost().length; i++) {
                for (int j = 0; j < card.getCost()[i]; j++) {
                    switch (i) {
                        case 0 -> mat[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.FUNGI) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 1 -> mat[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.PLANT) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 2 -> mat[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.ANIMAL) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        case 3 -> mat[2][bottomCenter] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.INSECT) + TuiColors.getColor(TuiColors.ANSI_RESET);

                    }
                    bottomCenter += 1;
                }
            }

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

    public void printCard(Achievement achievement) {
        //creazione matrice vuota e bordi a destra e sinistra
        String[][] mat = new String[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                mat[i][j] = Color.getBackground(Color.YELLOW) + Symbols.getString(Symbols.EMPTY_SPACE) + TuiColors.getColor(TuiColors.ANSI_RESET);
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
                mat[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        mat[0][centerObject - 1] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        mat[0][centerObject - 1] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        mat[0][centerObject - 1] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLUE) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        mat[0][centerObject - 1] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }

            }
            case AchievementL achievementL -> {
                mat[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                switch(achievement.getColor()){
                    case RED -> {
                        mat[0][centerObject] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case BLUE -> {
                        mat[0][centerObject] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.RED) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case GREEN -> {
                        mat[0][centerObject] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.GREEN) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                    case PURPLE -> {
                        mat[0][centerObject] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[1][centerObject] = Color.getBackground(Color.PURPLE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                        mat[2][centerObject + 1] = Color.getBackground(Color.BLUE) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getString(Symbols.CARD) + TuiColors.getColor(TuiColors.ANSI_RESET);
                    }
                }
            }
            case AchievementItem achievementItem -> {
                mat[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getStringBlack(achievement.getSymbols().get(0)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getStringBlack(achievement.getSymbols().get(1)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                if (achievement.getSymbols().size() == 3) {
                    mat[1][centerObject] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + Symbols.getStringBlack(achievement.getSymbols().get(2)) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            case AchievementResources achievementResources -> {
                mat[0][centerPoints] = Color.getBackground(Color.YELLOW) + TuiColors.getColor(TuiColors.ANSI_BLACK) + String.valueOf(achievement.getPoints()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[1][centerObject] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][centerObject - 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                mat[2][centerObject + 1] = Color.getBackground(Color.YELLOW) + Symbols.getStringBlack(achievement.getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            case null, default -> {
            }
        }
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

    //non stampa carta verde
    public void printCardFromPlayerboard(Player player, int[] coord){
        int[] c = coord;
        c[0] = PLAYERBOARD_DIM/2 - coord[0] + 1;
        c[1] = coord[1] - PLAYERBOARD_DIM/2 - 1;
        this.printCard(player, player.getPlayerBoard().getCard(c));
    }

    public void printPlayerBoard(Player player){
        int[] coord;

        for(int i = 0; i < PLAYERBOARD_DIM; i++){
            for(int j = 0; j < PLAYERBOARD_DIM; j++){
                if(i == 0){
                    mat[0][0][i][j] = padding + String.valueOf((j + 1)/10);
                    mat[0][1][i][j] = String.valueOf((j + 1) - ((j + 1)/10)*10) + padding;
                    mat[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    mat[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
                else if (j == 0) {
                    mat[0][0][i][j] = padding + String.valueOf((i + 1)/10);
                    mat[0][1][i][j] = String.valueOf((i + 1) - ((i + 1)/10)*10) + padding;
                    mat[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    mat[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
                else{
                    mat[0][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    mat[0][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    mat[1][0][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                    mat[1][1][i][j] = Symbols.getString(Symbols.EMPTY_SPACE) + padding;
                }
            }
        }

        for (Integer i : player.getPlayerBoard().getPositionCardKeys()) {
            coord = player.getPlayerBoard().getCardCoordinates(player.getPlayerBoard().getCardPositon().get(i));
            int[] coordCover = new int[2];
            Card card = player.getPlayerBoard().getCard(coord);
            matPlayerBoard[PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            if (card.getCorners()[0].getState() == CornerState.NOT_VISIBLE) {
                coordCover[0] = coord[0] - 1;
                coordCover[1] = coord[1] + 1;
                if (player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BR).getState() == CornerState.OCCUPIED) {
                    mat[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[2].getSymbol()) + Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                } else {
                    mat[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if (card.getCorners()[0].getState() == CornerState.OCCUPIED) {
                mat[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else {
                mat[0][0][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[0].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

            if (card.getCorners()[1].getState() == CornerState.NOT_VISIBLE) {
                coordCover[0] = coord[0] + 1;
                coordCover[1] = coord[1] + 1;
                if (player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.BL).getState() == CornerState.OCCUPIED) {
                    mat[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + padding + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[3].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                } else {
                    mat[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if(card.getCorners()[1].getState() == CornerState.OCCUPIED){
                mat[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(Symbols.NOCORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else {
                mat[0][1][PLAYERBOARD_DIM / 2 - coord[1]][PLAYERBOARD_DIM / 2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[1].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

            if(card.getCorners()[3].getState() == CornerState.NOT_VISIBLE){
                coordCover[0] = coord[0] - 1;
                coordCover[1] = coord[1] - 1;
                if (player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TR).getState() == CornerState.OCCUPIED) {
                    mat[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[1].getSymbol()) + Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else {
                    mat[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if(card.getCorners()[3].getState() == CornerState.OCCUPIED){
                mat[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(Symbols.NOCORNER) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else{
                mat[1][0][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + Symbols.getStringBlack(card.getCorners()[3].getSymbol()) + Color.getBackground(card.getColor()) + padding + TuiColors.getColor(TuiColors.ANSI_RESET);
            }

            if(card.getCorners()[2].getState() == CornerState.NOT_VISIBLE){
                coordCover[0] = coord[0] + 1;
                coordCover[1] = coord[1] - 1;
                if (player.getPlayerBoard().getCard(coordCover) != null && player.getPlayerBoard().getCard(coordCover).getCorner(CornerEnum.TL).getState() == CornerState.OCCUPIED) {
                    mat[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(player.getPlayerBoard().getCard(coordCover).getColor()) + padding + Symbols.getStringBlack(player.getPlayerBoard().getCard(coordCover).getCorners()[0].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
                else {
                    mat[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
                }
            }
            else if(card.getCorners()[3].getState() == CornerState.OCCUPIED){
                mat[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(Symbols.NOCORNER) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
            else{
                mat[1][1][PLAYERBOARD_DIM/2 - coord[1]][PLAYERBOARD_DIM/2 + coord[0]] = Color.getBackground(card.getColor()) + padding + Symbols.getStringBlack(card.getCorners()[2].getSymbol()) + TuiColors.getColor(TuiColors.ANSI_RESET);
            }
        }

        System.out.println(topBorderLong.repeat(PLAYERBOARD_DIM + 1));
        for(int i = 0; i < PLAYERBOARD_DIM; i++){
            for(int t = 0; t < 2; t++){
                for(int j = 0; j < PLAYERBOARD_DIM; j++) {
                    System.out.print("|");
                    for (int s = 0; s < 2; s++) {
                        System.out.print(mat[t][s][i][j]);
                    }
                }
                System.out.println("|");
            }
            System.out.println(bottomBorderLong.repeat(PLAYERBOARD_DIM));
        }

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
}