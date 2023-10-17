import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_15988_1_2_3_더하기_3 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        final long mod = 1_000_000_009;
        long[] dp = new long[1_000_001];
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 4;
        for (int i = 4; i < dp.length; i++) {
            dp[i] = ((dp[i-3] % mod) + (dp[i-2] % mod) + (dp[i-1] % mod)) % mod;
        }
        for (int i = 0; i < t; i++) {
            System.out.println(dp[Integer.parseInt(br.readLine())]);
        }
    }
}
