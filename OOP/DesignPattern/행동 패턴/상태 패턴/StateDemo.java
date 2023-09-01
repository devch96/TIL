import context.AlertStateContext;
import state.Ring;
import state.Silent;

public class StateDemo {
    public static void main(String[] args) {
        AlertStateContext stateContext = new AlertStateContext();
        stateContext.alert();
        stateContext.setState(new Ring());
        stateContext.alert();
        stateContext.setState(new Silent());
        stateContext.alert();

    }
}
