import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BJ_10026_적록색약 {
    static boolean[][] visited;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};
    static int n;
    static int count;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        char[][] graph = new char[n][n];
        char[][] graph2 = new char[n][n];

        visited = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            String line = br.readLine().strip();
            for (int j = 0; j < n; j++) {
                char c = line.charAt(j);
                graph[i][j] = c;
                if(c == 'R'){
                    c = 'G';
                }
                graph2[i][j] = c;
            }
        }
        List<Integer> result = new ArrayList<>();
        int[] answer = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j]){
                    count = 0;
                    dfs(i,j, graph);
                    result.add(count);
                }
            }
        }
        answer[0] = result.size();
        result.clear();
        visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j]){
                    count = 0;
                    dfs(i,j,graph2);
                    result.add(count);
                }
            }
        }
        answer[1] = result.size();
        System.out.println(answer[0] + " " + answer[1]);
    }

    private static void dfs(int x, int y, char[][] graph){
        visited[x][y] = true;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (0<=nx && nx < n && 0<= ny && ny < n) {
                if(!visited[nx][ny] && graph[x][y] == graph[nx][ny]){
                    count++;
                    dfs(nx,ny, graph);
                }
            }
        }
    }
}
