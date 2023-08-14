import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1456_거의_소수 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long start = Long.parseLong(st.nextToken());
        long end = Long.parseLong(st.nextToken());
        long[] sieve = new long[10_000_001];
        for (int i = 2; i < sieve.length; i++) {
            sieve[i] = i;
        }
        for (int i = 2; i <= Math.sqrt(sieve.length); i++) {
            if(sieve[i] == 0){
                continue;
            }
            for (int j = i + i; j < sieve.length; j+=i) {
                sieve[j] = 0;
            }
        }
        int answer = 0;
        for (int i =2; i < 10_000_001; i++) {
            if(sieve[i]!=0){
                long temp = sieve[i];
                while((double) sieve[i] <= (double) end / (double) temp){
                    if ((double) sieve[i] >= (double) start / (double) temp) {
                        answer++;
                    }
                    temp = temp *  sieve[i];
                }
            }
        }
        System.out.println(answer);
    }
}
