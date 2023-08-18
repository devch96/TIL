import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_14940_쉬운_최단거리 {
    static int[][] graph;
    static boolean[][] visited;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        graph = new int[n][m];
        visited = new boolean[n][m];
        int[] start = new int[2];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                int ground = Integer.parseInt(st.nextToken());
                if (ground == 2) {
                    start[0] = i;
                    start[1] = j;
                }
                graph[i][j] = ground;
            }
        }

        visited[start[0]][start[1]] = true;
        graph[start[0]][start[1]] = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            int[] now = queue.poll();
            int x = now[0];
            int y = now[1];
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (0 <= nx && nx < n && 0 <= ny && ny < m && graph[nx][ny] != 0) {
                    if (!visited[nx][ny]) {
                        visited[nx][ny] = true;
                        queue.add(new int[]{nx, ny});
                        graph[nx][ny] = graph[x][y]+1;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(graph[i][j] == 1 && visited[i][j] == false){
                    System.out.print("-1 ");
                }else{
                    System.out.print(graph[i][j]+" ");
                }
            }
            System.out.println();
        }
    }
}
