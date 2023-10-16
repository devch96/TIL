import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_11052_카드_구매하기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] prices = new int[n + 1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            prices[i + 1] = Integer.parseInt(st.nextToken());
        }
        int[] answer = new int[n + 1];
        answer[1] = prices[1];
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                answer[i] = Math.max(answer[i], answer[i - j] + prices[j]);
            }
        }
        System.out.println(answer[n]);
    }
}
