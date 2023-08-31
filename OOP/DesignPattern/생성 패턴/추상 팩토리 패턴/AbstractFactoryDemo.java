import factory.FurnitureFactory;
import factory.ModernFurnitureFactory;
import factory.VictorianFurnitureFactory;
import product.Chair;
import product.Table;

public class AbstractFactoryDemo {
    public static void main(String[] args) {
        FurnitureFactory factory;
        String type = "Modern";

        if (type.equals("Modern")) {
            factory = new ModernFurnitureFactory();
        }else{
            factory = new VictorianFurnitureFactory();
        }
        configure(factory);
    }

    private static void configure(FurnitureFactory factory) {
        Chair chair;
        Table table;

        chair = factory.createChair();
        chair.hasLegs();
        chair.sitOn();

        table = factory.createTable();
        table.hasLegs();
        table.putUp();
    }
}
