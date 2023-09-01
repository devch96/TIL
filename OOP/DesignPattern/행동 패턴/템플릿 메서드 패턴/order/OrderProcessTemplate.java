package order;

public abstract class OrderProcessTemplate {
    public abstract void doSelect();
    public abstract void doPayment();
    public abstract void doDelivery();

    public final void giftWrap(){
        try{
            System.out.println("Gift wrap successful");
        } catch (Exception e){
            System.out.println("Gift wrap fail");
        }
    }

    public final void processOrder(boolean isGift){
        doSelect();
        doPayment();
        if(isGift) giftWrap();
        doDelivery();
    }

}
