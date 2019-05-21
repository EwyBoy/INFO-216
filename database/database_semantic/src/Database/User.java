package Database;

public class User {

    private String movie1;
    private String movie2;
    private String movie3;

    private String genre1;
    private String genre2;
    private String genre3;

    public User(String movie1, String movie2, String movie3, String genre1, String genre2, String genre3) {
        this.movie1 = movie1;
        this.movie2 = movie2;
        this.movie3 = movie3;
        this.genre1 = genre1;
        this.genre2 = genre2;
        this.genre3 = genre3;
    }

    public String getMovie1() {
        return movie1;
    }

    public void setMovie1(String movie1) {
        this.movie1 = movie1;
    }

    public String getMovie2() {
        return movie2;
    }

    public void setMovie2(String movie2) {
        this.movie2 = movie2;
    }

    public String getMovie3() {
        return movie3;
    }

    public void setMovie3(String movie3) {
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
