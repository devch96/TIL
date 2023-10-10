import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BJ_1158_요세푸스_문제 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Queue<Integer> queue = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder answer = new StringBuilder();
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        for (int i = 1; i <= n; i++) {
            queue.offer(i);
        }

        answer.append("<");
        while (queue.size() != 1) {
            for (int i = 0; i < m - 1; i++) {
                queue.offer(queue.poll());
            }
            answer.append(queue.poll()).append(", ");
        }
        answer.append(queue.poll()).append(">");
        System.out.println(answer);
    }
}
