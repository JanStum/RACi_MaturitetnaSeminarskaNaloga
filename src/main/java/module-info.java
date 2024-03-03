module com.example.ultimate_tictactoe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.ultimate_tictactoe to javafx.fxml;
    exports com.example.ultimate_tictactoe;
}