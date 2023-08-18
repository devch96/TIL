import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BJ_9252_LCS_2 {
    static long[][] dp;
    static char[] a;
    static char[] b;
    static List<Character> path;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        a = br.readLine().toCharArray();
        b = br.readLine().toCharArray();
        dp = new long[a.length + 1][b.length + 1];
        path = new ArrayList<>();

        for (int i = 1; i <= a.length; i++) {
            for (int j = 1; j <= b.length; j++) {
                if (a[i - 1] == b[j - 1]) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else{
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        System.out.println(dp[a.length][b.length]);
        getText(a.length, b.length);
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i));
        }
        System.out.println();
    }

    private static void getText(int aLength, int bLength) {
        if (aLength == 0 || bLength == 0) {
            return;
        }
        if (a[aLength - 1] == b[bLength - 1]) {
            path.add(a[aLength - 1]);
            getText(aLength-1, bLength-1);
        }else{
            if (dp[aLength - 1][bLength] > dp[aLength][bLength - 1]) {
                getText(aLength - 1, bLength);
            }else{
                getText(aLength, bLength-1);
            }
        }
    }

}
