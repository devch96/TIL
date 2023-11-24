import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_2206_벽_부수고_이동하기 {
    static class Node{
        int x;
        int y;
        int count;
        boolean brokenWall;

        public Node(int x, int y, int count, boolean brokenWall) {
            this.x = x;
            this.y = y;
            this.count = count;
            this.brokenWall = brokenWall;
        }
    }

    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int[][] map;
    static int minValue;
    static int n;
    static int m;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        minValue = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < m; j++) {
                map[i][j] = str.charAt(j) - '0';
            }
        }
        System.out.println(bfs(new Node(0,0,1,false),n-1,m-1));
    }

    private static int bfs(Node start,int targetX, int targetY){
        boolean[][][] visited = new boolean[n][m][2];
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(start);
        visited[0][0][0] = true;
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            int x = now.x;
            int y = now.y;
            int count = now.count;
            boolean brokenWall = now.brokenWall;
            if (x == targetX && y == targetY) {
                return count;
            }
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }
                if (map[nx][ny] == 0) {
                    if (!brokenWall && !visited[nx][ny][0]) {
                        visited[nx][ny][0] = true;
                        queue.add(new Node(nx, ny, count + 1, false));
                    } else if (brokenWall && !visited[nx][ny][1]) {
                        visited[nx][ny][1] = true;
                        queue.add(new Node(nx, ny, count + 1, true));
                    }
                } else {
                    if (!brokenWall) {
                        visited[nx][ny][1] = true;
                        queue.add(new Node(nx, ny, count + 1, true));
                    }
                }
            }
        }
        return -1;
    }
}
