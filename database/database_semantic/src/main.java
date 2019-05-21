
import Database.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static User user = new User("","","","","","");

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample/WelcomeScreen.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Movie Recommender System");

        stage.setScene(scene);
        stage.show();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}


