import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class PG_Lv3_야근_지수 {
    public long solution(int n, int[] works) {
        long answer = 0;
        Queue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for(int work : works) {
            pq.add(work);
        }
        for (int i = 0; i < n; i++) {
            int work = pq.poll();
            if(work == 0){
                return 0;
            }
            pq.add(work-1);
        }
        while (!pq.isEmpty()) {
            answer += (Math.pow(pq.poll(),2));
        }
        return answer;
    }

    public static void main(String[] args) {
        PG_Lv3_야근_지수 p = new PG_Lv3_야근_지수();
        System.out.println(p.solution(4,new int[] {4,3,3}));
    }
}
