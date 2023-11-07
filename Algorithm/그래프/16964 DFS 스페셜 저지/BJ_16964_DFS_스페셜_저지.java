import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BJ_16964_DFS_스페셜_저지 {
    static int[] expect;
    static boolean[] visited;
    static int[] parent;
    static int n;
    static ArrayList<Integer>[] arrList;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        visited = new boolean[n + 1];
        expect = new int[n];
        parent = new int[n + 1];
        arrList = new ArrayList[n + 1];

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
            arrList[end].add(start);
        }
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            expect[i] = Integer.parseInt(st.nextToken());
        }

        System.out.println(dfs(1,0) ? "1" : "0");
    }

    private static boolean dfs(int now, int depth){
        visited[now] = true;
        int child = 0;
        for (int next : arrList[now]) {
            if (!visited[next]) {
                visited[next] = true;
                parent[next] = now;
                child++;
            }
        }

        for (int i = 0; i < child; i++) {
            int next = expect[depth+1];
            if(parent[next] != now){
                return false;
            }
            return dfs(next, depth+1);
        }
        return true;
    }
}
