import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_21736_헌내기는_친구가_필요해 {
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static char[][] graph;
    static boolean[][] visited;
    static int result;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        graph = new char[n][m];
        visited = new boolean[n][m];
        result = 0;
        int[] start = new int[2];

        for (int i = 0; i < n; i++) {
            String line = br.readLine().strip();
            for (int j = 0; j < m; j++) {
                char c = line.charAt(j);
                if (c == 'I') {
                    start[0] = i;
                    start[1] = j;
                }
                graph[i][j] = line.charAt(j);
            }
        }
        dfs(start);
        if(result != 0){
            System.out.println(result);
        }else{
            System.out.println("TT");
        }
    }

    private static void dfs(int[] xy){
        int x = xy[0];
        int y = xy[1];
        visited[x][y] = true;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (0 <= nx && nx < n && 0 <= ny && ny < m && graph[nx][ny] != 'X' && !visited[nx][ny]) {
                if (graph[nx][ny] == 'P') {
                    result++;
                }
                dfs(new int[]{nx, ny});
            }
        }
    }
}
