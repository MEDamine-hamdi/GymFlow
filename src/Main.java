import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.gymflow.util.DBConnection;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        try {
            // 🔴 TEST DATABASE CONNECTION
            if (DBConnection.getConnection() != null) {
                System.out.println("✅ Database connected successfully");
            } else {
                System.out.println("❌ Database connection failed");
            }

            // 🔴 LOAD FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/login.fxml")
            );

            Scene scene = new Scene(loader.load());

            // 🔴 LOAD CSS
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            // 🔴 STAGE SETTINGS
            stage.setTitle("GymFlow");
            stage.setScene(scene);

            // 👉 Maximized (NOT fullscreen, keeps taskbar)
            stage.setMaximized(true);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}