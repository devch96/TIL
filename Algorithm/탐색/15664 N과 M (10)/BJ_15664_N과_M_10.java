import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_15664_N과_M_10 {
    static int n;
    static int m;
    static StringBuilder sb;
    static int[] arr;
    static int[] sequence;
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arr = new int[m];
        sequence = new int[n];
        visited = new boolean[n];
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
            for(int num :arr){
                sb.append(num).append(" ");
            }
            sb.append("\n");
            return;
        }
        int before = -1;
        for (int i = start; i < n; i++) {
            int now = sequence[i];
            if(before != now && !visited[i]){
                before = now;
                visited[i] = true;
                arr[depth] = sequence[i];
                backtrack(i+1,depth+1);
                visited[i] = false;
            }
        }
    }
}
