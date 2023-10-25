import java.io.*;
import java.util.StringTokenizer;

public class BJ_15650_N과_M_2 {
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
        arr = new int[n + 1];
        sb = new StringBuilder();
        backtrack(1,0);
        bw.write(sb.toString());
        bw.flush();
    }
    private static void backtrack(int start, int depth){
        if(depth == m){
            for (int i = 0; i < m; i++) {
                sb.append(arr[i] + " ");
            }
            sb.append("\n");
            return;
        }
        for (int i = start; i <= n; i++) {
            arr[depth] = i;
            backtrack(i+1,depth+1);
        }
    }
}
