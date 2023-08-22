import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_15657_N과_M_8 {
    static List<String> answer;
    static int[] arr;
    static int n;
    static int m;
    static int[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());

        answer = new ArrayList<>();
        arr = new int[n];
        visited = new int[m];

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(arr);
        backtrack(0, 0);
    }

    private static void backtrack(int start, int depth){
        if(depth == m){
            for (int i = 0; i < m; i++) {
                System.out.print(visited[i]+" ");
            }
            System.out.println();
            return;
        }
        for (int i = start; i < n; i++) {
            visited[depth] = arr[i];
            backtrack(i, depth+1);
        }
    }
}
