import factory.NYPizzaStore;
import factory.PizzaStore;

public class FactoryMethodDemo {
    public static void main(String[] args) {
        PizzaStore pizzaStore = new NYPizzaStore();
        pizzaStore.orderPizza("cheese");
    }
}
