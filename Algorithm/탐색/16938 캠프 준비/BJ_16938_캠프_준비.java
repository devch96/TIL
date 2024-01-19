import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_16938_캠프_준비 {
    static int n;
    static int l;
    static int r;
    static int x;
    static int[] arr;
    static int answer;
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        l = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
        x = Integer.parseInt(st.nextToken());
        arr = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        bruteforce(0,0,Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        System.out.println(answer);
    }

    private static void bruteforce(int start, int depth, int max, int min, int score) {
        if (depth >= 2) {
            if (isValid(max, min, score)) {
                answer++;
            }
        }
        for (int i = start; i < n; i++) {
                bruteforce(i + 1, depth + 1,
                        Math.max(max, arr[i]), Math.min(min, arr[i]), score + arr[i]);
        }
    }

    private static boolean isValid(int max, int min, int score) {
        return l <= score && score <= r && (max - min) >= x;
    }
}
