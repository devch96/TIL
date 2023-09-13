import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class PG_Lv2_디펜스_게임 {
    public static int solution(int n, int k, int[] enemy){
        Queue<Integer> queue = new PriorityQueue<>();
        for (int round = 0; round < enemy.length; round++) {
            queue.add(enemy[round]);
            if(queue.size() > k){
                n -= queue.poll();
            }
            if(n < 0){
                return round;
            }
        }
        return enemy.length;
    }


    public static void main(String[] args) {
        System.out.println(solution(7,3,new int[]{4,2,4,5,3,3,1}));
    }
}
