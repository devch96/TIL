import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1987_알파벳 {
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static boolean[] visited;
    static int r;
    static int c;
    static char[][] board;
    static int maxValue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        visited = new boolean[26];
        StringTokenizer st = new StringTokenizer(br.readLine());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        board = new char[r][c];

        for (int i = 0; i < r; i++) {
            String str = br.readLine();
            for (int j = 0; j < c; j++) {
                board[i][j] = str.charAt(j);
            }
        }
        visited[board[0][0] - 65] = true;
        backtrack(1,0,0);
        System.out.println(maxValue);
    }

    private static void backtrack(int count, int x, int y){
        maxValue = Math.max(maxValue,count);
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx < 0 || nx >= r || ny < 0 || ny >= c) {
                continue;
            }
            if (!visited[board[nx][ny] - 65]) {
                visited[board[nx][ny] - 65] = true;
                backtrack(count+1,nx,ny);
                visited[board[nx][ny] - 65] = false;
            }
        }
    }
}
