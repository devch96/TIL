import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_2146_다리_만들기 {
    static class Node{
        int x;
        int y;
        int depth;

        public Node(int x, int y, int depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
        }
    }
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int[][] graph;
    static boolean[][] visited;
    static int n;
    static int minValue;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        StringTokenizer st;
        graph = new int[n][n];
        visited = new boolean[n][n];
        minValue = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int color = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j] && graph[i][j] == 1){
                    separateGround(i,j,color);
                    color++;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (graph[i][j] != 0) {
                    visited = new boolean[n][n];
                    bfs(i,j);
                }
            }

        }
        System.out.println(minValue);
    }
    
    private static void separateGround(int x, int y, int color){
        visited[x][y] = true;
        graph[x][y] = color;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (0 <= nx && 0 <= ny && nx < n && ny < n && !visited[nx][ny] && graph[nx][ny] != 0) {
                separateGround(nx,ny,color);
            }
        }
    }


    private static void bfs(int x, int y){
        Queue<Node> queue = new ArrayDeque<>();
        visited[x][y] = true;
        queue.add(new Node(x,y,0));
        int currentLandNum = graph[x][y];
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            for (int i = 0; i < 4; i++) {
                int nx = now.x + dx[i];
                int ny = now.y + dy[i];
                if ((0 <= nx && 0 <= ny && nx < n && ny < n) && !visited[nx][ny] && graph[nx][ny] != currentLandNum) {
                    visited[nx][ny] = true;
                    if(graph[nx][ny] == 0){
                        queue.add(new Node(nx, ny, now.depth + 1));
                    }else{
                        minValue = Math.min(minValue, now.depth);
                    }
                }
            }
        }
    }
}
