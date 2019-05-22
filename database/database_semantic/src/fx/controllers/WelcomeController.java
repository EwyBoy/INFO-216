package sample;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class WelcomeController {


    /**
     * When these methods are called, they will change the Scenes
     */
    public void startButtonPushed(ActionEvent event) throws IOException {

        Parent movieScreenParent = FXMLLoader.load(getClass().getResource("MovieInputScreen.fxml"));
        Scene movieScreenScene = new Scene(movieScreenParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(movieScreenScene);
        window.show();

    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }


    public void backButtonPushed3(ActionEvent event) throws IOException {

        Parent genreScreenParent = FXMLLoader.load(getClass().getResource("GenreScreen.fxml"));
        Scene genreScreenScene = new Scene(genreScreenParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(genreScreenScene);
        window.show();

    }

}
