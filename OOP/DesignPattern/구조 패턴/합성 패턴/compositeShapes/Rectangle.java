package compositeShapes;

public class Rectangle implements Shape{
    @Override
    public void draw(String color) {
        System.out.println("Rectangle Color : "+ color);
    }
}
