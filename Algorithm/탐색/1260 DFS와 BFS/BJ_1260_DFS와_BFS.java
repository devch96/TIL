import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1260_DFS와_BFS {
    public  static List<Integer>[] arrList;
    public static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int v = Integer.parseInt(st.nextToken());

        arrList = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
            arrList[end].add(start);
        }
        visited = new boolean[n+1];
        for (int i = 1; i <= n; i++) {
            Collections.sort(arrList[i]);
        }
        dfs(v);
        System.out.println();
        visited = new boolean[n + 1];
        bfs(v);
        System.out.println();
    }

    private static void dfs(int node) {
        System.out.print(node + " ");
        visited[node] = true;
        for (int i : arrList[node]) {
            if (!visited[i]) {
                dfs(i);
            }
        }
    }

    private static void bfs(int node) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(node);
        visited[node] = true;
        while (!queue.isEmpty()) {
            int nowNode = queue.poll();
            System.out.print(nowNode + " ");
            for (int i : arrList[nowNode]) {
                if (!visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }
    }
}
