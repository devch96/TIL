import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_16993_벽_부수고_이동하기_3 {
    static class Node{
        int x;
        int y;
        int destroyed;
        int day;
        int move;

        public Node(int x, int y, int destroyed, int day, int move) {
            this.x = x;
            this.y = y;
            this.destroyed = destroyed;
            this.day = day;
            this.move = move;
        }
    }

    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int n;
    static int m;
    static int k;
    static int[][] map;
    static boolean[][][] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        visited = new boolean[n][m][k + 1];

        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < m; j++) {
                map[i][j] = str.charAt(j) - '0';
            }
        }

        System.out.println(bfs());
    }

    private static int bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(0, 0, 0, 0, 1));
        visited[0][0][0] = true;
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            int x = now.x;
            int y = now.y;
            int destroyed = now.destroyed;
            int move = now.move;
            int day = now.day;
            int nextDay;
            if (day == 0) {
                nextDay = 1;
            } else {
                nextDay = 0;
            }
            if (x == n - 1 && y == m - 1) {
                return move;
            }
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }
                if (map[nx][ny] == 0 && !visited[nx][ny][destroyed]) {
                    visited[nx][ny][destroyed] = true;
                    queue.offer(new Node(nx, ny, destroyed, nextDay, move + 1));
                } else if (map[nx][ny] == 1) {
                    if (destroyed < k && !visited[nx][ny][destroyed + 1]) {
                        if (day == 0) {
                            visited[nx][ny][destroyed + 1] = true;
                            queue.offer(new Node(nx, ny, destroyed + 1, nextDay, move + 1));
                        } else {
                            queue.offer(new Node(x, y, destroyed, nextDay, move + 1));
                        }
                    }
                }
            }
        }
        return -1;
    }
}
