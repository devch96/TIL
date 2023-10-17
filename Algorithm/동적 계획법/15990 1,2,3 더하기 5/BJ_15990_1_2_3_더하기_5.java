import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_15990_1_2_3_더하기_5 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long[][] dp = new long[100_001][4];
        final long mod = 1_000_000_009;
        dp[1][1] = 1;
        dp[2][2] = 1;
        dp[3][1] = 1;
        dp[3][2] = 1;
        dp[3][3] = 1;
        for (int i = 4; i <= 100000; i++) {
            dp[i][1] = ((dp[i - 1][2] % mod) + (dp[i - 1][3] % mod)) % mod;
            dp[i][2] = ((dp[i - 2][1] % mod) + (dp[i - 2][3] % mod)) % mod;
            dp[i][3] = ((dp[i - 3][1] % mod) + (dp[i - 3][2] % mod)) % mod;
        }
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int num = Integer.parseInt(br.readLine());
            System.out.println((dp[num][1] + dp[num][2] + dp[num][3]) % mod);
        }
    }
}
