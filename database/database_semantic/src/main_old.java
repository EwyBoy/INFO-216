import Database.JSONToTDB;
import Database.Movie;
import Database.SparqlQueries;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;



public class main_old {

    public static final HashMap<String, Movie> movieMap = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        firstTimeSetup();
        SparqlQueries sparql = new SparqlQueries();
//        sparql.createMovieObjects();
//        ArrayList<Movie> movies = sparql.getMovies();
//        System.out.println("How many movies?  " + movies.size());
//        for (Movie m: movies) {
//            System.out.println(m.getTitle());
//        }
//        sparql.printAllTriples();

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonReader file = new JsonReader(new FileReader("movies.json"));

        JsonElement element = parser.parse(file);
        JsonArray array = element.getAsJsonArray();

        for(JsonElement movies : array) {

            JsonObject movieObj = movies.getAsJsonObject();

            ArrayList<String> actorList = new ArrayList<>();
            ArrayList<String> keywordList = new ArrayList<>();


            Movie movie = new Movie(
                movieObj.get("title").getAsString(),
                movieObj.get("year").getAsString(),
                movieObj.get("genres").getAsString(),
                movieObj.get("runtime").getAsString(),
                movieObj.get("language").getAsString(),
                movieObj.get("country").getAsString(),
                movieObj.get("gross").getAsString(),
                movieObj.get("num_votes").getAsString(),
                movieObj.get("content_rating").getAsString(),
                movieObj.get("imdb_rating").getAsString(),
                movieObj.get("imdb_link").getAsString(),
                movieObj.get("imdb_id").getAsString(),
                movieObj.get("director").getAsString(),
                keywordList,
                actorList
            );

            System.out.println(movieObj.get("title").getAsString());
            movieMap.put(movieObj.get("title").getAsString(), movie);

        }

        // Remove empty space here
//        System.out.println("Score: " + movieMap.get("Scary Movie 4 ").getScore());

        /*

        Iterator it = movieMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        */

    }
//
//    private void testQueries(SparqlQueries sparql) {
////        sparql.sparqlEndpoint("Interstellar");
////        sparql.sparqlEndpointGetComment("Interstellar");
////        sparql.sparqlEndpointGetSubjects("Interstellar");
////        sparql.AllMoviesOfDirector("quentin");
////        sparql.AllMoviesOfActor("Leonardo DiCaprio");
//        sparql.close();
//    }

    public static void firstTimeSetup() {

        Dataset dataset = TDBFactory.createDataset("movie_tdb");
        if(dataset.isEmpty()) {
            System.out.println("HELLO");
            JSONToTDB jsontoTDB = new JSONToTDB("movies.json");
        }
    }
}
