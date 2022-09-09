package com.example.pain_t;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Pain_tController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}