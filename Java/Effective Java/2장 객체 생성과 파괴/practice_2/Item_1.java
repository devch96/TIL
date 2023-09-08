package practice_2;

import practice_2.item1components.*;

public class Item_1 {
    public static void main(String[] args) {
        // 이름을 갖음
        Car fullOilCar = Car.createCar("car1", 100);
        Car noOilCar = Car.createNoOilCar("car2");

        // 호출될 때마다 인스턴스를 새로 생성하지 않아도 됨.

        Day day = Day.from("mon");
        System.out.println(day);
        day = Day.from("mon");
        System.out.println(day);

        // 반환 타입의 하위 타입 객체를 반환할 수 있음.
        Apple apple = Fruit.toApple("사과", 10);

        // 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
        Fruit apple1 = Fruit.whichFruit("apple", 10);
        Fruit banana1 = Fruit.whichFruit("banana", 10);

    }
}

