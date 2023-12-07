package com.editor.controllers;

import com.editor.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button btn_login;

    @FXML
    private Button btn_register;

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    void login(ActionEvent event) {
        System.out.println("login");
        //跳转到主页面
        MainApplication mainApplication = new MainApplication();
        try {
            mainApplication.start(new Stage());
            Stage stage = (Stage) btn_login.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void register(ActionEvent event) {
        System.out.println("register");
        //跳转到主页面
        MainApplication mainApplication = new MainApplication();
        try {
            mainApplication.start(new Stage());
            Stage stage = (Stage) btn_register.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
