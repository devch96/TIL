import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_16195_1_2_3_더하기_9 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final long mod = 1_000_000_009;
        long[][] dp = new long[1001][1001];
        dp[1][1] = 1;
        dp[2][1] = 1;
        dp[2][2] = 1;
        dp[3][1] = 1;
        dp[3][2] = 2;
        dp[3][3] = 1;
        for (int i = 4; i < 1001; i++) {
            for (int j = i; j > 0; j--) {
                dp[i][j] = (dp[i-1][j-1] + dp[i-2][j-1] + dp[i-3][j-1]) % mod;
            }
        }
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            long answer = 0;
            for (int j = 1; j <= m; j++) {
                answer += dp[n][j];
            }
            System.out.println(answer % mod);
        }
    }
}
