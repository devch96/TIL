import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_11724_연결_요소의_개수 {
    public static List<Integer>[] arrList;
    public static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        arrList = new ArrayList[n+1];
        visited = new boolean[n + 1];
        for (int i = 1; i < n + 1; i++) {
            arrList[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
            arrList[end].add(start);
        }
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if(!visited[i]){
                count++;
                dfs(i);
            }
        }
        System.out.println(count);
    }

    private static void dfs(int v){
        if (visited[v]) {
            return;
        }
        visited[v] = true;
        for(int i : arrList[v]){
            if (!visited[i]) {
                dfs(i);
            }
        }
    }
}
