package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageApplication extends Application {
    public static Stage window;
    public static FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(ImageApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Paint 2");
        stage.setScene(scene);
        stage.show();
        Image icon = new Image(new FileInputStream("src/main/resources/images/paint2.png"));
        stage.getIcons().add(icon);
        window = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}