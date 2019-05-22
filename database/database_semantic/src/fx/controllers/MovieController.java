package fx.controllers;

import Database.JSONToTDB;
import Database.Movie;
import Database.MovieMap;
import Database.SparqlQueries;
import fx.models.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import javax.xml.soap.Text;
import java.io.File;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    @FXML
    private AnchorPane search;

    @FXML
    private TableView<MovieModel> movielist;

    @FXML
    private TableColumn<MovieModel, String> tab_table;

    @FXML
    private Text selected;

    @FXML
    private Button next;

    @FXML
    private Pane movieinfo;

    @FXML
    private ImageView cover;

    @FXML
    private Text title;

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

                        if (movie != null) {
                            System.out.println("Movie name: " + movieMap.get(movie.getTitle()).getTitle());
                            System.out.println("Movie year: " + movieMap.get(movie.getTitle()).getYear());
                            System.out.println("Movie country: " + movieMap.get(movie.getTitle()).getCountry());
                            System.out.println("Movie language: " + movieMap.get(movie.getTitle()).getLanguage());
                            System.out.println("Movie PEGI: " + movieMap.get(movie.getTitle()).getRating());
                            System.out.println("Movie Score: " + movieMap.get(movie.getTitle()).getScore());
                            System.out.println("Movie grossing: $" + movieMap.get(movie.getTitle()).getGross());
                            System.out.println("Movie actors: " + movieMap.get(movie.getTitle()).getActors());
                            System.out.println("Movie director: " + movieMap.get(movie.getTitle()).getDirector());
                            System.out.println("Movie keywords: " + movieMap.get(movie.getTitle()).getKeywords());
                            System.out.println("\n");
                        }
                    }
                }
            });
            return row ;
        });

        File f = new File("movie_tdb");
        if (f.exists() && f.isDirectory()) {
            System.out.println("Database found!");
        } else {
            System.out.println("Database not found!");
            firstTimeSetup();
        }


        SparqlQueries sparqlQueries = new SparqlQueries();
        ResultSet rs = sparqlQueries.allTitles();

        rs.forEachRemaining(
            qsol -> {
                if (qsol.get("?title").toString() != null) {
                    observableList.add(new MovieModel(qsol.get("?title").toString()));
                    //System.out.println(qsol.get("?title"));
                }
            }
        );

        tab_table.setCellValueFactory(new PropertyValueFactory<>("list"));
        movielist.setItems(observableList);

    }
}
