import chainHandler.Handler;
import chainHandler.NegativeHandler;
import chainHandler.PositiveHandler;
import chainHandler.ZeroHandler;
import request.Number;

public class ChainDemo {
    public static void main(String[] args) {
        Handler h1 = new PositiveHandler();
        Handler h2 = new ZeroHandler();
        Handler h3 = new NegativeHandler();

        h1.setNext(h2);
        h2.setNext(h3);

        h1.process(new Number(90));
        h1.process(new Number(-60));
        h1.process(new Number(0));
        h1.process(new Number(20));
    }
}
