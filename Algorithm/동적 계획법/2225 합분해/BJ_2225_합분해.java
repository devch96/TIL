import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2225_합분해 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        final long mod = 1_000_000_000;
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        long[][] dp = new long[201][201];
        for (int i = 0; i < 201; i++) {
            dp[1][i] = i;
        }
        for (int i = 2; i < 201; i++) {
            for (int j = 1; j < 201; j++) {
                dp[i][j] = (dp[i][j-1] + dp[i-1][j]) % mod;
            }
        }

        System.out.println(dp[n][k] % mod);
    }
}
