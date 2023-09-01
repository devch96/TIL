package compositeShapes;

public class Dot implements Shape{
    @Override
    public void draw(String color) {
        System.out.println("Dot Color : " + color);
    }
}
