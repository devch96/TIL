import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_14501_퇴사 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] income = new int[n + 2];
        int[] t = new int[n + 1];
        int[] p = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            t[i] = Integer.parseInt(st.nextToken());
            p[i] = Integer.parseInt(st.nextToken());
        }
        for (int i = n; i > 0; i--) {
            if (i + t[i] > n + 1) {
                income[i] = income[i+1];
            }else{
                income[i] = Math.max(income[i + 1], p[i] + income[i + t[i]]);
            }
        }
        System.out.println(income[1]);
    }
}
