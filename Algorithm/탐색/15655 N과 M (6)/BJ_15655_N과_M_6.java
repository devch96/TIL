import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_15655_N과_M_6 {
    static int[] arr;
    static int[] sequence;
    static int n;
    static int m;
    static StringBuilder sb;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arr = new int[m];
        sequence = new int[n];
        sb = new StringBuilder();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            sequence[i] = Integer.parseInt(st.nextToken());
        }
        Arrays.sort(sequence);
        backtrack(0,0);
        bw.write(sb.toString());
        bw.flush();
    }

    private static void backtrack(int startIndex, int depth){
        if(depth == m){
            for (int i = 0; i < m; i++) {
                sb.append(arr[i] + " ");
            }
            sb.append("\n");
            return;
        }
        for (int i = startIndex; i < n; i++) {
            arr[depth] = sequence[i];
            backtrack(i+1, depth+1);
        }
    }
}
