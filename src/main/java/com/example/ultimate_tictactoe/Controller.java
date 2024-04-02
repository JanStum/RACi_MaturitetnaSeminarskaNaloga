package com.example.ultimate_tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class Controller {
    @FXML
    private GridPane localGrid1, localGrid2, localGrid3, localGrid4, localGrid5, localGrid6, localGrid7, localGrid8, localGrid9;
    private GridPane[][] localGrid = new GridPane[3][3];
    @FXML
    private Label localWinnerLabel1, localWinnerLabel2, localWinnerLabel3, localWinnerLabel4, localWinnerLabel5, localWinnerLabel6, localWinnerLabel7, localWinnerLabel8, localWinnerLabel9;
    private Label[][] localWinnerLabel = new Label[3][3];
    private final int GlobalRow = 3; private final int GlobalColumn = 3; private final int LocalRow = 3; private final int LocalColumn = 3;
    private enum BoardStatus {
        X,
        O,
        TIE,
        EMPTY,
        UNDETERMINED;
    }
    private final BoardStatus[][][][] gameBoard = new BoardStatus[GlobalColumn][GlobalRow][LocalColumn][LocalRow];
    private final BoardStatus[][] localGameResults = new BoardStatus[GlobalColumn][GlobalRow];
    private BoardStatus onTheMove;
    private BoardStatus gameWinner;
    private Integer previousGlobalColumn;
    private Integer previousGlobalRow;
    private Integer previousLocalColumn;
    private Integer previousLocalRow;

    public void initialize(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    for (int l = 0; l < 3; l++){
                        gameBoard[l][k][j][i] = BoardStatus.EMPTY;
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                localGameResults[j][i] = BoardStatus.UNDETERMINED;
            }
        }
        previousGlobalColumn = 0;
        previousGlobalRow = 0;
        onTheMove = BoardStatus.X;
        gameWinner = BoardStatus.UNDETERMINED;
        localGrid[0][0] = localGrid1; localGrid[1][0] = localGrid2; localGrid[2][0] = localGrid3; localGrid[0][1] = localGrid4; localGrid[1][1] = localGrid5; localGrid[2][1] = localGrid6; localGrid[0][2] = localGrid7; localGrid[1][2] = localGrid8; localGrid[2][2] = localGrid9;
        localWinnerLabel[0][0] = localWinnerLabel1; localWinnerLabel[1][0] = localWinnerLabel2; localWinnerLabel[2][0] = localWinnerLabel3; localWinnerLabel[0][1] = localWinnerLabel4; localWinnerLabel[1][1] = localWinnerLabel5; localWinnerLabel[2][1] = localWinnerLabel6; localWinnerLabel[0][2] = localWinnerLabel7; localWinnerLabel[1][2] = localWinnerLabel8; localWinnerLabel[2][2] = localWinnerLabel9;
        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                localWinnerLabel[i][j].setVisible(false);
                localWinnerLabel[i][j].setText(null);
            }
        }
    }
    public void performMove(ActionEvent event) {
        Button square = (Button) event.getSource();
        int localColumn = GridPane.getColumnIndex(square);
        int localRow = GridPane.getRowIndex(square);
        int globalColumn = GridPane.getColumnIndex(square.getParent());
        int globalRow = GridPane.getRowIndex(square.getParent());


        if (isValidLocalMove(globalColumn, globalRow, localColumn, localRow)){

            gameBoard[globalColumn][globalRow][localColumn][localRow] = onTheMove;
            square.setText(onTheMove.toString());
            switch (onTheMove){
                case X:
                    square.getStyleClass().clear();
                    square.getStyleClass().add("square-x");
                    break;
                case O:
                    square.getStyleClass().clear();
                    square.getStyleClass().add("square-o");
                    break;
            }

            if ((localGameResults[globalColumn][globalRow] = localWinner(globalColumn, globalRow)) != BoardStatus.UNDETERMINED){
                indicateLocalWinner(globalColumn, globalRow, square);
                if ((gameWinner = globalWinner()) != BoardStatus.UNDETERMINED){
                    System.out.println("Zmagovalec je " + gameWinner.toString());
                }
            }

            previousGlobalColumn = globalColumn;
            previousGlobalRow = globalRow;
            previousLocalColumn = localColumn;
            previousLocalRow = localRow;

            indicatePlayableLocalGrid();

            switch (onTheMove){
                case X:
                    onTheMove = BoardStatus.O;
                    break;
                case O:
                    onTheMove = BoardStatus.X;
                    break;
            }
        }
    }public void resetGame(ActionEvent event){
        initialize();
        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                for (Node node : localGrid[i][j].getChildren()) {
                    if (node instanceof Button) {
                        ((Button) node).setText(null);
                        node.getStyleClass().removeAll("square-x", "square-o");
                        //node.getStyleClass().add("test");
                    }
                }
                localGrid[i][j].getStyleClass().clear();
                localGrid[i][j].getStyleClass().add("playable-local-grid");
            }
        }
    }
    public boolean isValidLocalMove(int globalColumn, int globalRow, int localColumn, int localRow){
        if (localGameResults[globalColumn][globalRow] == BoardStatus.UNDETERMINED
            && (gameWinner == BoardStatus.UNDETERMINED)
            && !isLocalBoardFull(globalColumn, globalRow)
            && gameBoard[globalColumn][globalRow][localColumn][localRow] == BoardStatus.EMPTY) {
            if ((localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.UNDETERMINED && previousLocalColumn == null && previousLocalRow == null)
               || (localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.UNDETERMINED && previousLocalColumn == globalColumn && previousLocalRow == globalRow)
               || (localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.X && previousLocalColumn == globalColumn && previousLocalRow == globalRow)
               || (localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.O && previousLocalColumn == globalColumn && previousLocalRow == globalRow)
               || (localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.TIE && previousLocalColumn == globalColumn && previousLocalRow == globalRow)
               || localGameResults[previousLocalColumn][previousLocalRow] == BoardStatus.O
               || localGameResults[previousLocalColumn][previousLocalRow] == BoardStatus.X
               || localGameResults[previousLocalColumn][previousLocalRow] == BoardStatus.TIE)
                return true;
        }
        return false;
    }
    public BoardStatus localWinner(int globalColumn, int globalRow){

        // Preverjanje zmagovalca horizontalno
        for (int i = 0; i < 3; i++){
            if (gameBoard[globalColumn][globalRow][0][i] == BoardStatus.X && gameBoard[globalColumn][globalRow][1][i] == BoardStatus.X && gameBoard[globalColumn][globalRow][2][i] == BoardStatus.X)
                return BoardStatus.X;
            else if (gameBoard[globalColumn][globalRow][0][i] == BoardStatus.O && gameBoard[globalColumn][globalRow][1][i] == BoardStatus.O && gameBoard[globalColumn][globalRow][2][i] == BoardStatus.O)
                return BoardStatus.O;
        }

        // Preverjanje zmagovalca vertikalno
        for (int i = 0; i < 3; i++){
            if (gameBoard[globalColumn][globalRow][i][0] == BoardStatus.X && gameBoard[globalColumn][globalRow][i][1] == BoardStatus.X && gameBoard[globalColumn][globalRow][i][2] == BoardStatus.X)
                return BoardStatus.X;
            else if (gameBoard[globalColumn][globalRow][i][0] == BoardStatus.O && gameBoard[globalColumn][globalRow][i][1] == BoardStatus.O && gameBoard[globalColumn][globalRow][i][2] == BoardStatus.O)
                return BoardStatus.O;
        }

        // Preverjanje zmagovalca po padajoči diagonali
        if (gameBoard[globalColumn][globalRow][0][0] == BoardStatus.X && gameBoard[globalColumn][globalRow][1][1] == BoardStatus.X && gameBoard[globalColumn][globalRow][2][2] == BoardStatus.X)
            return BoardStatus.X;
        else if (gameBoard[globalColumn][globalRow][0][0] == BoardStatus.O && gameBoard[globalColumn][globalRow][1][1] == BoardStatus.O && gameBoard[globalColumn][globalRow][2][2] == BoardStatus.O)
            return BoardStatus.O;

        // Preverjanje zmagovalca po naraščujoči diagonali
        else if (gameBoard[globalColumn][globalRow][2][0] == BoardStatus.X && gameBoard[globalColumn][globalRow][1][1] == BoardStatus.X && gameBoard[globalColumn][globalRow][0][2] == BoardStatus.X)
            return BoardStatus.X;
        else if (gameBoard[globalColumn][globalRow][2][0] == BoardStatus.O && gameBoard[globalColumn][globalRow][1][1] == BoardStatus.O && gameBoard[globalColumn][globalRow][0][2] == BoardStatus.O)
            return BoardStatus.O;

        // Preverjanje ali je lokalna igra izenačena
        if (isLocalBoardFull(globalColumn, globalRow))
            return BoardStatus.TIE;

        // Če ni niti zmagovalca, niti izenačeno, je igra še ne dokončana
        return BoardStatus.UNDETERMINED;
    }
    public BoardStatus globalWinner(){
        // Preverjanje zmagovalca horizontalno
        for (int i = 0; i < 3; i++){
            if (localGameResults[0][i] == BoardStatus.X && localGameResults[1][i] == BoardStatus.X && localGameResults[2][i] == BoardStatus.X)
                return BoardStatus.X;
            else if (localGameResults[0][i] == BoardStatus.O && localGameResults[1][i] == BoardStatus.O && localGameResults[2][i] == BoardStatus.O)
                return BoardStatus.O;
        }

        // Preverjanje zmagovalca vertikalno
        for (int i = 0; i < 3; i++){
            if (localGameResults[i][0] == BoardStatus.X && localGameResults[i][1] == BoardStatus.X && localGameResults[i][2] == BoardStatus.X)
                return BoardStatus.X;
            else if (localGameResults[i][0] == BoardStatus.O && localGameResults[i][1] == BoardStatus.O && localGameResults[i][2] == BoardStatus.O)
                return BoardStatus.O;
        }

        // Preverjanje zmagovalca po padajoči diagonali
        if (localGameResults[0][0] == BoardStatus.X && localGameResults[1][1] == BoardStatus.X && localGameResults[2][2] == BoardStatus.X)
            return BoardStatus.X;
        else if (localGameResults[0][0] == BoardStatus.O && localGameResults[1][1] == BoardStatus.O && localGameResults[2][2] == BoardStatus.O)
            return BoardStatus.O;

        // Preverjanje zmagovalca po naraščujoči diagonali
        else if (localGameResults[2][0] == BoardStatus.X && localGameResults[1][1] == BoardStatus.X && localGameResults[0][2] == BoardStatus.X)
            return BoardStatus.X;
        else if (localGameResults[2][0] == BoardStatus.O && localGameResults[1][1] == BoardStatus.O && localGameResults[0][2] == BoardStatus.O)
            return BoardStatus.O;

        // Preverjanje ali je lokalna igra izenačena
        if (isGlobalBoardFull())
            return BoardStatus.TIE;

        // Če ni niti zmagovalca, niti izenačeno, je igra še ne dokončana
        return BoardStatus.UNDETERMINED;
    }
    public boolean isLocalBoardFull(int globalColumn, int globalRow){
        int counter = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(gameBoard[globalColumn][globalRow][i][j] != BoardStatus.EMPTY)
                    counter ++;
            }
        }
        if (counter == 9)
            return true;
        return false;
    }
    public boolean isGlobalBoardFull(){
        int counter = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(localGameResults[i][j] != BoardStatus.UNDETERMINED)
                    counter ++;
            }
        }
        if (counter == 9)
            return true;
        return false;
    }
    public void indicateLocalWinner(int globalColumn, int globalRow, Button square){
        if (localGameResults[globalColumn][globalRow] == BoardStatus.X){
            localWinnerLabel[globalColumn][globalRow].setVisible(true);
            localWinnerLabel[globalColumn][globalRow].setText("X");
            localWinnerLabel[globalColumn][globalRow].getStyleClass().clear();
            localWinnerLabel[globalColumn][globalRow].getStyleClass().add("local-winner-x");
        }
        if (localGameResults[globalColumn][globalRow] == BoardStatus.O){
            localWinnerLabel[globalColumn][globalRow].setVisible(true);
            localWinnerLabel[globalColumn][globalRow].setText("O");
            localWinnerLabel[globalColumn][globalRow].getStyleClass().clear();
            localWinnerLabel[globalColumn][globalRow].getStyleClass().add("local-winner-o");
        }
        if (localGameResults[globalColumn][globalRow] == BoardStatus.TIE){
            localWinnerLabel[globalColumn][globalRow].setVisible(true);
            localWinnerLabel[globalColumn][globalRow].setText("=");
            localWinnerLabel[globalColumn][globalRow].getStyleClass().clear();
            localWinnerLabel[globalColumn][globalRow].getStyleClass().add("local-winner-tie");
        }
    }
    public void indicatePlayableLocalGrid(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                if(localGrid[i][j].getStyleClass().contains("local-winner-x") || localGrid[i][j].getStyleClass().contains("local-winner-o") || localGrid[i][j].getStyleClass().contains("local-winner-tie"))
                    continue;

                if ((localGameResults[i][j] == BoardStatus.UNDETERMINED) && (i == previousLocalColumn && j == previousLocalRow)
                    || ((localGameResults[previousLocalColumn][previousLocalRow] != BoardStatus.UNDETERMINED) && (localGameResults[i][j] == BoardStatus.UNDETERMINED))) {
                    localGrid[i][j].getStyleClass().removeAll("non-playable-local-grid");
                    localGrid[i][j].getStyleClass().add("playable-local-grid");
                }
                else {
                    localGrid[i][j].getStyleClass().removeAll("playable-local-grid");
                    localGrid[i][j].getStyleClass().add("non-playable-local-grid");
                }
            }
        }
    }
}