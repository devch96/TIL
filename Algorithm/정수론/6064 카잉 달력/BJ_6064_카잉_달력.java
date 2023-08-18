import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_6064_카잉_달력 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int m = Integer.parseInt(st.nextToken());
            int n = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken()) -1;
            int y = Integer.parseInt(st.nextToken()) -1 ;
            int answer = -1;
            for (int j = x; j <= m*n / gcd(m, n); j+=m) {
                if (j % n == y) {
                    answer = j+1;
                    break;
                }
            }

            System.out.println(answer);
        }
    }

    private static int gcd(int a, int b){
        if (b == 0) {
            return a;
        }else{
            return gcd(b, a % b);
        }
    }
}
