import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_9251_LCS {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[] a;
        char[] b;
        long[][] dp;
        a = br.readLine().toCharArray();
        b = br.readLine().toCharArray();
        dp = new long[a.length + 1][b.length + 1];
        for (int i = 1; i <= a.length; i++) {
            for (int j = 1; j <= b.length; j++) {
                if (a[i - 1] == b[j - 1]) {
                    dp[i][j] = dp[i-1][j-1]+1;
                }else{
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        System.out.println(dp[a.length][b.length]);
    }
}
