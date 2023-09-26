import java.util.*;

public class PG_Lv2_카카오프렌즈_컬러링북 {
    static int numberOfArea;
    static int maxSizeOfOneArea;
    static int[][] graph;
    static boolean[][] visited;

    static int count;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static int[] solution(int m, int n, int[][] picture){
        graph = picture;
        visited = new boolean[m][n];
        numberOfArea = 0;
        maxSizeOfOneArea = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] != 0 && !visited[i][j]) {
                    numberOfArea++;
                    count = 0;
                    dfs(i,j);
                    maxSizeOfOneArea = Math.max(maxSizeOfOneArea, count);
                }
            }
        }
        return new int[]{numberOfArea, maxSizeOfOneArea};
    }

    private static void dfs(int x, int y){
        visited[x][y] = true;
        count++;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (0 <= nx && nx < graph.length && 0 <= ny && ny < graph[0].length && !visited[nx][ny]) {
                if (graph[x][y] == graph[nx][ny]) {
                    dfs(nx,ny);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(6,4,new int[][] {{1,1,1,0},{1,2,2,0},{1,0,0,1},{0,0,0,1},{0,0,0,3},{0,0,0,3}})));
    }
}
