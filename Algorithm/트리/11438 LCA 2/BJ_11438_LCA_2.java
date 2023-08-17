import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_11438_LCA_2 {
    static List<Integer>[] tree;
    static int[] depth;

    static int kmax;
    static int[][] parent;
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        int n = Integer.parseInt(br.readLine());
        tree = new List[n+1];
        depth = new int[n + 1];
        int temp = 1;
        kmax = 0;
        while(temp <= n){
            temp <<= 1;
            kmax++;
        }
        parent = new int[kmax + 1][n + 1];
        visited = new boolean[n + 1];

        for (int i = 1; i <= n; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            tree[s].add(e);
            tree[e].add(s);
        }
        bfs(1);
        for (int k = 1; k <= kmax; k++) {
            for (int i = 1; i <= n; i++) {
                parent[k][i] = parent[k - 1][parent[k - 1][i]];
            }
        }

        int m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            System.out.println(excuteLCA(a,b));
        }
    }

    private static int excuteLCA(int a, int b) {
        if (depth[a] > depth[b]) {
            int temp = a;
            a = b;
            b = temp;
        }
        for (int k = kmax; k >= 0; k--) {
            if (Math.pow(2, k) <= depth[b] - depth[a]) {
                if (depth[a] <= depth[parent[k][b]]) {
                    b = parent[k][b];
                }
            }
        }
        for (int k = kmax; k >= 0; k--) {
            if (parent[k][a] != parent[k][b]) {
                a = parent[k][a];
                b = parent[k][b];
            }
        }
        int lca = a;
        if (a != b) {
            lca = parent[0][lca];
        }
        return lca;

    }

    private static void bfs(int node) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(node);
        visited[node] = true;
        int level = 1;
        int nowSize = 1;
        int count = 0;
        while (!queue.isEmpty()) {
            int nowNode = queue.poll();
            for (int next : tree[nowNode]) {
                if (!visited[next]) {
                    queue.offer(next);
                    visited[next] = true;
                    parent[0][next] = nowNode;
                    depth[next] = level;
                }
            }
            count++;
            if (count == nowSize) {
                count = 0;
                nowSize = queue.size();
                level++;
            }
        }
    }
}
