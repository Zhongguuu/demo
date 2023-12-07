package com.editor.controllers;

import com.editor.Data;
import com.editor.utils.FileItem;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import com.editor.utils.MD5Util;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class MainController implements Initializable {
    @FXML
    private WebView codearea;

    @FXML
    private AnchorPane files;

    @FXML
    private TreeView<FileItem> filetree;

    @FXML
    private TextArea code;

    @FXML
    private ChoiceBox<String> theme;

    private String[] themes = {"idea", "darcula", "eclipse", "monokai"};

    @FXML
    private TextField MD5;

    @FXML
    private Button cal;

    @FXML
    private Button save;

    @FXML
    private Button copy;

    @FXML
    private Button fresh;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = codearea.getEngine();

        // JavaFX application code
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject jsobj = (JSObject) webEngine.executeScript("window");
                jsobj.setMember("javaConnector", new JavaConnector());
                System.out.println("加载成功");
            }
        });

        webEngine.load(Objects.requireNonNull(getClass().getResource("code.html")).toExternalForm());

        //设置主题
        theme.getItems().addAll(themes);
        theme.setValue("idea");
        theme.setOnAction(event -> {
            String theme = this.theme.getValue();
            String jsCode = "setTheme('" + theme + "');";
            webEngine.executeScript(jsCode);
            webEngine.executeScript("new_test()");
        });

        //渲染文件树
        refreshTree();

        filetree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<FileItem> selectedItem = filetree.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    FileItem fileItem = selectedItem.getValue();
                    File selectedFile = new File(fileItem.getPath());
                    if (selectedFile.isFile()) {
                        // 读取文件内容并传递到JavaScript中更新HTML中的TextArea
                        try {
                            Data.curFileItem = fileItem;
                            String content = Files.readString(selectedFile.toPath());
                            updateTextAreaInHtml(webEngine, content);
                            code.setText(content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        //点击计算按钮计算MD5
        cal.setOnAction(event -> {
            try {
                calMD5();
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        //点击复制按钮复制MD5
        copy.setOnAction(event -> {
            String md5 = MD5.getText();
            javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            javafx.scene.input.ClipboardContent clipboardContent = new javafx.scene.input.ClipboardContent();
            clipboardContent.putString(md5);
            clipboard.setContent(clipboardContent);
        });
    }

    public static class JavaConnector {
        public void sendToJava(String text) {
            // 在这里，你可以将来自textarea的数据传递到JavaFX应用程序中
            System.out.println("Received from HTML: " + text);
        }
    }

    public void refreshTree() {
        //渲染文件树
        File folder = new File("src/main/resources/com/editor/data");
        System.out.println(folder.exists());
        ImageView rootIcon= new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/folder.png"))));
        //设置rootIcon大小
        rootIcon.setFitWidth(16);
        rootIcon.setFitHeight(16);
        TreeItem<FileItem> rootItem = new TreeItem<>(new FileItem("data", folder.lastModified(), folder.getPath()), rootIcon);
        rootItem.setExpanded(true);
        rootItem.getValue().setFolder(true);
        rootItem.getValue().setSync(true);

        //加载codepoint下的png并存在ImageView列表中
        Map<String,ImageView> icons= Map.of(
                "java",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/java.png")))),
                "html",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/html.png")))),
                "css",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/css.png")))),
                "js",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/JavaScript.png")))),
                "py",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/python.png")))),
                "c",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/C.png")))),
                "cpp",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/cpp.png")))),
                "txt",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/txt.png")))),
                "folder",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/folder.png")))),
                "other",new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("codepoint/other.png")))));

        //设置icon大小
        for (ImageView icon:icons.values()){
            icon.setFitWidth(16);
            icon.setFitHeight(16);
        }

        populateTreeView(folder, rootItem, icons);

        filetree.setRoot(rootItem);
        filetree.setShowRoot(true);
    }

    private void populateTreeView(File folder, TreeItem<FileItem> rootItem, Map<String, ImageView> icons) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            FileItem fileItem = new FileItem(file.getName(), file.lastModified(), file.getPath());
            fileItem.setSync(true);
            TreeItem<FileItem> newItem = new TreeItem<>(fileItem);
            rootItem.getChildren().add(newItem);
            if (file.isDirectory()) {
                fileItem.setFolder(true);
                newItem.setGraphic(icons.get("folder"));
                populateTreeView(file, newItem, icons);
            } else {
                fileItem.setFolder(false);
                String[] split = file.getName().split("\\.");
                String suffix = split[split.length - 1];
                if (icons.containsKey(suffix)) {
                    newItem.setGraphic(icons.get(suffix));
                } else {
                    newItem.setGraphic(icons.get("other"));
                }
            }
        }
    }

    private void updateTextAreaInHtml(WebEngine webEngine, String content) {
        // 通过执行JavaScript来更新HTML中的TextArea
        // 对content进行编码，防止出现特殊字符
        content = URLEncoder.encode(content, StandardCharsets.UTF_8).replace("\\+", "%20");
        String jsCode = "updateTextArea('" + content + "');";
        webEngine.executeScript(jsCode);
    }

    //点击计算按钮计算MD5
    public void calMD5() throws IOException, NoSuchAlgorithmException {
        if (Data.curFileItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("未选择文件");
            return;
        }
        if (Data.curFileItem.isFolder()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("文件夹无法计算MD5");
            return;
        }
        String curPath = Data.curFileItem.getPath();
        String md5 = MD5Util.getFileMD5(new File(curPath));
        MD5.setText(md5);
    }

    //点击保存按钮保存文件
    public void saveFile() throws IOException {
        String curPath = Data.curFileItem.getPath();
        String content = code.getText();
        Files.writeString(new File(curPath).toPath(), content);
    }

}
