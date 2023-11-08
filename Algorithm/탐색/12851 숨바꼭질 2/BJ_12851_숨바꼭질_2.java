import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_12851_숨바꼭질_2 {
    static int n;
    static int k;
    static int[] visited;
    static int minTime;
    static int count;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        minTime = Integer.MAX_VALUE;
        count = 0;
        visited = new int[100_001];
        if (n >= k) {
            System.out.println((n-k) + "\n1");
            return;
        }
        bfs();
        System.out.println(minTime);
        System.out.println(count);

    }

    private static void bfs(){
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            int currentPosition = queue.poll();
            if(currentPosition == k){
                minTime = visited[currentPosition];
                count++;
            }
            int next;
            for (int i = 0; i < 3; i++) {
                if(i == 0){
                    next = currentPosition + 1;
                } else if (i == 1) {
                    next = currentPosition - 1;
                }else{
                    next = currentPosition * 2;
                }

                if(0 > next || next > 100000){
                    continue;
                }
                if (visited[next] == 0 || visited[next] == visited[currentPosition] + 1) {
                    queue.add(next);
                    visited[next] = visited[currentPosition]+1;
                }
            }
        }
    }
}
