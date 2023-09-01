package subject;

import observer.Observer;

public interface Publisher {
    void registerObserver(Observer observer);

    void remoteObserver(Observer observer);
    void notifyObservers();
}
