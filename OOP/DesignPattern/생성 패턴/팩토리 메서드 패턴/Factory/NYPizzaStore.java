package Factory;

import Product.NYStyleCheesePizza;
import Product.NYStylePepperoniPizza;
import Product.Pizza;

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
