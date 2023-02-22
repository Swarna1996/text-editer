package lk.ijse.dep10.editor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dep10.editor.AppInitializer;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

public class MainFormController {

    public MenuItem mnSaveAs;
    public HTMLEditor txtEditor;
    public Stage currentStage = AppInitializer.stage;
    private boolean isFileOpened = false;
    private File currentOpenedFile;

    public void initialize() {
        currentStage.setOnCloseRequest(windowEvent -> {
            boolean isBodyEmpty = txtEditor.getHtmlText().replaceAll("\\<.*?\\>", "").trim().isEmpty();
            if (!isBodyEmpty) {
                Optional<ButtonType> optButton = new Alert(Alert.AlertType.CONFIRMATION, "Are you want to Save the file before exit?",
                        ButtonType.YES, ButtonType.NO).showAndWait();
                if (optButton.isEmpty() || optButton.get() == ButtonType.NO) return;
                mnSaveAs.fire();
            }
        });
    }

    @FXML
    void mnAboutOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(
                FXMLLoader.load(
                        getClass().getResource("/view/AboutForm.fxml"))));

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(txtEditor.getScene().getWindow());
        stage.setTitle("About");
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    @FXML
    void mnCloseOnAction(ActionEvent event) {

    }

    @FXML
    void mnNewOnAction(ActionEvent event) {
        String htmlText = txtEditor.getHtmlText();
        boolean isBodyEmpty = htmlText.replaceAll("\\<.*?\\>", "").trim().isEmpty();

        if (isFileOpened || !isBodyEmpty) {
            Optional<ButtonType> optButton = new Alert(Alert.AlertType.CONFIRMATION, "File not saved, Do you want to Save?"
                    , ButtonType.YES, ButtonType.NO).showAndWait();
            if ( optButton.get() == ButtonType.YES) mnSaveAs.fire();
        }
        mnSaveAs.setDisable(true);
        isFileOpened = false;
        txtEditor.setHtmlText("");
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.setTitle("Text-Editor");

    }

    @FXML
    void mnOpenOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a text File");
        File file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());
        if (file == null) return;

        currentOpenedFile = file;
        String title = file.getName();

        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = fis.readAllBytes();

        fis.close();
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(title);
        isFileOpened = true;
        txtEditor.setHtmlText(new String(bytes));
    }

    @FXML
    void mnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void mnSaveOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        String title = stage.getTitle();

        if (title.equals("*Untitled Document")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save a text file");
            File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (file == null) return;

            FileOutputStream fos = new FileOutputStream(file, false);
            String text = txtEditor.getHtmlText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);

            fos.close();
        } else {
            Optional<ButtonType> optButton = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to override?"
                    , ButtonType.YES, ButtonType.NO).showAndWait();
            if (optButton.isEmpty() || optButton.get() == ButtonType.NO) return;

            FileOutputStream fos = new FileOutputStream(currentOpenedFile);
            String text = txtEditor.getHtmlText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);

            fos.close();
        }
    }

    public void rootOnDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.ANY);
    }


    public void rootOnDragDropped(DragEvent dragEvent) throws IOException {
        System.out.println("added");
        File droppedFile = dragEvent.getDragboard().getFiles().get(0);

        FileInputStream fis = new FileInputStream(droppedFile);
        byte[] bytes = fis.readAllBytes();

        currentOpenedFile = droppedFile;

        fis.close();
        isFileOpened = true;
        System.out.println(Arrays.toString(bytes));
        txtEditor.setHtmlText(new String(bytes));
    }

    public void txtEditorOnInputMethodTextChanged(InputMethodEvent inputMethodEvent) {
//        System.out.println("on text changed");
    }

    public void txtEditorOnKeyTyped(KeyEvent keyEvent) {
//        System.out.println("on key typed");
    }

    public void txtEditorOnKeyReleased(KeyEvent keyEvent) {
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        String appTitle = stage.getTitle();
        if (appTitle.charAt(0) != '*') stage.setTitle("*" + appTitle);
        if (!stage.getTitle().equals("*Untitled Document")) mnSaveAs.setDisable(false);
    }

    public void txtEditorOnKeyPressed(KeyEvent keyEvent) {
        String htmlText = txtEditor.getHtmlText();
        boolean isBodyEmpty = htmlText.replaceAll("\\<.*?\\>", "").trim().isEmpty();

        if (!isFileOpened && isBodyEmpty) {
            Stage stage = (Stage) txtEditor.getScene().getWindow();
            stage.setTitle("Untitled Document");
        }

        Stage stage = (Stage) txtEditor.getScene().getWindow();
        String appTitle = stage.getTitle();
        if (appTitle.charAt(0) != '*') {
            stage.setTitle("*" + appTitle);
            if (!stage.getTitle().equals("*Untitled Document")) mnSaveAs.setDisable(false);
        }
        if (!stage.getTitle().equals("*Untitled Document")) mnSaveAs.setDisable(false);
    }

    public void mnSaveAsOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a text file");
        File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
        if (file == null) return;

        FileOutputStream fos = new FileOutputStream(file, false);
        String text = txtEditor.getHtmlText();
        byte[] bytes = text.getBytes();
        fos.write(bytes);

        fos.close();
    }
}


//Dragged