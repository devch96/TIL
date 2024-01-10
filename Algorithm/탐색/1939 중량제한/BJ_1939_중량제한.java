import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1939_중량제한 {
    static int start;
    static int destination;
    static int n;
    static int m;
    static int answer;
    static class Bridge{
        int destination;
        int maxWeight;

        public Bridge(int destination, int maxWeight) {
            this.destination = destination;
            this.maxWeight = maxWeight;
        }
    }
    static List<Bridge>[] arrList;
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arrList = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());
            int maxWeight = Integer.parseInt(st.nextToken());
            arrList[start].add(new Bridge(destination, maxWeight));
            arrList[destination].add(new Bridge(start, maxWeight));
        }
        st = new StringTokenizer(br.readLine());
        start = Integer.parseInt(st.nextToken());
        destination = Integer.parseInt(st.nextToken());
        binarySearch();
        System.out.println(answer);
    }

    private static void binarySearch(){
        int low = 1;
        int high = 1_000_000_000;
        while(low <= high){
            int mid = high + (low - high)/2;
            if (canMove(mid)) {
                answer = Math.max(answer, mid);
                low = mid+1;
            }else{
                high = mid-1;
            }
        }
    }

    private static boolean canMove(int weight){
        boolean[] visited = new boolean[n+1];
        Queue<Bridge> queue = new ArrayDeque<>();
        visited[start] = true;
        queue.add(new Bridge(start, 0));
        while (!queue.isEmpty()) {
            Bridge now = queue.poll();
            int cur = now.destination;
            if (cur == destination) {
                return true;
            }
            for (Bridge next : arrList[cur]) {
                if (next.maxWeight >= weight && !visited[next.destination]) {
                    visited[next.destination] = true;
                    queue.add(next);
                }
            }
        }
        return false;
    }
}
