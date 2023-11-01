import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1182_부분수열의_합 {
    static int n;
    static int s;
    static int[] arr;
    static int answer;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        s = Integer.parseInt(st.nextToken());
        arr = new int[n];
        answer = 0;
        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        backtrack(0, 0);
        System.out.println(s == 0 ? answer-1 : answer);
    }

    private static void backtrack(int sum, int depth){
        if(depth == n){
            if(sum == s){
                answer++;
            }
            return;
        }
        backtrack(sum+arr[depth], depth+1);
        backtrack(sum, depth+1);
    }
}
