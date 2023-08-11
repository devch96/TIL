import java.io.*;
import java.util.StringTokenizer;

public class BJ_10986_나머지_합 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        long[] sumArray = new long[n];
        long[] remainArray = new long[m];
        long answer = 0;
        st = new StringTokenizer(br.readLine());
        sumArray[0] = Integer.parseInt(st.nextToken());
        for (int i = 1; i < n; i++) {
            sumArray[i] = sumArray[i - 1] + Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < n; i++) {
            int remainder = (int) (sumArray[i] % m);
            if (remainder == 0) {
                answer++;
            }
            remainArray[remainder]++;
        }

        for (int i = 0; i < m; i++) {
            if(remainArray[i] > 1){
                answer = answer +  (remainArray[i] * (remainArray[i] - 1) / 2);
            }
        }
        bw.write(String.valueOf(answer));
        bw.flush();
        bw.newLine();
    }
}
