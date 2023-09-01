package order;

public class StoreOrder extends OrderProcessTemplate{
    @Override
    public void doSelect() {
        System.out.println("Do Select : Customer chooses the item for shelf.");
    }

    @Override
    public void doPayment() {
        System.out.println("Do Payment : Pays at counter through cash/POS");
    }

    @Override
    public void doDelivery() {
        System.out.println("Do Delivery : Item deliverd to in delivery counter.");
    }
}
