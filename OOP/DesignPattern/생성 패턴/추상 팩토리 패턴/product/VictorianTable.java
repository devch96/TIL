package product;

public class VictorianTable implements Table{

    @Override
    public void hasLegs() {
        System.out.println("빅토리아식 테이블: 다리 4개");
    }

    @Override
    public void putUp() {
        System.out.println("빅토리아식 테이블에 커피잔을 놓다");
    }
}
