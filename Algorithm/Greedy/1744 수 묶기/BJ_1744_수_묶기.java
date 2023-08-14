import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class BJ_1744_수_묶기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int zero = 0;
        int one = 0;
        int answer = 0;
        Queue<Integer> plusQueue = new PriorityQueue<>(Collections.reverseOrder());
        Queue<Integer> minusQueue = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            int input = Integer.parseInt(br.readLine());
            if(input < 0){
                minusQueue.add(input);
            } else if (input == 0) {
                zero++;
            } else if (input == 1) {
                one++;
            }else{
                plusQueue.add(input);
            }
        }
        while(minusQueue.size() > 1){
            int first = minusQueue.poll();
            int second = minusQueue.poll();
            answer += (first * second);
        }
        if (!minusQueue.isEmpty()){
            if(zero == 0){
                answer += minusQueue.poll();
            }
        }
        while(plusQueue.size() > 1){
            int first = plusQueue.poll();
            int second = plusQueue.poll();
            answer += (first * second);
        }
        if (!plusQueue.isEmpty()){
            answer += plusQueue.poll();
        }

        answer += one;
        System.out.println(answer);
    }
}
