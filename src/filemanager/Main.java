package filemanager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        MainScene mainScene = new MainScene(800, 600);

        primaryStage.setTitle("File Manager");
        primaryStage.setScene(mainScene.getScene());
        primaryStage.setMinWidth(550);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}