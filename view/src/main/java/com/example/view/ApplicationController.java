package com.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.model.ElGamal;
import org.example.model.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.*;


public class ApplicationController extends Window {
    public TextField keyG = new TextField();
    public TextField keyH = new TextField();
    public TextField modN = new TextField();
    public TextField keyA = new TextField();

    public TextArea signatureArea = new TextArea();
    public TextArea publicText = new TextArea();
    private final ElGamal elgamal = new ElGamal();
    final FileChooser fileChooser = new FileChooser();
    public Label fileWithPublicText = new Label();

    public ApplicationController() throws NoSuchAlgorithmException {
    }

    @FXML
    protected void GenerujKlucze() throws NoSuchAlgorithmException {

        elgamal.generateKeys();
        keyG.setText(elgamal.getG().toString(16));
        keyH.setText(elgamal.getH().toString(16));
        modN.setText(elgamal.getP().toString(16));
        keyA.setText(elgamal.getA().toString(16));

    }
    @FXML
    protected void wczytajKlucze(){
    }
    @FXML
    protected void zapiszKlucze(){
    }

    @FXML
    protected void weryfikujPodpisTekstu(){
        BigInteger[] signature = Arrays.stream(signatureArea.getText()
                        .split("\n"))
                .map(Utils::hexToBytes)
                .map(BigInteger::new)
                .toArray(BigInteger[]::new);
        if (elgamal.verify(publicText.getText().getBytes(), signature)) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Zweryfikowano poprawnie!", ButtonType.OK);
            alert.setTitle("Weryfikacja");
            alert.setResizable(false);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Zweryfikowano niepoprawnie!", ButtonType.OK);
            alert.setTitle("Weryfikacja");
            alert.setResizable(false);
            alert.showAndWait();
        }
    }

    @FXML
    protected void podpiszTekst(){
        BigInteger[] result = elgamal.sign(publicText.getText().getBytes());
        signatureArea.setText(result[0].toString(16) + "\n" +  result[1].toString(16));
    }
    @FXML
    protected void podpiszPlik(){
        try {
            if(keyA.getText().equals("") || keyG.getText().equals("") || keyH.getText().equals("") || modN.getText().equals("")) {
                elgamal.generateKeys();
            }
            fileChooser.setTitle("Wybierz plik do podpisania");
            File fileToSign = fileChooser.showOpenDialog(this);
            BigInteger[] signNumbers = elgamal.sign(Files.readAllBytes(fileToSign.getAbsoluteFile().toPath()));
            fileChooser.setTitle("Zapisz podpis do pliku");
            File signFile = fileChooser.showSaveDialog(this);
            fileWithPublicText.setText("Otworzono do podpisu: " + fileToSign.getName());
            try (FileWriter fos = new FileWriter(signFile, true)) {
                fos.write(signNumbers[0].toString(16));
                fos.write('\n');
                fos.write(signNumbers[1].toString(16));
            }

        } catch (NullPointerException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Nie wybrano pliku!", ButtonType.OK);
            alert.setTitle("Błąd");
            alert.setResizable(false);
            alert.showAndWait();
        }
    }
    @FXML
    protected void weryifkujPlik(){
    }
}