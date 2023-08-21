import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_7569_토마토 {
    static class Position{
        int height;
        int row;
        int col;

        public Position(int height, int row, int col) {
            this.height = height;
            this.row = row;
            this.col = col;
        }
    }
    static int m;
    static int n;
    static int h;
    static int[][][] graph;
    static int rowArr[] = {-1, 0, 1, 0, 0, 0};
    static int colArr[] = {0, 1, 0, -1, 0, 0};
    static int heightArr[] = {0, 0, 0, 0, 1, -1};
    static Queue<Position> queue = new ArrayDeque<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        m = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());

        graph = new int[h][n][m];


        for (int i = 0; i < h; i++) {
            for (int j = 0; j < n; j++) {
                st = new StringTokenizer(br.readLine());
                for (int k = 0; k < m; k++) {
                    int tomato = Integer.parseInt(st.nextToken());
                    if(tomato == 1){
                        queue.add(new Position(i, j, k));
                    }
                    graph[i][j][k] = tomato;
                }
            }
        }
        System.out.println(bfs());
    }

    private static int bfs(){
        while (!queue.isEmpty()) {
            Position now = queue.poll();

            int height = now.height;
            int row = now.row;
            int col = now.col;

            for (int i = 0; i < 6; i++) {
                int nHeight = height + heightArr[i];
                int nRow = row + rowArr[i];
                int nCol = col + colArr[i];
                if (check(nHeight, nRow, nCol)) {
                    queue.add(new Position(nHeight, nRow, nCol));
                    graph[nHeight][nRow][nCol] = graph[height][row][col] + 1;
                }
            }
        }

        int result = Integer.MIN_VALUE;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    if(graph[i][j][k] == 0){
                        return -1;
                    }
                    result = Math.max(result, graph[i][j][k]);
                }
            }
        }
        if(result == 1){
            return 0;
        }else{
            return result-1;
        }
    }

    private static boolean check(int height, int row, int col) {
        if(height < 0 || height > h-1 || row < 0 || row > n-1 || col < 0 || col > m-1) {
            return false;
        }
        if(graph[height][row][col] == 0) {
            return true;
        } else {
            return false;
        }
    }
}
