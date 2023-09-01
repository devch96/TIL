package subject;

import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class NewsPublisher implements Publisher{
    private List<Observer> observers;
    private String title;
    private String news;

    public NewsPublisher() {
        observers = new ArrayList<>();
        title = null;
        news = null;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void remoteObserver(Observer observer) {
        observers.remove(observer);
    }

    public void setNews(String title, String news){
        this.title = title;
        this.news = news;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update(title,news);
        }
    }
}
