import order.NetOrder;
import order.OrderProcessTemplate;
import order.StoreOrder;

public class TemplateMethodDemo {
    public static void main(String[] args) {
        OrderProcessTemplate netOrder = new NetOrder();
        netOrder.processOrder(true);
        System.out.println();
        OrderProcessTemplate storeOrder = new StoreOrder();
        storeOrder.processOrder(true);
    }
}
