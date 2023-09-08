package practice_2;

public class Item_3 {
    public static void main(String[] args) {
        Elvis e = Elvis.INSTANCE;
        Elvis l = Elvis.INSTANCE;
        System.out.println(e == l);
        e.leaveTheBuilding();
        l.leaveTheBuilding();

    }


}

enum Elvis{
    INSTANCE;

    public void leaveTheBuilding(){
        System.out.println("leave");
    }
}