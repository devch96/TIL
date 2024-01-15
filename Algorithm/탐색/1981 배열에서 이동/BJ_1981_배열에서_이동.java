import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1981_배열에서_이동 {
    static class Node{
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static int answer;
    static int n;
    static int[][] graph;
    static boolean[][] visited;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int min;
    static int max;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        answer = 200;
        min = 1000;
        max = 0;
        graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
                min = Math.min(min, graph[i][j]);
                max = Math.max(max, graph[i][j]);
            }
        }
        binarySearch(0, max-min);
        System.out.println(answer);
    }

    private static void binarySearch(int start, int end){
        while (start <= end) {
            int mid = (start + end) / 2;
            boolean flag = false;
            for (int i = min; i <= max; i++) {
                if (bfs(i, i + mid)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                end = mid-1;
                answer = Math.min(answer, mid);
            } else {
                start = mid + 1;
            }
        }
    }

    private static boolean bfs(int start, int end){
        if (graph[0][0] < start || graph[0][0] > end) {
            return false;
        }
        visited = new boolean[n][n];
        visited[0][0] = true;
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(0, 0));
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            int x = now.x;
            int y = now.y;
            if (x == n - 1 && y == n - 1) {
                return true;
            }
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || ny < 0 || nx >= n || ny >= n) {
                    continue;
                }
                if (!visited[nx][ny]) {
                    if (graph[nx][ny] >= start && graph[nx][ny] <= end) {
                        visited[nx][ny] = true;
                        queue.offer(new Node(nx, ny));
                    }
                }
            }
        }
        return false;
    }
}
