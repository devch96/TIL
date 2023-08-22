import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_16953_A_B {
    static long a, b;
    static int cnt;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        a = Long.parseLong(st.nextToken());
        b = Long.parseLong(st.nextToken());

        System.out.println(bfs());
    }

    private static int bfs(){
        Queue<Long> queue = new ArrayDeque<>();
        queue.add(a);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                long now = queue.poll();
                if(now == b){
                    return cnt+1;
                }
                if(now * 2 <= b){
                    queue.add(now * 2);
                }
                if (now * 10 + 1 <= b) {
                    queue.add(now * 10 + 1);
                }
            }
            cnt++;
        }
        return -1;
    }
}
