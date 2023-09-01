package observer;

import subject.Publisher;

public class Subscriber implements Observer{
    private String name;
    private String news;
    Publisher publisher;

    public Subscriber(String subscriber, Publisher publisher) {
        this.name = subscriber;
        this.publisher = publisher;
        publisher.registerObserver(this);
    }

    @Override
    public void update(String title, String news) {
        this.news = title + "!!! " + news;
        display();
    }

    private void display() {
        System.out.println("=== " + name + " 수신 내용 ===\n" + news + "\n");
    }
}
