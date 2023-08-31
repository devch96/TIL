package factory;

import product.NYStyleCheesePizza;
import product.NYStylePepperoniPizza;
import product.Pizza;

public class NYPizzaStore extends PizzaStore{
    @Override
    Pizza createPizza(String type) {
        if ("cheese".equals(type)) {
            return new NYStyleCheesePizza();
        } else if ("pepperoni".equals(type)) {
            return new NYStylePepperoniPizza();
        }
        return null;
    }
}
