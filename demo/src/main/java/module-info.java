module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.web;
    requires jdk.jsobject;

    opens com.editor to javafx.fxml;
    exports com.editor;
    exports com.editor.utils;
    opens com.editor.utils to javafx.fxml;
    exports com.editor.controllers;
    opens com.editor.controllers to javafx.fxml;
}