import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_15665_N과_M_11 {
    static int[] sequence;
    static int[] arr;
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

    private static void backtrack(int start, int depth){
        if(depth == m){
            for(int num : arr){
                sb.append(num).append(" ");
            }
            sb.append("\n");
            return;
        }
        int before = -1;
        for (int i = start; i < n; i++) {
            int now = sequence[i];
            if(before != now){
                before = now;
                arr[depth] = sequence[i];
                backtrack(start, depth+1);
            }
        }
    }
}
