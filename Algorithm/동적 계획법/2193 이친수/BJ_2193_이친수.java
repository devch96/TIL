import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_2193_이친수 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long[][] dp = new long[91][2];
        dp[1][1] = 1;
        dp[2][0] = 1;
        dp[3][1] = 1;
        dp[3][0] = 1;
        for (int i = 4; i <= 90; i++) {
            dp[i][0] = dp[i-1][0] + dp[i-1][1];
            dp[i][1] = dp[i-1][0];
        }
        int n = Integer.parseInt(br.readLine());
        System.out.println(dp[n][0] + dp[n][1]);
    }
}
