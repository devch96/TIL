import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_4963_섬의_개수 {
    static int[] dx = {1, 1, 0, -1,-1,-1,0,1};
    static int[] dy = {0, -1, -1, -1,0,1,1,1};
    static int w;
    static int h;
    static int[][] graph;
    static boolean[][] visited;
    static int answer;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        while(true){
            st = new StringTokenizer(br.readLine());
            w = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());
            if(w == 0 && h == 0){
                return;
            }

            graph = new int[h][w];
            answer = 0;
            visited = new boolean[h][w];

            for (int i = 0; i < h; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < w; j++) {
                    graph[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    if (!visited[i][j] && graph[i][j] == 1) {
                        dfs(i,j);
                        answer++;
                    }
                }
            }
            System.out.println(answer);
        }
    }

    private static void dfs(int x, int y){
        visited[x][y] = true;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(nx < 0 || ny < 0 || nx >= h || ny >= w){
                continue;
            }
            if (!visited[nx][ny] && graph[nx][ny] == 1) {
                dfs(nx,ny);
            }
        }
    }
}
