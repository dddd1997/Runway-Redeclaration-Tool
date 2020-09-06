package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

// GUI entry point app.
public class RedeclarationApp extends Application {
    private static Scene scene;
    private final String FXML_NAME = "interface";

    @Override
    // Main entry point for JavaFX applications
    public void start(Stage stage) throws IOException {
        URL fxmlFileURL = getClass().getResource(FXML_NAME + ".fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFileURL);
        scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);

        TabPane tabPane = FXMLLoader.load(fxmlFileURL);
        Node rootOfFirstTab = tabPane.getTabs().get(0).getContent();

        double minHeight = rootOfFirstTab.minHeight(-1) + 71; //Magic number account for borders, title and tabs.

        stage.setMinHeight(minHeight);
        stage.setMinWidth(rootOfFirstTab.minWidth(-1));

        stage.show();
    }

    // Launches the application by calling the start() method.
    public static void main(String[] args) {
        launch();
    }
}