package product;

public class VictorianChair implements Chair{

    @Override
    public void hasLegs() {
        System.out.println("빅토리아식 의자: 다리 4개");
    }

    @Override
    public void sitOn() {
        System.out.println("빅토리아식 의자에 앉다");
    }
}
