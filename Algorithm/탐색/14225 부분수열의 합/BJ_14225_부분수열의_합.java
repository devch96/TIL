import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_14225_부분수열의_합 {
    static int n;
    static int[] sequence;
    static boolean[] candidates;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        sequence = new int[n];
        candidates = new boolean[2_000_001];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            sequence[i] = Integer.parseInt(st.nextToken());
        }
        dfs(0,0,0);
        for (int i = 1; i < 2000001; i++) {
            if (!candidates[i]) {
                System.out.println(i);
                break;
            }
        }
    }

    private static void dfs(int start, int sum, int depth){
        candidates[sum] = true;
        if(depth == n){
            return;
        }
        for (int i = start; i < n; i++) {
            dfs(i+1,sum + sequence[i], depth+1);
        }
    }
}
