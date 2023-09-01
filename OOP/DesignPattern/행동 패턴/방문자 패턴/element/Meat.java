package element;

import visitor.ShoppingCartVisitor;

public class Meat implements ItemElement{
    private int price;
    private String name;

    public Meat(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }

}
