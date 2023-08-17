import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_11725_트리의_부모_찾기 {
    static List<Integer>[] arrList;
    static boolean[] visited;

    static int[] answer;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        arrList = new List[n + 1];
        visited = new boolean[n + 1];
        answer = new int[n + 1];

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
        dfs(1);
        for (int i = 2; i <= n; i++) {
            System.out.println(answer[i]);
        }
    }

    private static void dfs(int start) {
        if (visited[start]) {
            return;
        }
        visited[start] = true;
        for (int next : arrList[start]) {
            if (!visited[next]) {
                answer[next] = start;
                dfs(next);
            }
        }
    }
}
