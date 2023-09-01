package chainHandler;

import request.Number;

public class ZeroHandler implements Handler{
    private Handler nextHandler;

    @Override
    public void setNext(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void process(Number request) {
        if(request.getNumber() == 0){
            System.out.println("ZeroHandler : " + request.getNumber());
        }else{
            nextHandler.process(request);
        }
    }
}
