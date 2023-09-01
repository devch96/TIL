package mediator;

import component.IATCMediator;

public class Flight implements Command {
    private IATCMediator atcMediator;

    public Flight(IATCMediator atcMediator) {
        this.atcMediator = atcMediator;
    }

    @Override
    public void land() {
        if (atcMediator.isLandingOK()) {
            System.out.println("Landed Success.");
            atcMediator.setLandingStatus(true);
        } else {
            System.out.println("Waiting for Landing.");
        }
    }

    public void getReady() {
        System.out.println("Ready for landing");
    }
}