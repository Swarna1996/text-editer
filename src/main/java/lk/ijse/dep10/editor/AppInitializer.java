package lk.ijse.dep10.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lk.ijse.dep10.editor.controller.MainFormController;

import java.io.IOException;
import java.util.Optional;

public class AppInitializer extends Application {
public static Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        primaryStage.setScene(new Scene(
                FXMLLoader.load(
                        getClass().getResource("/view/MainForm.fxml"))));

        primaryStage.show();
        primaryStage.setTitle("Text-Editor");
        primaryStage.centerOnScreen();


    }
}
