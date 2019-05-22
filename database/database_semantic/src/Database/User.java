package Database;

public class User {

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    private String genre1;
    private String genre2;
    private String genre3;

    public User(Movie movie1, Movie movie2, Movie movie3, String genre1, String genre2, String genre3) {
        this.movie1 = movie1;
        this.movie2 = movie2;
        this.movie3 = movie3;
        this.genre1 = genre1;
        this.genre2 = genre2;
        this.genre3 = genre3;
    }

    public Movie getMovie1() {
        return movie1;
    }

    public void setMovie1(Movie movie1) {
        this.movie1 = movie1;
    }

    public Movie getMovie2() {
        return movie2;
    }

    public void setMovie2(Movie movie2) {
        this.movie2 = movie2;
    }

    public Movie getMovie3() {
        return movie3;
    }

    public void setMovie3(Movie movie3) {
        this.movie3 = movie3;
    }

    public String getGenre1() {
        return genre1;
    }

    public void setGenre1(String genre1) {
        this.genre1 = genre1;
    }

    public String getGenre2() {
        return genre2;
    }

    public void setGenre2(String genre2) {
        this.genre2 = genre2;
    }

    public String getGenre3() {
        return genre3;
    }

    public void setGenre3(String genre3) {
        this.genre3 = genre3;
    }


}
