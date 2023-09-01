package state;

import context.AlertStateContext;

public class Vibration implements MobileAlertState{
    @Override
    public void alert(AlertStateContext ctx) {
        System.out.println("Vibration");
    }
}
