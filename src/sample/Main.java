package sample;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root  = new BorderPane();

        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                root.setCenter(createGridPane(newValue.intValue(), 100));
            }
        });

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    Node createGridPane(int sceneWidth, int foldersCount) {

        int buttonWidth = 100;
        int buttonCountInRow = sceneWidth / buttonWidth;
        int h = buttonCountInRow == 0 ? 0 : foldersCount / buttonCountInRow;
        Image image = new Image("ic_folder_black_48dp_2x.png");
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(0,15,50,0));
        for (int i = 0; i < buttonCountInRow; i ++) {
            for (int j = 0; j < h; j ++) {
                ImageView imageView = new ImageView(image);
                Label label = new Label("name" + i + j);
                VBox item = new VBox(imageView, label);

                item.setAlignment(Pos.CENTER);
                pane.add(item, i, j);
            }
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(pane);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        return sp;
    }

}
