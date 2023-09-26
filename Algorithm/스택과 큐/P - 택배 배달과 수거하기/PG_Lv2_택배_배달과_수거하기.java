import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Collectors;

public class PG_Lv2_택배_배달과_수거하기 {
    public static long solution(int cap, int n, int[] deliveries, int[] pickups) {
        Deque<int[]> deliverStack = new ArrayDeque<>();
        Deque<int[]> pickupStack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if(deliveries[i] > 0){
                deliverStack.add(new int[]{deliveries[i], i + 1});
            }
            if (pickups[i] > 0) {
                pickupStack.add(new int[]{pickups[i], i + 1});
            }
        }
        long answer = 0;
        while (!deliverStack.isEmpty() || !pickupStack.isEmpty()) {
            int capacity = 0;
            long distance = 0;
            while(capacity < cap && !deliverStack.isEmpty()){
                int[] deliverHome = deliverStack.pollLast();
                distance = Math.max(distance, deliverHome[1]);
                if(capacity + deliverHome[0] <= cap){
                    capacity += deliverHome[0];
                }else{
                    deliverHome[0] -= (cap - capacity);
                    deliverStack.addLast(deliverHome);
                    break;
                }
            }
            capacity = 0;
            while (capacity < cap && !pickupStack.isEmpty()) {
                int[] pickupHome = pickupStack.pollLast();
                distance = Math.max(distance, pickupHome[1]);
                if(capacity + pickupHome[0] <= cap){
                    capacity += pickupHome[0];
                }else{
                    pickupHome[0] -= (cap - capacity);
                    pickupStack.addLast(pickupHome);
                    break;
                }
            }
            answer += distance * 2;
        }
        return answer;
    }
    public static long solution2(int cap, int n, int[] deliveries, int[] pickups) {
        Deque<Integer> deliverStack = Arrays.stream(deliveries).boxed().collect(Collectors.toCollection(ArrayDeque::new));
        Deque<Integer> pickupStack = Arrays.stream(pickups).boxed().collect(Collectors.toCollection(ArrayDeque::new));
        long answer = 0;
        while (!deliverStack.isEmpty() || !pickupStack.isEmpty()) {
            int deliverCapacity = 0;
            int pickupCapacity = 0;
            long distance = 0;
            while(deliverCapacity < cap && !deliverStack.isEmpty()){
                if(deliverStack.peekLast() != 0){
                    distance = Math.max(distance, deliverStack.size());
                }
                int lastHomeDeliverBox = deliverStack.pollLast();
                if(deliverCapacity + lastHomeDeliverBox <= cap){
                    deliverCapacity += lastHomeDeliverBox;
                }else{
                    lastHomeDeliverBox -= (cap - deliverCapacity);
                    deliverStack.addLast(lastHomeDeliverBox);
                    break;
                }
            }
            while (pickupCapacity < cap && !pickupStack.isEmpty()) {
                if(pickupStack.peekLast() != 0){
                    distance = Math.max(distance, pickupStack.size());
                }
                int lastHomePickUpBox = pickupStack.pollLast();
                if(pickupCapacity + lastHomePickUpBox <= cap){
                    pickupCapacity += lastHomePickUpBox;
                }else{
                    lastHomePickUpBox -= (cap - pickupCapacity);
                    pickupStack.addLast(lastHomePickUpBox);
                    break;
                }
            }
            answer += distance * 2;
        }
        return answer;
    }

    public static void main(String[] args) {
//        System.out.println(solution(4,5,new int[] {1,0,3,1,2},new int[]{0,3,0,4,0}));
        System.out.println(solution2(2,7,new int[] {1,0,2,0,1,0,2},new int[]{0,2,0,1,0,2,0}));
    }
}
