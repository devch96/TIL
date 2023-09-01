import element.Fruit;
import element.ItemElement;
import element.Meat;
import visitor.ShoppingCartVisitor;
import visitor.ShoppingCartVisitorImpl;

public class VisitorDemo {
    public static void main(String[] args) {
        ItemElement[] items = new ItemElement[]{new Meat(20, "Pork"), new Meat(100, "Cow"),
                new Fruit(10, 2, "Banana"), new Fruit(5, 5, "Apple")};
        ShoppingCartVisitor visitor = new ShoppingCartVisitorImpl();
        int sum = 0;

        for (ItemElement item : items) {
            sum = sum + item.accept(visitor);
        }

        System.out.println("Total Cost = " + sum);


    }
}
