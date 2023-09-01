package practice_2.item1components;

public class Fruit {
    String name;
    int price;

    protected Fruit(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Apple toApple(String name, int price) {
        return new Apple(name, price);
    }

    public static Fruit whichFruit(String name, int price){
        if (name.equals("apple")) {
            return new Apple(name, price);
        }else{
            return new Banana(name, price);
        }
    }
}
