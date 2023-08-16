import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1325_효율적인_해킹 {
    static List<Integer>[] arrList;
    static boolean[] visited;

    static int[] answer;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        arrList = new ArrayList[n + 1];
        answer = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }


        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
        }

        for (int i = 1; i <= n; i++) {
            visited = new boolean[n + 1];
            dfs(i);
        }

        int max = Arrays.stream(answer).max().getAsInt();

        for (int i = 1; i <= n; i++) {
            if (answer[i] == max) {
                System.out.print(i + " ");
            }
        }
    }

    private static void bfs(int start) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start] = true;
        while (!queue.isEmpty()) {
            int now = queue.poll();
            for (int next : arrList[now]) {
                if (!visited[next]) {
                    queue.add(next);
                    visited[next] = true;
                    answer[next]++;
                }
            }
        }
    }

    private static void dfs(int node){
        visited[node] = true;
        for (int next : arrList[node]) {
            if (!visited[next]) {
                visited[next] = true;
                answer[next]++;
                dfs(next);
            }
        }
    }
}
