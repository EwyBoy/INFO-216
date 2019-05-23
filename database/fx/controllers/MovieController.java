package fx.controllers;

import Database.JSONToTDB;
import movie.Movie;
import movie.MovieMap;
import Database.SparqlQueries;
import fx.models.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MovieController implements Initializable {


    @FXML
    private AnchorPane search;

    @FXML
    private TextField search_box;

    @FXML
    private Pane movieinfo;

    @FXML
    private ImageView cover;

    @FXML
    private Text title;

    @FXML
    private Text country;

    @FXML
    private Text language;

    @FXML
    private Text year;

    @FXML
    private Text age_rating;

    @FXML
    private Text user_rating;

    @FXML
    private Text grossing;

    @FXML
    private Text director;

    @FXML
    private Text actors;

    @FXML
    private Text runtime;

    @FXML
    private Text genres;

    @FXML
    private Text error;

    @FXML
    private Text search_text;

    @FXML
    private Button add_movie;

    @FXML
    private Text amount_selected;

    @FXML
    private Button next;

    @FXML
    private Button back;

    @FXML
    private Button reset;

    @FXML
    private TableView<MovieModel> movielist;

    @FXML
    private TableColumn<MovieModel, String> tab_table;

    public static List<Movie> selectedMovies = new ArrayList<>();

    private ObservableList<MovieModel> observableList = FXCollections.observableArrayList();

    public static Model firstTimeSetup() {
        JSONToTDB jsontoTDB = new JSONToTDB("movies.json");
        Model model = jsontoTDB.getModel();
        return model;
    }

    public static final HashMap<String, Movie> movieMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final String[] name = new String[1];

        MovieMap.initMovieMap(movieMap);

        movielist.setRowFactory( tv -> {
            TableRow<MovieModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    MovieModel rowData = row.getItem();

                    if (rowData.getList() != null)  {
                        name[0] = rowData.getList();
                        System.out.println(Arrays.toString(name));

                        Movie movie = movieMap.get(
                             Arrays.toString(name)
                            .replace("[", "")
                            .replace("]", "")
                        );

                        error.setText("");

                        title.setText("Title: " + movie.getTitle());
                        year.setText("Year: " + movie.getYear());
                        genres.setText("Genres: " + movie.getGenres().replace("|", " - "));
                        runtime.setText("Runtime: " + movie.getRuntime() + "min");
                        country.setText("Country: " + movie.getCountry());
                        language.setText("Language: " + movie.getLanguage());
                        age_rating.setText("Age Rating: " + movie.getRating());
                        user_rating.setText("User Rating: " + movie.getScore() + "/10 (" + movie.getVotes() +" votes)");
                        grossing.setText("Grossing: $" + movie.getGross());
                        director.setText("Director: " + movie.getDirector());
                        actors.setText("Actors: " + movie.getActors().toString().replace("[", "").replace("]", "").replace(",", " -"));

                    }
                }
            });
            return row ;
        });

        File f = new File("movie_tdb");
        if (f.exists() && f.isDirectory()) {
            System.out.println("SPARQL database found!");
        } else {
            System.out.println("Generating SPARQL database");
            firstTimeSetup();
        }


        SparqlQueries sparqlQueries = new SparqlQueries();
        ResultSet rs = sparqlQueries.allTitles();

        rs.forEachRemaining (
            qsol -> {
                if (qsol.get("?title").toString() != null) {
                    observableList.add(new MovieModel(qsol.get("?title").toString()));
                    //System.out.println(qsol.get("?title"));
                }
            }
        );

        tab_table.setCellValueFactory(new PropertyValueFactory<>("list"));
        movielist.setItems(observableList);

        search_box.setOnAction(type -> {

            observableList.clear();
            movielist.setItems(observableList);

            ResultSet searchSet = sparqlQueries.search(search_box.getText());

            searchSet.forEachRemaining (
                qsol -> {
                    if (qsol.get("?title").toString() != null) {
                        observableList.add(new MovieModel(qsol.get("?title").toString()));
                    }
                }
            );

            tab_table.setCellValueFactory(new PropertyValueFactory<>("list"));
            movielist.setItems(observableList);

        });

        reset.setOnMouseClicked(clicked -> {
            selectedMovies.clear();
            error.setText("");
            amount_selected.setText(selectedMovies.size() + " / 5 - Movies Selected");
        });

        add_movie.setOnMouseClicked(clicked -> {
            if (selectedMovies.size() < 5) {
                if (title.getText() != null && !title.getText().equals("")) {
                    Movie selectedMovie = movieMap.get(title.getText().replace("Title: ", ""));
                    if (selectedMovie != null && !selectedMovie.getTitle().equals("")) {
                        if (!selectedMovies.contains(selectedMovie)) {
                            selectedMovies.add(selectedMovie);
                            error.setText(selectedMovie.getTitle() + " was added to your list");
                            amount_selected.setText(selectedMovies.size() + " / 5 - Movies Selected");
                        } else {
                            error.setText("ERROR: This movie has already been selected");
                            System.out.println("ERROR: This movie has already been selected");
                        }
                    } else {
                        error.setText("ERROR: Unknown movie");
                        System.out.println("ERROR: Unknown movie");
                    }
                } else {
                    error.setText("ERROR: Please select a valid movie");
                    System.out.println("ERROR: Please select a valid movie");
                }
            } else {
                error.setText("ERROR: You can't select more then 5 movies");
                System.out.println("ERROR: You can't select more then 5 movies");
            }

        });

    }

    public void backButtonPushed(ActionEvent event) throws IOException {
        selectedMovies.clear();
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("fx/fmxl/WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    public void nextButtonPushed(ActionEvent event) throws IOException {

        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("/fx/TestX.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(welcomeScreenScene);
        window.show();

    }
}
