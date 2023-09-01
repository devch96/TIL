public abstract class BaseTreeDecorator implements ChristmasTree{
    private ChristmasTree christmasTree;

    public BaseTreeDecorator(ChristmasTree christmasTree) {
        this.christmasTree = christmasTree;
    }
    @Override
    public String decorate() {
        return christmasTree.decorate();
    }
}
