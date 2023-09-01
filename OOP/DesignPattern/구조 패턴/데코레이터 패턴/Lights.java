public class Lights extends BaseTreeDecorator{
    public Lights(ChristmasTree christmasTree) {
        super(christmasTree);
    }

    public String addLights(){
        return " with Lights";
    }

    @Override
    public String decorate() {
        return super.decorate() + addLights();
    }
}
