import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_14502_연구소 {
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int[][] lab;
    static boolean[][] wall;
    static int maxValue;
    static int n;
    static int m;
    static List<int[]> virusLocation;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        lab = new int[n][m];
        wall = new boolean[n][m];
        maxValue = Integer.MIN_VALUE;
        virusLocation = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                int num = Integer.parseInt(st.nextToken());
                if(num == 2){
                    virusLocation.add(new int[]{i, j});
                }
                lab[i][j] = num;
            }
        }
        buildWall(0);
        System.out.println(maxValue);
    }

    private static void buildWall(int numOfWall){
        if(numOfWall == 3){
            maxValue = Math.max(maxValue, countSafeArea());
            return;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(!wall[i][j] && lab[i][j] == 0){
                    wall[i][j] = true;
                    buildWall(numOfWall+1);
                    wall[i][j] = false;
                }
            }
        }
    }

    private static int countSafeArea(){
        int result = 0;
        int[][] infect = infectedLab();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(infect[i][j] == 0 && !wall[i][j]){
                    result++;
                }
            }
        }
        return result;
    }

    private static int[][] infectedLab(){
        int[][] temp = new int[n][m];
        boolean[][] visited = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            temp[i] = lab[i].clone();
        }
        Queue<int[]> queue = new ArrayDeque<>(virusLocation);
        while (!queue.isEmpty()) {
            int[] nowVirus = queue.poll();
            int x = nowVirus[0];
            int y = nowVirus[1];
            visited[x][y] = true;
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (0 > nx || nx >= n || 0 > ny || ny >= m) {
                    continue;
                }
                if (!visited[nx][ny] && temp[nx][ny] != 1 && !wall[nx][ny]) {
                    visited[nx][ny] = true;
                    temp[nx][ny] = 2;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        return temp;
    }
}
