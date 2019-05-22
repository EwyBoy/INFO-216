import Database.Movie;
import Database.MovieMap;

import java.util.HashMap;
import java.util.Scanner;


public class MainOld {

    public static final HashMap<String, Movie> movieMap = new HashMap<>();

    public static void main(String[] args) {
        MovieMap.initMovieMap(movieMap);

        System.out.println("Enter movie title: ");
        Scanner scanner = new Scanner(System.in);
        String movie = scanner.nextLine();

        /*System.out.println("Movie name: " + movieMap.get(movie).getTitle());
        System.out.println("Movie year: " + movieMap.get(movie).getYear());
        System.out.println("Movie country: " + movieMap.get(movie).getCountry());
        System.out.println("Movie language: " + movieMap.get(movie).getLanguage());
        System.out.println("Movie PEGI: " + movieMap.get(movie).getRating());
        System.out.println("Movie Score: " + movieMap.get(movie).getScore());
        System.out.println("Movie grossing: $" + movieMap.get(movie).getGross());
        System.out.println("Movie actors: " + movieMap.get(movie).getActors());
        System.out.println("Movie director: " + movieMap.get(movie).getDirector());
        System.out.println("Movie keywords: " + movieMap.get(movie).getKeywords());*/
    }
}
