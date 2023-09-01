import facadeSubsystems.Facade;

public class FacadeDemo {
    public static void main(String[] args) {
        Facade facade = new Facade("콜라", "오펜하이머");
        facade.viewMovie();
    }
}
