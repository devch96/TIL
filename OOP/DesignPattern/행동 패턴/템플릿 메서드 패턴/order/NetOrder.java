package order;

public class NetOrder extends OrderProcessTemplate{
    @Override
    public void doSelect() {
        System.out.println("Do Select : Item added to online shopping cart");
        System.out.println("Do Select : Get gift wrap preference");
        System.out.println("Do Select : Get delivery address.");
    }

    @Override
    public void doPayment() {
        System.out.println
                ("Do Payment : Online Payment through Netbanking, card or Paytm");
    }

    @Override
    public void doDelivery() {
        System.out.println
                ("Do Delivery : Ship the item through post to delivery address");
    }
}
