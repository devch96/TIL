import compositeShapes.*;
import compositeShapes.Circle;
import compositeShapes.Rectangle;
import compositeShapes.Shape;

public class CompositeDemo {
    public static void main(String[] args) {
        Shape c1 = new Circle();
        Shape r1 = new Rectangle();
        Shape d1 = new Dot();

        Drawing drawing = new Drawing();
        drawing.add(r1);
        drawing.add(d1);
        drawing.add(c1);
        drawing.draw("red");

        drawing.clear();

        drawing.add(r1);
        drawing.add(c1);
        drawing.draw("blue");
    }
}
