package chainHandler;

import request.Number;

public interface Handler {
    void setNext(Handler nextHandler);
    void process(Number request);
}
