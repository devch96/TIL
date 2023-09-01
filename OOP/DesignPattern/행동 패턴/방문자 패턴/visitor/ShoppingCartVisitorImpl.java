package visitor;

import element.Fruit;
import element.Meat;

public class ShoppingCartVisitorImpl implements ShoppingCartVisitor{
    @Override
    public int visit(Meat meat) {
        int cost = 0;

        if(meat.getPrice() > 50) cost = meat.getPrice()-5;
        else cost = meat.getPrice();

        System.out.println("MEAT : " + meat.getName() + " cost = " + cost);
        return cost;
    }

    @Override
    public int visit(Fruit fruit) {
        int cost = fruit.getPricePerKg() * fruit.getWeight();
        System.out.println(fruit.getName() + " cost = " + cost);
        return cost;
    }
}
