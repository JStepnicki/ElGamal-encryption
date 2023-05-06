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
import java.util.Scanner;

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
    protected void generateKeys() {
        elgamal.generateKeys();
        keyG.setText(elgamal.getG().toString(16));
        keyH.setText(elgamal.getH().toString(16));
        modN.setText(elgamal.getN().toString(16));
        keyA.setText(elgamal.getA().toString(16));

    }

    @FXML
    protected void loadKeys() {
        fileChooser.setTitle("Load signature from file");
        File signFile = fileChooser.showOpenDialog(this);
        try (Scanner scanner = new Scanner(signFile)) {
            String gLine = scanner.nextLine();
            String hLine = scanner.nextLine();
            String nLine = scanner.nextLine();
            String aLine = scanner.nextLine();
            BigInteger g = new BigInteger(gLine,16);
            BigInteger h = new BigInteger(hLine,16);
            BigInteger n = new BigInteger(nLine,16);
            BigInteger a = new BigInteger(aLine,16);
            elgamal.setG(g);
            elgamal.setH(h);
            elgamal.setN(n);
            elgamal.setA(a);
            keyG.setText(elgamal.getG().toString(16));
            keyH.setText(elgamal.getH().toString(16));
            modN.setText(elgamal.getN().toString(16));
            keyA.setText(elgamal.getA().toString(16));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void saveKeys() {
        fileChooser.setTitle("Save signature to file");
        File signFile = fileChooser.showSaveDialog(this);
        try (FileWriter fos = new FileWriter(signFile, true)) {
            fos.write(elgamal.getG().toString(16) + "\n");
            fos.write(elgamal.getH().toString(16) + "\n");
            fos.write(elgamal.getN().toString(16) + "\n");
            fos.write(elgamal.getA().toString(16) + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void verifyText() {
        BigInteger[] signature = Arrays.stream(signatureArea.getText()
                        .split("\n"))
                .map(Utils::hexToBytes)
                .map(BigInteger::new)
                .toArray(BigInteger[]::new);
        if (elgamal.verify(publicText.getText().getBytes(), signature)) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Verified correctly", ButtonType.OK);
            alert.setTitle("Verification");
            alert.setResizable(false);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Verified incorrectly", ButtonType.OK);
            alert.setTitle("Verification");
            alert.setResizable(false);
            alert.showAndWait();
        }
    }

    @FXML
    protected void signText() {
        BigInteger[] result = elgamal.sign(publicText.getText().getBytes());
        signatureArea.setText(result[0].toString(16) + "\n" + result[1].toString(16));
    }

    @FXML
    protected void signFile() {
        try {
            if (keyA.getText().equals("") || keyG.getText().equals("") || keyH.getText().equals("") || modN.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.NONE, "NO KEYS GENERATED!", ButtonType.OK);
                alert.setTitle("ERROR");
                alert.showAndWait();
            } else {
                fileChooser.setTitle("Choose file to sign");
                File fileToSign = fileChooser.showOpenDialog(this);
                BigInteger[] signNumbers = elgamal.sign(Files.readAllBytes(fileToSign.getAbsoluteFile().toPath()));
                fileChooser.setTitle("Save signature to file");
                File signFile = fileChooser.showSaveDialog(this);
                try (FileWriter fos = new FileWriter(signFile, true)) {
                    fos.write(signNumbers[0].toString(16));
                    fos.write('\n');
                    fos.write(signNumbers[1].toString(16));
                }

            }
        } catch (NullPointerException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "No file selected!", ButtonType.OK);
            alert.setTitle("Error");
            alert.setResizable(false);
            alert.showAndWait();
        }
    }

    @FXML
    protected void verifyFile() {
        try {
            if (keyA.getText().equals("") || keyG.getText().equals("") || keyH.getText().equals("") || modN.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.NONE, "NO KEYS GENERATED!", ButtonType.OK);
                alert.setTitle("ERROR");
                alert.showAndWait();
            } else {
                fileChooser.setTitle("Select the file that was signed");
                File signedFile = fileChooser.showOpenDialog(this);
                fileChooser.setTitle("Select file with signature");
                File fileWithSign = fileChooser.showOpenDialog(this);
                BigInteger[] signature = Arrays.stream(new String(Files.readAllBytes(fileWithSign.toPath()))
                                .split("\n"))
                        .map(Utils::hexToBytes)
                        .map(BigInteger::new)
                        .toArray(BigInteger[]::new);
                if (elgamal.verify(Files.readAllBytes(signedFile.getAbsoluteFile().toPath()), signature)) {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Verified correctly", ButtonType.OK);
                    alert.setTitle("Verification");
                    alert.setResizable(false);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Verified Incorrectly", ButtonType.OK);
                    alert.setTitle("Verification");
                    alert.setResizable(false);
                    alert.showAndWait();
                }
            }
        } catch (NullPointerException | FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "No file selected!", ButtonType.OK);
            alert.setTitle("Error");
            alert.setResizable(false);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}