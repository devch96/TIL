import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1167_트리의_지름 {
    static class Node{
        int e;
        int value;

        public Node(int e, int value) {
            this.e = e;
            this.value = value;
        }
    }

    static List<Node>[] arrList;

    static boolean[] visited;

    static int[] distance;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int v = Integer.parseInt(br.readLine());
        arrList = new List[v + 1];
        for (int i = 1; i <= v; i++) {
            arrList[i] = new ArrayList<>();
        }
        for (int i = 0; i < v; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            while(true){
                int e = Integer.parseInt(st.nextToken());
                if (e == -1) {
                    break;
                }
                int value = Integer.parseInt(st.nextToken());
                arrList[s].add(new Node(e, value));
            }
        }
        distance = new int[v + 1];
        visited = new boolean[v + 1];
        bfs(1);
        int max = 1;
        for (int i = 2; i <= v; i++) {
            if (distance[max] < distance[i]) {
                max = i;
            }
        }
        distance = new int[v + 1];
        visited = new boolean[v + 1];
        bfs(max);
        Arrays.sort(distance);
        System.out.println(distance[v]);
    }

    private static void bfs(int index) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(index);
        visited[index] = true;
        while (!queue.isEmpty()) {
            int nowIndex = queue.poll();
            for (Node n : arrList[nowIndex]) {
                int e = n.e;
                int value = n.value;
                if (!visited[e]) {
                    visited[e] = true;
                    queue.add(e);
                    distance[e] = distance[nowIndex]+value;
                }
            }
        }
    }
}

