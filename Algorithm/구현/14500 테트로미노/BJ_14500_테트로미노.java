import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_14500_테트로미노 {
    static int[][] graph;
    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {1, 0, -1, 0};
    static int n;
    static int m;
    static int max;
    static boolean[][] visited;


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        graph = new int[n][m];
        visited = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                visited[i][j] = true;
                dfs(i, j, graph[i][j], 1);
                visited[i][j] = false;
            }
        }
        System.out.println(max);
    }

    private static void dfs(int x, int y, int value, int depth) {
        if (depth == 4) {
            max = Math.max(max, value);
            return;
        }
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (0 <= nx && nx < n && 0 <= ny && ny < m && !visited[nx][ny]) {
                if(depth == 2){
                    visited[nx][ny] = true;
                    dfs(x, y, value + graph[nx][ny], depth + 1);
                    visited[nx][ny] = false;
                }
                visited[nx][ny] = true;
                dfs(nx, ny, value + graph[nx][ny], depth + 1);
                visited[nx][ny] = false;
            }
        }
    }
}
