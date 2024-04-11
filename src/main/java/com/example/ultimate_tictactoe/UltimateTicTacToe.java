package com.example.ultimate_tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UltimateTicTacToe extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UltimateTicTacToe.class.getResource("View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 1000);
        stage.setTitle("Ultimate Tic-Tac-Toe");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("UltimateTicTacToe.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}