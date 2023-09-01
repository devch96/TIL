package component;

import mediator.Flight;
import mediator.Runway;

public interface IATCMediator {
    void registerRunway(Runway runway);
    public void registerFlight(Flight flight);

    public boolean isLandingOK();

    public void setLandingStatus(boolean status);
}
