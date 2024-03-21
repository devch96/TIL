import java.util.ArrayDeque;
import java.util.Deque;

public class PG_Lv3_징검다리_건너기 {
    static class Data {
        int value;
        int index;

        public Data(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    public int solution(int[] stones, int k) {
        int answer = Integer.MAX_VALUE;
        Deque<Data> dq = new ArrayDeque<>();
        for(int i = 0; i < stones.length; i++) {
            int stone = stones[i];
            while(!dq.isEmpty() && i - dq.peekFirst().index >= k) {
                dq.pollFirst();
            }

            while (!dq.isEmpty() && stone > dq.peekLast().value) {
                dq.pollLast();
            }

            dq.addLast(new Data(stone, i));

            if (i >= k - 1) {
                answer = Math.min(answer, dq.peekFirst().value);
            }
        }
        if(answer == Integer.MAX_VALUE) {
            return 0;
        }
        return answer;
    }

    public static void main(String[] args) {
        PG_Lv3_징검다리_건너기 p = new PG_Lv3_징검다리_건너기();
        p.solution(new int[]{2, 4, 5, 3, 2, 1, 4, 2, 5, 1}, 5);
    }
}
