import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_16929_Two_Dots {
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int n;
    static int m;
    static int startX;
    static int startY;
    static char[][] board;
    static boolean[][] visited;
    static boolean flag;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new char[n][m];
        flag = false;
        for (int i = 0; i < n; i++) {
            String str = br.readLine().strip();
            for (int j = 0; j < m; j++) {
                board[i][j] = str.charAt(j);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                visited = new boolean[n][m];
                startX = i;
                startY = j;
                dfs(i, j, 1);
            }
        }
        System.out.println(flag ? "Yes" : "No");
    }

    private static void dfs(int nowX, int nowY, int depth){
        visited[nowX][nowY] = true;
        for (int i = 0; i < 4; i++) {
            int nx = nowX + dx[i];
            int ny = nowY + dy[i];
            if (nx >= 0 && ny >= 0 && nx < n && ny < m && board[nowX][nowY] == board[nx][ny]) {
                if(!visited[nx][ny]){
                    dfs(nx,ny,depth+1);
                }else if(nx == startX && ny == startY && depth >= 4){
                    flag = true;
                    return;
                }
            }
        }
    }
}
