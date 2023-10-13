import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_17103_골드바흐_파티션 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean[] prime = new boolean[1_000_001];
        prime[0] = true;
        prime[1] = true;
        for (int i = 2; i < prime.length; i++) {
            for (int j = i + i; j < prime.length; j += i) {
                prime[j] = true;
            }
        }
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int n = Integer.parseInt(br.readLine());
            int answer = 0;
            for (int j = 1; j <= n / 2; j++) {
                if (!prime[j] && !prime[n - j]) {
                    answer++;
                }
            }
            System.out.println(answer);
        }
    }
}
