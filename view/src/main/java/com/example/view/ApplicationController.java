package com.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.model.ElGamal;
import org.example.model.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class ApplicationController extends Window {
    public TextField keyG = new TextField();
    public TextField keyH = new TextField();
    public TextField modN = new TextField();
    public TextField keyA = new TextField();
    public TextArea signatureArea = new TextArea();
    public TextArea publicText = new TextArea();


    private ElGamal elgamal = new ElGamal();
    final FileChooser fileChooser = new FileChooser();


    public ApplicationController() throws NoSuchAlgorithmException {
    }

    @FXML
    protected void GenerujKlucze() throws NoSuchAlgorithmException {

        elgamal.generateKeys();
        keyG.setText(elgamal.getG().toString(16));
        keyH.setText(elgamal.getH().toString(16));
        modN.setText(elgamal.getN().toString(16));
        keyA.setText(elgamal.getA().toString(16));

    }

    @FXML
    protected void wczytajKlucze() {
    }

    @FXML
    protected void zapiszKlucze() {
    }

    @FXML
    protected void weryfikujPodpisTekstu() {
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
    protected void podpiszTekst() {
        BigInteger[] result = elgamal.sign(publicText.getText().getBytes());
        signatureArea.setText(result[0].toString(16) + "\n" + result[1].toString(16));
    }

    @FXML
    protected void podpiszPlik() {
        try {
            if (keyA.getText().equals("") || keyG.getText().equals("") || keyH.getText().equals("") || modN.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.NONE, "NIE WYGENEROWANO KLUCZY !", ButtonType.OK);
                alert.setTitle("ERROR");
                alert.showAndWait();
            } else {
                fileChooser.setTitle("Wybierz plik do podpisania");
                File fileToSign = fileChooser.showOpenDialog(this);
                BigInteger[] signNumbers = elgamal.sign(Files.readAllBytes(fileToSign.getAbsoluteFile().toPath()));
                fileChooser.setTitle("Zapisz podpis do pliku");
                File signFile = fileChooser.showSaveDialog(this);
                try (FileWriter fos = new FileWriter(signFile, true)) {
                    fos.write(signNumbers[0].toString(16));
                    fos.write('\n');
                    fos.write(signNumbers[1].toString(16));
                }

            }
        } catch (NullPointerException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Nie wybrano pliku!", ButtonType.OK);
            alert.setTitle("Błąd");
            alert.setResizable(false);
            alert.showAndWait();
        }
    }

    @FXML
    protected void weryifkujPlik() {
        try {
            if (keyA.getText().equals("") || keyG.getText().equals("") || keyH.getText().equals("") || modN.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.NONE, "NIE WYGENEROWANO KLUCZY !", ButtonType.OK);
                alert.setTitle("ERROR");
                alert.showAndWait();
            } else {
                fileChooser.setTitle("Wybierz plik, który był podpisany");
                File signedFile = fileChooser.showOpenDialog(this);
                fileChooser.setTitle("Wybierz plik z podpisem");
                File fileWithSign = fileChooser.showOpenDialog(this);
                BigInteger[] signature = Arrays.stream(new String(Files.readAllBytes(fileWithSign.toPath()))
                                .split("\n"))
                        .map(Utils::hexToBytes)
                        .map(BigInteger::new)
                        .toArray(BigInteger[]::new);
                if (elgamal.verify(Files.readAllBytes(signedFile.getAbsoluteFile().toPath()), signature)) {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Zweryfikowano poprawnie", ButtonType.OK);
                    alert.setTitle("Weryfikacja");
                    alert.setResizable(false);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Zweryfikowano niepoprawnie", ButtonType.OK);
                    alert.setTitle("Weryfikacja");
                    alert.setResizable(false);
                    alert.showAndWait();
                }
            }
        } catch (NullPointerException | FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Nie wybrano pliku!", ButtonType.OK);
            alert.setTitle("Błąd");
            alert.setResizable(false);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}