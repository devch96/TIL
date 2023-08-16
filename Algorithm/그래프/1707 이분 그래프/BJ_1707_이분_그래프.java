import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1707_이분_그래프 {
    static List<Integer>[] arrList;
    static boolean[] visited;

    static int[] check;

    static boolean flag;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int k = Integer.parseInt(br.readLine());
        for (int x = 0; x < k; x++) {
            st = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());

            arrList = new ArrayList[v + 1];
            visited = new boolean[v + 1];
            check = new int[v + 1];
            flag = true;

            for (int i = 1; i <= v; i++) {
                arrList[i] = new ArrayList<>();
            }

            for (int i = 0; i < e; i++) {
                st = new StringTokenizer(br.readLine());
                int start = Integer.parseInt(st.nextToken());
                int end = Integer.parseInt(st.nextToken());
                arrList[start].add(end);
                arrList[end].add(start);
            }

            for (int i = 1; i <= v; i++) {
                if(flag){
                    dfs(i);
                }
                else{
                    break;
                }
            }

            if(flag){
                System.out.println("YES");
            }else{
                System.out.println("NO");
            }
        }

    }

    private static void dfs(int node) {
        visited[node] = true;
        for (int next : arrList[node]) {
            if (!visited[next]) {
                check[next] = (check[node] + 1) % 2;
                dfs(next);
            } else if (check[node] == check[next]) {
                flag = false;
            }
        }
    }
}
