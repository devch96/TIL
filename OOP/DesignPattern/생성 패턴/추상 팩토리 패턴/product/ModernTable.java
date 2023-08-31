package product;

public class ModernTable implements Table{
    @Override
    public void hasLegs() {
        System.out.println("현대식 테이블: 다리 4개");
    }

    @Override
    public void putUp() {
        System.out.println("현대식 테이블에 스마트폰을 놓다");
    }
}
