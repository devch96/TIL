import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1504_특정한_최단_경로 {
    static class Node implements Comparable<Node>{
        int destination;
        int value;

        public Node(int destination, int value) {
            this.destination = destination;
            this.value = value;
        }

        @Override
        public int compareTo(Node o) {
            return this.value - o.value;
        }
    }

    static List<Node>[] arrList;
    static boolean[] visited;
    static int[] distance;
    static int n;
    static int e;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());

        arrList = new List[n + 1];
        distance = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < e; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            arrList[start].add(new Node(destination, value));
            arrList[destination].add(new Node(start, value));
        }
        st = new StringTokenizer(br.readLine());
        int target1 = Integer.parseInt(st.nextToken());
        int target2 = Integer.parseInt(st.nextToken());

        long route1 = 0;
        route1 += dijkstra(1, target1);
        route1 += dijkstra(target1, target2);
        route1 += dijkstra(target2, n);

        long route2 = 0;
        route2 += dijkstra(1, target2);
        route2 += dijkstra(target2, target1);
        route2 += dijkstra(target1,n);

        long answer = (route1 >= Integer.MAX_VALUE && route2 >= Integer.MAX_VALUE) ? -1 : Math.min(route1, route2);
        System.out.println(answer);
    }

    private static int dijkstra(int start, int destination){
        Arrays.fill(distance, Integer.MAX_VALUE);
        visited = new boolean[n + 1];
        Queue<Node> queue = new PriorityQueue<>();
        distance[start] = 0;
        queue.add(new Node(start, 0));
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (!visited[cur.destination]) {
                visited[cur.destination] = true;
                for (Node n : arrList[cur.destination]) {
                    int next = n.destination;
                    int value = n.value;
                    if (distance[next] > distance[cur.destination] + value && !visited[next]) {
                        distance[next] = distance[cur.destination] + value;
                        queue.add(new Node(next, distance[next]));
                    }
                }
            }
        }
        return distance[destination];
    }
}
