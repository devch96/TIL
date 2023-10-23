import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_9465_스티커 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int n = Integer.parseInt(br.readLine());
            int[][] dp = new int[n + 1][3];
            StringTokenizer st = new StringTokenizer(br.readLine());
            StringTokenizer st2 = new StringTokenizer(br.readLine());
            for (int j = 1; j <= n; j++) {
                dp[j][1] = Integer.parseInt(st.nextToken());
                dp[j][2] = Integer.parseInt(st2.nextToken());
            }
            for (int j = 1; j <= n; j++) {
                dp[j][0] = Math.max(dp[j-1][1], dp[j-1][2]);
                dp[j][1] += Math.max(dp[j-1][0],dp[j-1][2]);
                dp[j][2] += Math.max(dp[j-1][1], dp[j-1][0]);
            }
            System.out.println(Math.max(Math.max(dp[n][0], dp[n][1]), dp[n][2]));
        }
    }
}
