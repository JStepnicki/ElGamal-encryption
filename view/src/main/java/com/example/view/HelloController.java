package com.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.model.ElGamal;

import java.security.NoSuchAlgorithmException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws NoSuchAlgorithmException {
        welcomeText.setText("Welcome to JavaFX Application!");
        ElGamal dupa = new ElGamal();
    }
}