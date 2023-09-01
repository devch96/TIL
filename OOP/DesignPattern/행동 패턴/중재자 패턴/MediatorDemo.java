import component.ATCMediator;
import component.IATCMediator;
import mediator.Flight;
import mediator.Runway;

public class MediatorDemo {
    public static void main(String[] args) {
        IATCMediator atcMediator = new ATCMediator();
        Flight cptJack = new Flight(atcMediator);
        Runway runnerWay = new Runway(atcMediator);
        atcMediator.registerFlight(cptJack);
        atcMediator.registerRunway(runnerWay);
        cptJack.getReady();
        runnerWay.land();
        cptJack.land();
    }
}
