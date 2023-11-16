import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_16198_에너지_모으기 {
    static int[] energy;
    static boolean[] deleted;
    static int n;
    static int maxValue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        energy = new int[n];
        deleted = new boolean[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            energy[i] = Integer.parseInt(st.nextToken());
        }

        maxValue = Integer.MIN_VALUE;
        backtrack(0,0);
        System.out.println(maxValue);
    }

    private static void backtrack(int sum, int depth) {
        if (depth == n - 2) {
            maxValue = Math.max(maxValue, sum);
            return;
        }
        for (int i = 1; i < n-1; i++) {
            if(!deleted[i]){
                deleted[i] = true;
                backtrack(sum + (findLeft(i) * findRight(i)), depth+1);
                deleted[i] = false;
            }
        }
    }

    private static int findLeft(int num){
        for (int i = num; i > 0; i--) {
            if (!deleted[i]) {
                return energy[i];
            }
        }
        return energy[0];
    }

    private static int findRight(int num){
        for (int i = num; i < n; i++) {
            if (!deleted[i]) {
                return energy[i];
            }
        }
        return energy[n-1];
    }
}
