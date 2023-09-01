package facadeSubsystems;

public class Facade {
    private String beverageName = "";
    private String movieName="";

    public Facade(String beverageName, String movieName) {
        this.beverageName = beverageName;
        this.movieName = movieName;
    }

    public void viewMovie(){
        Beverage beverage = new Beverage(beverageName);
        RemoteControl remoteControl = new RemoteControl();
        Movie movie = new Movie(movieName);

        beverage.Prepare();
        remoteControl.turnOn();
        movie.Search_Movie();
        movie.Charge_Movie();
        movie.play_Movie();
    }
}
