import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_3085_사탕_게임 {
    static char[][] board;
    static int n;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {-1, 0, 1, 0};
    static int maxValue = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());

        board = new char[n][n];
        for (int i = 0; i < n; i++) {
            String input = br.readLine().strip();
            for (int j = 0; j < input.length(); j++) {
                board[i][j] = input.charAt(j);
            }
        }
        bruteforce();
        System.out.println(maxValue);

    }

    private static void bruteforce(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = board[i][j];
                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if (0 <= nx && nx < n && 0 <= ny && ny < n) {
                        char c1 = board[nx][ny];
                        if(c != c1){
                            board[i][j] = c1;
                            board[nx][ny] = c;
                            count();
                            board[i][j] = c;
                            board[nx][ny] = c1;
                        }
                    }
                }
            }
        }
    }

    private static void count(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 4; k++) {
                    int tempCount = 1;
                    int goX = i;
                    int goY = j;
                    while(true){
                        goX += dx[k];
                        goY += dy[k];
                        if (0 <= goX && goX < n && 0 <= goY && goY < n && board[i][j] == board[goX][goY]) {
                            tempCount++;
                        }else{
                            break;
                        }
                    }
                    maxValue = Math.max(maxValue, tempCount);
                }
            }
        }

    }
}
