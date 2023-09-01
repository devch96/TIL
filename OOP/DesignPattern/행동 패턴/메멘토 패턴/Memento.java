public class Memento {
    private final String time;

    public Memento(String timeToSave){
        this.time = timeToSave;
    }

    public String getSavedTime(){
        return time;
    }
}
