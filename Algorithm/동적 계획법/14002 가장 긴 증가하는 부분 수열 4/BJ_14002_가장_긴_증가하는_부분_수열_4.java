import java.io.*;
import java.util.*;

public class BJ_14002_가장_긴_증가하는_부분_수열_4 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int[] arr = new int[n+1];
        int[] dp = new int[n+1];
        int max = Integer.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            int num = Integer.parseInt(st.nextToken());
            arr[i] = num;
            for (int j = 0; j < i; j++) {
                if(arr[i] > arr[j]){
                    dp[i] = Math.max(dp[i], dp[j]+1);
                    max = Math.max(max, dp[i]);
                }
            }
        }
        sb.append(max + "\n");
        Stack<Integer> answer = new Stack<>();
        for (int i = n; i >= 1; i--) {
            if(max == dp[i]){
                answer.push(arr[i]);
                max--;
            }
        }
        while (!answer.isEmpty()) {
            sb.append(answer.pop()+" ");
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
