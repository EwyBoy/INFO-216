package film;

public class Movie {

    private int year;
    private String title;
    private String regi[];
    private String actors[];

    private boolean hasSubGenres;

    public Movie() {}

    public Movie(int year, String title, String[] regi, String[] actors) {
        this.year = year;
        this.title = title;
        this.regi = regi;
        this.actors = actors;
    }

    public boolean isHasSubGenres() {
        return hasSubGenres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRegi() {
        return regi;
    }

    public void setRegi(String[] regi) {
        this.regi = regi;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }
}
