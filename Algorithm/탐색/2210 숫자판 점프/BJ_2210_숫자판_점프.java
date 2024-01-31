import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class BJ_2210_숫자판_점프 {
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static Set<String> answers = new HashSet<>();
    static int[][] board;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        board = new int[5][5];
        StringTokenizer st;
        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                dfs(String.valueOf(board[i][j]), i, j, 0);
            }
        }
        System.out.println(answers.size());
    }

    private static void dfs(String str, int x, int y, int depth) {
        if (depth == 5) {
            answers.add(str);
            return;
        }
        for (int i = 0; i < 4; i++) {
            int nx = dx[i] + x;
            int ny = dy[i] + y;
            if (nx < 0 || nx >= 5 || ny < 0 || ny >= 5) {
                continue;
            }
            dfs(str + String.valueOf(board[nx][ny]), nx, ny, depth + 1);
        }
    }
}
