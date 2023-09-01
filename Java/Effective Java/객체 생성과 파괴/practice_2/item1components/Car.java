package practice_2.item1components;

public class Car {
    private final String name;
    private final int oil;

    private Car(String name, int oil) {
        this.name = name;
        this.oil = oil;
    }

    public static Car createCar(String name, int oil) {
        return new Car(name, oil);
    }

    public static Car createNoOilCar(String name) {
        return new Car(name, 0);
    }
}
