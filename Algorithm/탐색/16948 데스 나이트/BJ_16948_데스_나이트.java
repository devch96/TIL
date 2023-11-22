import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_16948_데스_나이트 {
    static int[][] move = {{-2, -1}, {-2, 1}, {0, -2}, {0, 2}, {2, -1}, {2, 1}};
    static class DeathNight{
        int x;
        int y;
        int count;

        public DeathNight(int x, int y, int count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }
    }
    static int r2;
    static int c2;
    static int n;
    static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        visited = new boolean[n][n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        int r1 = Integer.parseInt(st.nextToken());
        int c1 = Integer.parseInt(st.nextToken());
        r2 = Integer.parseInt(st.nextToken());
        c2 = Integer.parseInt(st.nextToken());
        System.out.println(bfs(new DeathNight(r1,c1,0)));
    }

    private static int bfs(DeathNight start){
        Queue<DeathNight> queue = new ArrayDeque<>();
        visited[start.x][start.y] = true;
        queue.offer(start);
        while (!queue.isEmpty()) {
            DeathNight now = queue.poll();
            int x = now.x;
            int y = now.y;
            int count = now.count;
            if (x == r2 && y == c2) {
                return count;
            }
            for (int[] dir : move) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (0 > nx || n <= nx || 0 > ny || n <= ny) {
                    continue;
                }
                if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(new DeathNight(nx, ny, count + 1));
                }
            }
        }
        return -1;
    }
}
