package sample;

import Database.SparqlQueries;
import Database.User;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import movie.MovieRecommender;
import org.apache.jena.query.ResultSet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GenreController implements Initializable {

    @FXML
    private Button next;

    @FXML
    private TableView<GenreModel> genrelist;

    @FXML
    private TableColumn<GenreModel, String> tab_table;

    @FXML
    private Button add_genre;

    @FXML
    private Text amount_selected;

    @FXML
    private Button back;

    @FXML
    private Button reset;

    @FXML
    private ListView<String> selectedGenresList;

    @FXML
    private Text error;

    @FXML
    private Text title;

    private ObservableList<GenreModel> observableList = FXCollections.observableArrayList();
    private ObservableList<String> observableGenreList = FXCollections.observableArrayList();

    public static List<String> selectedGenres = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final String[] name = new String[1];

        genrelist.setRowFactory( tv -> {
            TableRow<GenreModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    GenreModel rowData = row.getItem();
                    if (rowData.getList() != null)  {
                        name[0] = rowData.getList();
                        System.out.println(Arrays.toString(name));
                        String genre = Arrays.toString(name).replace("[", "").replace("]", "");
                        error.setText("");
                        title.setText(genre);
                    }
                }
            });
            return row;
        });

        SparqlQueries sparqlQueries = new SparqlQueries();
        ResultSet rs = sparqlQueries.allGenres();

        List<String> genreLister = new ArrayList<>();

        rs.forEachRemaining (
            qsol -> {
                if (qsol.get("?genre").toString() != null) {
                    String genre = qsol.get("?genre").toString();
                    String[] genres = genre.split("\\|");
                    for (String s : genres) {
                       if (!genreLister.contains(s)) {
                           genreLister.add(s);
                           observableList.add(new GenreModel(s));
                       }
                    }
                }
            }
        );

        tab_table.setCellValueFactory(new PropertyValueFactory<>("list"));
        genrelist.setItems(observableList);

        reset.setOnMouseClicked(clicked -> {
            error.setText("");
            observableGenreList.clear();
            selectedGenresList.setItems(observableGenreList);
            selectedGenres.clear();
            genreLister.clear();
            amount_selected.setText(selectedGenres.size() + " / 3 - Movies Genres");
        });

        add_genre.setOnMouseClicked(clicked -> {
            if (selectedGenres.size() < 3) {
                if (title.getText() != null && !title.getText().equals("")) {
                    String selectedGenre = title.getText();
                    if (selectedGenre != null && !selectedGenre.equals("")) {
                        if (!selectedGenres.contains(selectedGenre)) {
                            selectedGenres.add(selectedGenre);
                            error.setText(selectedGenre + " was added to your list");
                            amount_selected.setText(selectedGenres.size() + " / 3 - Genre Selected");
                            observableGenreList.add(selectedGenre);
                            selectedGenresList.setItems(observableGenreList);
                        } else {
                            error.setText("ERROR: This genre has already been selected");
                            System.out.println("ERROR: This genre has already been selected");
                        }
                    } else {
                        error.setText("ERROR: Unknown genre");
                        System.out.println("ERROR: Unknown genre");
                    }
                } else {
                    error.setText("ERROR: Please select a valid genre");
                    System.out.println("ERROR: Please select a valid genre");
                }
            } else {
                error.setText("ERROR: You can't select more then 3 genres");
                System.out.println("ERROR: You can't select more then 3 genres");
            }
        });
    }

    @FXML
    void backButtonPushed(ActionEvent event) throws IOException {
        selectedGenres.clear();
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("MovieScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    @FXML
    void nextButtonPushed(ActionEvent event) throws IOException {
        if (selectedGenres.size() == 3) {
            User user = new User(MovieController.selectedMovies, selectedGenres);
            MovieRecommender.recommend(user);

            Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("RecommendationScreen.fxml"));
            Scene welcomeScreenScene = new Scene(welcomeScreenParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(welcomeScreenScene);
            window.show();
        } else {
            error.setText("You must select 3 genres before continuing");
            System.out.println("You must select 3 genres before continuing");
        }
    }
}
