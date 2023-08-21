import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_16928_뱀과_사다리_게임 {
    static boolean[] visited;
    static int[] graph;
    static Map<Integer, Integer> ladderOrSnake;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());


        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        graph = new int[101];
        visited = new boolean[101];

        ladderOrSnake = new HashMap<>();

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            ladderOrSnake.put(x, y);
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            ladderOrSnake.put(u, v);
        }
        bfs(1);
        System.out.println(graph[100]);
    }

    private static void bfs(int location){
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(location);
        visited[location] = true;
        while (!queue.isEmpty()) {
            int now = queue.poll();
            for (int i = 1; i <= 6; i++) {
                int next = now + i;
                int nextValue = ladderOrSnake.getOrDefault(next,next);
                if(nextValue <= 100){
                    if(!visited[nextValue]){
                        queue.add(nextValue);
                        visited[nextValue] = true;
                        graph[nextValue] = graph[now]+1;
                    }
                }
            }
        }
    }
}
