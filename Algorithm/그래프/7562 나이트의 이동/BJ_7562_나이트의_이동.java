import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_7562_나이트의_이동 {
    private static class Node{
        int x;
        int y;
        int depth;

        public Node(int x, int y, int depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
        }
    }
    static int[][] dxdy = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
    static int[][] board;
    static boolean[][] visited;
    static int l;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());
        for (int x = 0; x < t; x++) {
            l = Integer.parseInt(br.readLine());
            board = new int[l][l];
            visited = new boolean[l][l];
            st = new StringTokenizer(br.readLine());
            int startX = Integer.parseInt(st.nextToken());
            int startY = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine());
            int targetX = Integer.parseInt(st.nextToken());
            int targetY = Integer.parseInt(st.nextToken());
            System.out.println(bfs(new Node(startX, startY, 0), new int[]{targetX, targetY}));
        }
    }

    private static int bfs(Node start, int[] targetXY){
        Queue<Node> queue = new ArrayDeque<>();
        visited[start.x][start.y] = true;
        queue.add(start);
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            if(now.x == targetXY[0] && now.y == targetXY[1]){
                return now.depth;
            }
            for (int i = 0; i < 8; i++) {
                int dx = now.x + dxdy[i][0];
                int dy = now.y + dxdy[i][1];
                if(dx < 0 || dy < 0 || dx >= l || dy >= l){
                    continue;
                }
                if(!visited[dx][dy]){
                    visited[dx][dy] = true;
                    queue.add(new Node(dx, dy, now.depth+1));
                }
            }
        }
        return 0;
    }
}
