import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_11054_가장_긴_바이토닉_부분_수열 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] naturalDp = new int[n + 1];
        int[] reverseDp = new int[n + 1];
        int[] arr = new int[n + 1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // LIS
        for (int i = 1; i <= n ; i++) {
            for (int j = 0; j < i; j++) {
                if(arr[i] > arr[j]){
                    naturalDp[i] = Math.max(naturalDp[i], naturalDp[j] + 1);
                }
            }
        }

        // LDS
        for (int i = n; i > 0 ; i--) {
            for (int j = n; j > i ; j--) {
                if(arr[i] > arr[j]){
                    reverseDp[i] = Math.max(reverseDp[i], reverseDp[j] + 1);
                }
            }
        }

        int max = 0;
        for (int i = 1; i <= n; i++) {
            max = Math.max(max, naturalDp[i] + reverseDp[i]);
        }
        System.out.println(max);
    }
}
