import Database.JSONToTDB;
import Database.SparqlQueries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import movie.Movie;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;

public class Main extends Application {

    private static Model model;

    public static void main(String[] args) {
        launch(args);
    }

    private void testQueries(SparqlQueries sparql) {
        ArrayList<String> titles = new ArrayList<>();
        titles = sparql.selectRandomMovies(10);
//      convert titles from recommendation algorithm into movieobjects using sparql.
        ArrayList<Movie> movies = sparql.CreateMovieObjects(titles);
        // add descriptions from dbpedia endpoint to our own TDB.
        sparql.insertDescriptionEndpoint(movies);
        // query the descriptions now from our own TDB.
        ArrayList<String> descriptions = sparql.getExtraDescription(movies);
        // Find the most frequent subjects of the parameter movies.
        ArrayList<String> topSubgernes = sparql.predictSubgenres(movies);
    }

    @Override
    public void start(Stage stage) throws Exception {
        firstTimeSetup();
        SparqlQueries sparqlQueries = new SparqlQueries();
        testQueries(sparqlQueries);

        Parent root = FXMLLoader.load(getClass().getResource("sample/WelcomeScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Movie movie.MovieRecommender System");
        stage.setScene(scene);
        stage.show();
    }

    // set up TDB, if it doesn't exist.
    public static Model firstTimeSetup() {
        JSONToTDB jsontoTDB = new JSONToTDB("movies.json");
        Model model = jsontoTDB.getModel();
        return model;
    }
}
