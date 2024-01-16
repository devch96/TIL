import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1561_놀이_공원 {
    static long n;
    static int m;
    static int[] times;
    static int max;
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Long.parseLong(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        times = new int[m + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= m; i++) {
            times[i] = Integer.parseInt(st.nextToken());
            max = Math.max(max, times[i]);
        }
        if (n <= m) {
            System.out.println(n);
        } else {
            long t = binarySearch(0, (n/m) * max);

            long cnt = m;
            for (int i = 1; i <= m; i++) {
                cnt += (t-1) / times[i];
            }
            for (int i = 1; i <= m; i++) {
                if (t % times[i] == 0) {
                    cnt++;
                }
                if(cnt == n){
                    System.out.println(i);
                    return;
                }
            }
        }
    }

    private static long binarySearch(long left, long right){
        while (left <= right) {
            long mid = (left + right) / 2;
            long sum = m;
            for (int i = 1; i <= m; i++) {
                sum += mid / times[i];
            }
            if (sum < n) {
                left = mid + 1;
            } else {
                right = mid -1;

            }
        }
        return left;
    }
}
