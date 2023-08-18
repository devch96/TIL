import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_2667_단지번호붙이기 {
    static int[][] graph;
    static boolean[][] visited;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};
    static int n;
    static int count;
    static List<Integer> result;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        graph = new int[n][n];
        visited = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            String line = br.readLine().strip();
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }
        result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!visited[i][j] && graph[i][j] == 1) {
                    count = 1;
                    dfs(i,j);
                    result.add(count);
                }
            }
        }
        Collections.sort(result);
        System.out.println(result.size());
        for (int i : result) {
            System.out.println(i);
        }
    }

    private static void dfs(int x, int y) {
        visited[x][y] = true;
        for (int i = 0; i < 4; i++) {
            int nx = x+dx[i];
            int ny = y+dy[i];
            if (0 <= nx && nx < n && 0 <= ny && ny < n && !visited[nx][ny] && graph[nx][ny] != 0) {
                count++;
                dfs(nx, ny);
            }
        }
    }
}
