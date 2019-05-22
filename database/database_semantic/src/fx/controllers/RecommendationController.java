package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RecommendationController {


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
