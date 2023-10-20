import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2225_합분해 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int[][] dp = new int[201][201];
        dp[1][1] = 1;
        for (int i = 1; i < 201; i++) {
            for (int j = 1; j < 201; j++) {
                for (int l = 1; l < i; l++) {
                    dp[i][j] += dp[l][j-1];
                }
                dp[i][j] += dp[i][j-1]+1;
            }
        }
        System.out.println(dp[n][k]);
    }
}
