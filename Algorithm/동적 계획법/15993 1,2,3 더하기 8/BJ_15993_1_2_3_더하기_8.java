import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_15993_1_2_3_더하기_8 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final long mod = 1_000_000_009;
        long[][] dp = new long[100_001][2];
        dp[1][1] = 1;
        dp[2][0] = 1;
        dp[2][1] = 1;
        dp[3][0] = 2;
        dp[3][1] = 2;
        for (int i = 4; i < 100001; i++) {
            dp[i][1] = (dp[i-1][0] + dp[i-2][0] + dp[i-3][0]) % mod;
            dp[i][0] = (dp[i-1][1] + dp[i-2][1] + dp[i-3][1]) % mod;
        }
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int num = Integer.parseInt(br.readLine());
            long odd = dp[num][1] % mod;
            long even = dp[num][0] % mod;
            System.out.println(odd + " " + even);
        }
    }
}
