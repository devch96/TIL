public class WildTurkey implements Turkey{
    @Override
    public void gobble() {
        System.out.println("칠면조가 구르릉");
    }

    @Override
    public void fly() {
        System.out.println("칠면조가 난다");
    }
}
