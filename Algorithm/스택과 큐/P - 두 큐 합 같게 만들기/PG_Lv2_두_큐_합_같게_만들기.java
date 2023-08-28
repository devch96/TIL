import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;

public class PG_Lv2_두_큐_합_같게_만들기 {
    public static int solution(int[] queue1, int[] queue2){
        long sum1 = Arrays.stream(queue1).sum();
        long sum2 = Arrays.stream(queue2).sum();
        int count = 0;
        Queue<Long> q1 = new ArrayDeque<>(Arrays.stream(queue1).mapToLong(i -> Long.parseLong(String.valueOf(i))).boxed().collect(Collectors.toList()));
        Queue<Long> q2 = new ArrayDeque<>(Arrays.stream(queue2).mapToLong(i -> Long.parseLong(String.valueOf(i))).boxed().collect(Collectors.toList()));

        while(sum1 != sum2){
            if (count > queue1.length * 3) {
                break;
            }
            if (sum1 > sum2) {
                long n = q1.poll();
                sum1 -= n;
                sum2 += n;
                q2.add(n);
            }else{
                long n = q2.poll();
                sum1 += n;
                sum2 -= n;
                q1.add(n);
            }
            count++;
        }
        if(count > queue1.length * 3){
            return -1;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 2, 1, 2}, new int[]{1, 10, 1, 2}));
        System.out.println(solution(new int[]{3, 2, 7, 2}, new int[]{4, 6, 5, 1}));
        System.out.println(solution(new int[]{1,1}, new int[]{1,5}));
    }
}
