import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1915_가장_큰_정사각형 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long[][] dp = new long[1001][1001];
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        long max = 0;
        for (int i = 0; i < n; i++) {
            String line = br.readLine().strip();
            for (int j = 0; j < m; j++) {
                dp[i][j] = Long.parseLong(String.valueOf(line.charAt(j)));
                if (dp[i][j] == 1 && j > 0 && i > 0) {
                    dp[i][j] = Math.min(dp[i-1][j-1], Math.min(dp[i-1][j],dp[i][j-1])) + dp[i][j];
                }
                if(max < dp[i][j]){
                    max = dp[i][j];
                }
            }
        }
        System.out.println(max*max);
    }
}
