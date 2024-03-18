package com.example.ultimate_tictactoe;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Controller {
    private final int GlobalRow = 3; private final int GlobalColumn = 3; private final int LocalRow = 3; private final int LocalColumn = 3;
    private enum BoardStatus {
        X,
        O,
        TIE,
        EMPTY,
        UDETERMINED;
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
                localGameResults[j][i] = BoardStatus.UDETERMINED;
            }
        }
        previousGlobalColumn = 0;
        previousGlobalRow = 0;
        onTheMove = BoardStatus.X;
        gameWinner = BoardStatus.UDETERMINED;
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

            if ((localGameResults[globalColumn][globalRow] = localWinner(globalColumn, globalRow)) != BoardStatus.UDETERMINED){
                if ((gameWinner = globalWinner()) != BoardStatus.UDETERMINED){
                    System.out.println("Zmagovalec je " + gameWinner.toString());
                }
            }

            previousGlobalColumn = globalColumn;
            previousGlobalRow = globalRow;
            previousLocalColumn = localColumn;
            previousLocalRow = localRow;

            switch (onTheMove){
                case X:
                    onTheMove = BoardStatus.O;
                    break;
                case O:
                    onTheMove = BoardStatus.X;
                    break;
            }
        }
    }
    public boolean isValidLocalMove(int globalColumn, int globalRow, int localColumn, int localRow){
        if (localGameResults[globalColumn][globalRow] == BoardStatus.UDETERMINED
            && !isLocalBoardFull(globalColumn, globalRow)
            && gameBoard[globalColumn][globalRow][localColumn][localRow] == BoardStatus.EMPTY) {
            if ((localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.UDETERMINED && previousLocalColumn == null && previousLocalRow == null)
               || (localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.UDETERMINED && previousLocalColumn == globalColumn && previousLocalRow == globalRow)
               || localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.X
               || localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.O
               || localGameResults[previousGlobalColumn][previousGlobalRow] == BoardStatus.TIE
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
        return BoardStatus.UDETERMINED;
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
        return BoardStatus.UDETERMINED;
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
                if(localGameResults[i][j] != BoardStatus.UDETERMINED)
                    counter ++;
            }
        }
        if (counter == 9)
            return true;
        return false;
    }
}