import java.io.*;
import java.util.StringTokenizer;

public class BJ_15651_N과_M_3 {
    static int n;
    static int m;
    static int[] arr;
    static StringBuilder sb;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arr = new int[m+1];
        sb = new StringBuilder();
        backtrack(0);
        bw.write(sb.toString());
        bw.flush();
    }

    private static void backtrack(int depth){
        if(depth == m){
            for (int i = 0; i < m; i++) {
                sb.append(arr[i] + " ");
            }
            sb.append("\n");
            return;
        }

        for (int i = 1; i <= n; i++) {
            arr[depth] = i;
            backtrack(depth+1);
        }
    }
}
