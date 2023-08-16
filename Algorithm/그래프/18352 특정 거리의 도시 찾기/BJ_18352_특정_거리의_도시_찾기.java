import java.io.*;
import java.util.*;

public class BJ_18352_특정_거리의_도시_찾기 {
    static List<Integer>[] arrList;
    static int[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());

        List<Integer> answer = new ArrayList<>();
        arrList = new List[n + 1];
        visited = new int[n + 1];
        Arrays.fill(visited,-1);
        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
        }

        bfs(x);

        for (int i = 1; i <= n; i++) {
            if (visited[i] == k) {
                answer.add(i);
            }
        }

        if (answer.isEmpty()) {
            System.out.println(-1);
        }else{
            Collections.sort(answer);
            for (int ans : answer) {
                System.out.println(ans);
            }
        }
    }

    private static void bfs(int start) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start]++;
        while (!queue.isEmpty()) {
            int now = queue.poll();
            for (int next : arrList[now]) {
                if (visited[next] == -1) {
                    visited[next] = visited[now]+1;
                    queue.add(next);
                }
            }
        }
    }
}
