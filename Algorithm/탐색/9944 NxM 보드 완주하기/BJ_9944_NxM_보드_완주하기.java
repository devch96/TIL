import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_9944_NxM_보드_완주하기 {
    static int n;
    static int m;
    static int dotNum;
    static int min;
    static char[][] map;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int tc = 1;
        String input = "";
        while ((input = br.readLine()) != null) {
            st = new StringTokenizer(input);
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            map = new char[n][m];
            dotNum = 0;
            min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                String tmp = st.nextToken();
                for (int j = 0; j < m; j++) {
                    map[i][j] = tmp.charAt(j);
                    if (map[i][j] == '.') {
                        dotNum++;
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (map[i][j] != '*') {
                        boolean[][] visited = new boolean[n][m];
                        visited[i][j] = true;
                        dfs(i, j, visited, 1, 0);
                        visited[i][j] = false;
                    }
                }
            }
            if (min == Integer.MAX_VALUE) {
                System.out.println("Case " + tc + ": -1");
            } else {
                System.out.println("Case " + tc + ": " + min);
            }
            tc++;
        }
    }

    private static void dfs(int x, int y, boolean[][] visited, int dot, int edge) {
        if (dot == dotNum) {
            min = Math.min(min, edge);
            return;
        }
        for (int i = 0; i < 4; i++) {
            int cnt = 0;
            int cx = x;
            int cy = y;
            while(true) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m || visited[nx][ny] || map[nx][ny] == '*') {
                    break;
                }
                cx = nx;
                cy = ny;
                visited[nx][ny] = true;
                cnt++;
            }
            if (x == cx && y == cy) {
                continue;
            } else {
                dfs(cx, cy, visited, dot + cnt, edge + 1);
                for (int j = 1; j <= cnt ; j++) {
                    int tx = x + dx[i] * j;
                    int ty = y + dy[i] * j;
                    visited[tx][ty] = false;
                }
            }
        }
    }
}
