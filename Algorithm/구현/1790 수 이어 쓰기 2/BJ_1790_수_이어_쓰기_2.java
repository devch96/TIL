import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1790_수_이어_쓰기_2 {
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        long numLen = 1;
        long numCnt = 9;
        while (k > numCnt * numLen) {
            k -= (numCnt * numLen);
            numLen++;
            numCnt *= 10;
        }

        long target = ((int) Math.pow(10, numLen-1)) + (k - 1) / numLen;

        if (target > n) {
            System.out.println(-1);
        } else {
            int idx = (int) ((k - 1) % numLen);
            System.out.println(String.valueOf(target).charAt(idx));
        }
    }
}
