import Factory.NYPizzaStore;
import Factory.PizzaStore;

public class Demo {
    public static void main(String[] args) {
        PizzaStore pizzaStore = new NYPizzaStore();
        pizzaStore.orderPizza("cheese");
    }
}
