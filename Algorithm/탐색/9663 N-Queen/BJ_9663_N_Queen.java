import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_9663_N_Queen {
    static int[] arr;
    static int result;
    static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());
        arr = new int[n];
        dfs(0);
        System.out.println(result);
    }

    private static void dfs(int depth){
        if (depth == n){
            result++;
            return;
        }
        for (int i = 0; i < n; i++) {
            arr[depth] = i;
            if (isAvailable(depth)) {
                dfs(depth + 1);
            }
        }
    }

    private static boolean isAvailable(int col){
        for (int i = 0; i < col; i++) {
            if (arr[col] == arr[i]) {
                return false;
            } else if (Math.abs(col - i) == Math.abs(arr[col] - arr[i])) {
                return false;
            }
        }
        return true;
    }
}
