package product;

public class NYStylePepperoniPizza implements Pizza{
    @Override
    public void prepare() {
        System.out.println("뉴욕 스타일 페퍼로니 피자 준비중");
    }

    @Override
    public void bake() {
        System.out.println("뉴욕 스타일 페퍼로니 피자 굽는중");

    }

    @Override
    public void box() {
        System.out.println("뉴욕 스타일 페퍼로니 피자 포장중");

    }
}
