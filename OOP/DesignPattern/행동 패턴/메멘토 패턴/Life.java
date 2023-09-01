public class Life {
    private String time;

    public void setTime(String time){
        System.out.println("Setting Time : " + time);
        this.time = time;
    }

    public Memento saveToMemento(){
        System.out.println("Saving time to Memento");
        return new Memento(time);
    }

    public void restoreFromMemento(Memento memento) {
        time = memento.getSavedTime();
        System.out.println("Time restored from Memento : " + time);
    }

    public String getTime(){
        return time;
    }
}
