package product;

public class ModernChair implements Chair{
    @Override
    public void hasLegs() {
        System.out.println("현대식 의자: 다리 4개");
    }

    @Override
    public void sitOn() {
        System.out.println("현대식 의자에 앉다");
    }
}
