public class Stars extends BaseTreeDecorator{
    public Stars(ChristmasTree christmasTree) {
        super(christmasTree);
    }

    public String addStarts() {
        return " with Stars";
    }


    @Override
    public String decorate() {
        return super.decorate() + addStarts();
    }
}
