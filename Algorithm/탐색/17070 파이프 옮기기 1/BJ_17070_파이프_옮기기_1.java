import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_17070_파이프_옮기기_1 {
    static int n;
    static int[][] map;
    static int answer;
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        map = new int[n][n];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        dfs(0,1,0);
        System.out.println(answer);
    }

    private static void dfs(int x, int y, int direction) {
        if (x == n - 1 && y == n - 1) {
            answer++;
            return;
        }
        switch (direction) {
            case 0:
                if (canPlacePipeRight(x,y)) {
                    dfs(x, y + 1, 0);
                }
                break;
            case 1:
                if (canPlacePipeDown(x,y)) {
                    dfs(x+1, y, 1);
                }
                break;
            case 2:
                if (canPlacePipeRight(x,y)) {
                    dfs(x, y+1, 0);
                }
                if (canPlacePipeDown(x,y)) {
                    dfs(x+1, y, 1);
                }
                break;
        }
        if (canPlacePipeDiagonal(x, y)) {
            dfs(x+1, y+1, 2);
        }
    }

    private static boolean canPlacePipeRight(int x, int y) {
        return y + 1 < n && map[x][y + 1] == 0;
    }

    private static boolean canPlacePipeDown(int x, int y) {
        return x + 1 < n && map[x+1][y] == 0;
    }

    private static boolean canPlacePipeDiagonal(int x, int y) {
        return y + 1 < n && x + 1 < n && map[x][y + 1] == 0 && map[x + 1][y] == 0 && map[x + 1][y + 1] == 0;
    }
}
