import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1285_동전_뒤집기 {
    static int n;
    static int[][] map;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        map = new int[n][n];
        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < n; j++) {
                char c = str.charAt(j);
                if (c == 'T') {
                    map[i][j] = 1;
                }
            }
        }

        int answer = Integer.MAX_VALUE;
        for (int bit = 0; bit < (1 << n) - 1; bit++) {
            int sum = 0;
            for (int col = 0; col < n; col++) {
                int tail = 0;
                for (int row = 0; row < n; row++) {
                    int cur = map[row][col];
                    if ((bit & (1 << row)) != 0) {
                        cur = flip(row,col);
                    }
                    if(cur == 1){
                        tail++;
                    }
                }
                sum += Math.min(tail, n - tail);
            }
            if(answer > sum){
                answer = sum;
            }
        }
        System.out.println(answer);
    }

    private static int flip(int row, int col){
        return map[row][col] ^ 1;
    }
}
