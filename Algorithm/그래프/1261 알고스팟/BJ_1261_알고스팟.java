import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1261_알고스팟 {
    static class Node implements Comparable<Node> {
        int x;
        int y;
        int count;

        public Node(int x, int y, int count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.count, o.count);
        }
    }
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int[][] graph;
    static boolean[][] visited;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        graph = new int[m][n];
        visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            String str = br.readLine().strip();
            for (int j = 0; j < n; j++) {
                graph[i][j] = str.charAt(j) - '0';
            }
        }
        bfs();
    }

    private static void bfs(){
        Queue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, 0, 0));
        visited[0][0] = true;
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            int x = curNode.x;
            int y = curNode.y;
            int count = curNode.count;
            if (x == n - 1 && y == m - 1) {
                System.out.println(count);
                return;
            }
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if(nx <0 || ny <0 || nx > n-1 || ny > m-1) {
                    continue;
                }
                if (!visited[ny][nx]) {
                    visited[ny][nx] = true;
                    if (graph[ny][nx] == 0) {
                        queue.add(new Node(nx, ny, count));
                    }else{
                        queue.add(new Node(nx, ny, count + 1));
                    }
                }
            }
        }
    }
}
