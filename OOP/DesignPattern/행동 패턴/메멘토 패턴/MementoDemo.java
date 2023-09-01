public class MementoDemo {
    public static void main(String[] args) {
        Life life = new Life();
        life.setTime("100");
        System.out.println(life.getTime());
        Memento memento = life.saveToMemento();
        life.setTime("50");
        System.out.println(life.getTime());
        life.restoreFromMemento(memento);
        System.out.println(life.getTime());
    }
}
