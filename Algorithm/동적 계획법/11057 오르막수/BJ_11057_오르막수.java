import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_11057_오르막수 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        final long mod = 10_007;
        long[][] dp = new long[1001][10];
        for (int i = 0; i < 10; i++) {
            dp[1][i] = 1;
        }
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k <= j; k++) {
                    dp[i][j] += dp[i-1][k] % mod;
                }
            }
        }
        long answer = 0;
        for (int i = 0; i < 10; i++) {
            answer += dp[n][i] % mod;
        }
        System.out.println(answer % mod);
    }
}
