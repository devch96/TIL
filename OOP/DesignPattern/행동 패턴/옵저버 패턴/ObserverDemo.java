import observer.Observer;
import observer.Subscriber;
import subject.MagazinePublisher;
import subject.NewsPublisher;
import subject.Publisher;

public class ObserverDemo {
    public static void main(String[] args) {
        NewsPublisher newsPublisher = new NewsPublisher();
        MagazinePublisher magazinePublisher = new MagazinePublisher();

        Observer sub1 = new Subscriber("observer1", newsPublisher);
        Observer sub2 = new Subscriber("observer2", newsPublisher);
        Observer sub3 = new Subscriber("observer3", magazinePublisher);

        newsPublisher.setNews("뉴스1","00");
        newsPublisher.setNews("뉴스2","11");
        newsPublisher.remoteObserver(sub2);
        newsPublisher.setNews("뉴스3","22");
        magazinePublisher.setArticle("잡지","123");

    }
}
