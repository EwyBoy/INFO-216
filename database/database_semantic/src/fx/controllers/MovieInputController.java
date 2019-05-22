package fx.controllers;

import Database.SparqlQueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MovieInputController {

    @FXML TextField textField1;
    @FXML TextField textField2;
    @FXML TextField textField3;

    //private static User user = new User("", "", "","","","");

    public void backButtonPushed(ActionEvent event) throws IOException {

        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(welcomeScreenScene);
        window.show();

    }

    public void nextButtonPushed(ActionEvent event) throws IOException {

        SparqlQueries sparql = new SparqlQueries();
        boolean moveOn = false;

        if(!(textField1.getText().equals("")) && !(textField2.getText().equals("")) && !(textField3.getText().equals(""))) {
            // sparql queries to check existence of the three selected movies by the user.
            boolean exists1 = sparql.searchForATitle(textField1.getText());
            boolean exists2 = sparql.searchForATitle(textField2.getText());
            boolean exists3 = sparql.searchForATitle(textField3.getText());
            if(exists1 && exists2 && exists3) {
                moveOn = true;
                //user.setMovie1(textField1.getText());
                //user.setMovie2(textField2.getText());
                //user.setMovie3(textField3.getText());

                System.out.println("\n");
                sparql.sparqlEndpointGetComment(textField1.getText());
                System.out.println("\n");
                sparql.sparqlEndpointGetComment(textField2.getText());
                System.out.println("\n");
                sparql.sparqlEndpointGetComment(textField3.getText());
            }
        } if(moveOn) {

            Parent genreScreenParent = FXMLLoader.load(getClass().getResource("GenreScreen.fxml"));
            Scene genreScreenScene = new Scene(genreScreenParent);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(genreScreenScene);
            window.show();
        }

    }

    @FXML
    public void handleText(ActionEvent event) {}

}