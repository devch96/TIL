import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_16194_카드_구매하기_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] prices = new int[n + 1];
        int[] answer = new int[n + 1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            int price = Integer.parseInt(st.nextToken());
            prices[i] = price;
            answer[i] = price;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                answer[i] = Math.min(answer[i], answer[i - j] + prices[j]);
            }
        }
        System.out.println(answer[n]);
    }
}
