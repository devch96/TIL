package mediator;

import component.IATCMediator;

public class Runway implements Command{
    private IATCMediator atcMediator;

    public Runway(IATCMediator atcMediator) {
        this.atcMediator = atcMediator;
        atcMediator.setLandingStatus(true);
    }

    @Override
    public void land() {
        System.out.println("Lading permission granted.");
        atcMediator.setLandingStatus(true);
    }
}
