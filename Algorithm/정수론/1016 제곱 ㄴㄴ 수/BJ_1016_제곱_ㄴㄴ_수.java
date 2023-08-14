import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1016_제곱_ㄴㄴ_수 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long min = Long.parseLong(st.nextToken());
        long max = Long.parseLong(st.nextToken());
        boolean[] check = new boolean[(int) (max - min + 1)];
        for (long i = 2; i * i <= max; i++) {
            long pow = i*i;
            long startIdx = min/pow;
            if(min%pow != 0){
                startIdx++;
            }
            for (long j = startIdx; pow * j <= max; j++) {
                check[(int) ((j*pow) - min)] = true;
            }
        }
        int count = 0;
        for (int i = 0; i <= max - min; i++) {
            if (!check[i]) {
                count++;
            }
        }
        System.out.println(count);
    }
}
