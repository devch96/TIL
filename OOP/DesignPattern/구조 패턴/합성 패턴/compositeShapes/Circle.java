package compositeShapes;

public class Circle implements Shape{

    @Override
    public void draw(String color) {
        System.out.println("Circle Color : " + color);
    }
}
