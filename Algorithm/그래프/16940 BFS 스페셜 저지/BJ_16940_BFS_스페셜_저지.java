import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_16940_BFS_스페셜_저지 {
    static ArrayList<Integer>[] arrList;
    static boolean[] visited;
    static int[] expect;
    static int[] parent;
    static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        arrList = new ArrayList[n+1];
        visited = new boolean[n+1];
        parent = new int[n + 1];
        expect = new int[n];

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }
        for (int i = 0; i < n-1; i++) {
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
        System.out.println(bfs() ? "1" : "0");

    }

    private static boolean bfs() {
        visited[1] = true;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(1);
        if(expect[0] != 1){
            return false;
        }
        int checkIndex = 1;
        while (!queue.isEmpty()) {
            int now = queue.poll();
            int count = 0;
            for (int next : arrList[now]) {
                if (!visited[next]) {
                    visited[next] = true;
                    parent[next] = now;
                    count++;
                }
            }

            for (int i = 0; i < count; i++) {
                int expectNum = expect[checkIndex];
                if(parent[expectNum] != now){
                    return false;
                }
                queue.add(expectNum);
                checkIndex++;
            }
        }
        return true;
    }

}
