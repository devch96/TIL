import java.io.*;
import java.util.StringTokenizer;

public class BJ_9613_GCD_합 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = Integer.parseInt(st.nextToken());
            }
            long answer = 0;
            for (int j = 0; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    answer += GCD(arr[j], arr[k]);
                }
            }
            System.out.println(answer);
        }
    }

    private static int GCD(int x, int y){
        if(y == 0){
            return x;
        }else{
            return GCD(y, x % y);
        }
    }
}
