package subject;

import observer.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MagazinePublisher implements Publisher{
    List<Observer> observers = new ArrayList<>();
    String title;
    String content;

    public void setArticle(String title, String content) {
        this.title = title;
        this.content = content;
        notifyObservers();
    }
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void remoteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Iterator<Observer> it = observers.iterator();
        while (it.hasNext()) {
            it.next().update(title, content);
        }
    }
}
