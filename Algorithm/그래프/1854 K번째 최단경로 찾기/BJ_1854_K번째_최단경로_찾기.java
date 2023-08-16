import java.io.*;
import java.util.*;

public class BJ_1854_K번째_최단경로_찾기 {
    static class Node implements Comparable<Node>{
        int node;
        int cost;

        Node(int node, int cost){
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return this.cost < o.cost ? -1 : 1;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int[][] graph = new int[1001][1001];

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        Queue<Integer>[] distQueues = new PriorityQueue[n + 1];

        for (int i = 1; i <= n; i++) {
            distQueues[i] = new PriorityQueue<>(k, Collections.reverseOrder());
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            graph[a][b] = c;
        }

        Queue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(1, 0));
        distQueues[1].add(0);
        while (!queue.isEmpty()) {
            Node u = queue.poll();
            for (int adjNode = 1; adjNode <= n; adjNode++) {
                if (graph[u.node][adjNode] != 0) {
                    if (distQueues[adjNode].size() < k) {
                        distQueues[adjNode].add(u.cost + graph[u.node][adjNode]);
                        queue.add(new Node(adjNode, u.cost + graph[u.node][adjNode]));
                    } else if (distQueues[adjNode].peek() > u.cost + graph[u.node][adjNode]) {
                        distQueues[adjNode].poll();
                        distQueues[adjNode].add(u.cost + graph[u.node][adjNode]);
                        queue.add(new Node(adjNode, u.cost + graph[u.node][adjNode]));
                    }
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            if(distQueues[i].size() == k){
                bw.write(distQueues[i].peek() + "\n");
            }else{
                bw.write(-1 + "\n");
            }
        }
        bw.flush();
        bw.close();
        br.close();
    }
}
