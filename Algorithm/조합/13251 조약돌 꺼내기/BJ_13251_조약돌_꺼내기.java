import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_13251_조약돌_꺼내기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int m = Integer.parseInt(br.readLine());
        int dp[] = new int[51];
        int t = 0;
        double probability[] = new double[51];
        double answer = 0.0;
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            dp[i] = Integer.parseInt(st.nextToken());
            t += dp[i];
        }
        int k = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            if (dp[i] >= k) {
                probability[i] = 1.0;
                for (int j = 0; j < k; j++) {
                    probability[i] *= (double) (dp[i]-j) / (t-j);
                }
            }
            answer += probability[i];
        }
        System.out.println(answer);
    }
}
