package state;

import context.AlertStateContext;

public class Ring implements MobileAlertState{
    @Override
    public void alert(AlertStateContext ctx) {
        System.out.println("Ring");
    }
}
