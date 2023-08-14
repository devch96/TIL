import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1929_소수_구하기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int start = Integer.parseInt(st.nextToken());
        int end = Integer.parseInt(st.nextToken());
        int[] sieve = new int[end+1];
        for (int i = 2; i <= end; i++) {
            sieve[i] = i;
        }
        for (int i = 2; i <= Math.sqrt(end); i++) {
            if(sieve[i] == 0){
                continue;
            }
            for (int j = i + i; j <= end; j += i) {
                sieve[j] = 0;
            }
        }
        for (int i = start; i <= end; i++) {
            if(sieve[i] != 0){
                System.out.println(sieve[i]);
            }
        }
    }
}
