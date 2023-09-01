public class DecoratorDemo {
    public static void main(String[] args) {
        ChristmasTree tree = new DefaultChristmasTree();
        System.out.println(tree.decorate());

        ChristmasTree streetTree = new Lights(new DefaultChristmasTree());
        System.out.println(streetTree.decorate());

        ChristmasTree houseTree = new Stars(new DefaultChristmasTree());
        System.out.println(houseTree.decorate());

        ChristmasTree martTree = new Lights(new Stars(new DefaultChristmasTree()));
        System.out.println(martTree.decorate());
    }
}
