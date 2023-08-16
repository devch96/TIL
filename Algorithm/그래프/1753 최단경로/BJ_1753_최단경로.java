import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1753_최단경로 {
    static class Node implements Comparable<Node> {
        int next;
        int value;
        Node(int next, int value){
            this.next = next;
            this.value = value;
        }

        @Override
        public int compareTo(Node o) {
            if(this.value > o.value) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int v = Integer.parseInt(st.nextToken());
        int e = Integer.parseInt(st.nextToken());
        int start = Integer.parseInt(br.readLine());

        boolean[] visited = new boolean[v + 1];
        int[] distance = new int[v + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);


        List<Node>[] arrList = new List[v + 1];
        for (int i = 1; i <= v; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < e; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            arrList[x].add(new Node(y, z));
        }

        Queue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(start, 0));
        distance[start] = 0;

        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            int nextVertex = cur.next;
            if (!visited[nextVertex]) {
                visited[nextVertex] = true;
                for (int i = 0; i < arrList[nextVertex].size(); i++) {
                    Node temp = arrList[nextVertex].get(i);
                    int next = temp.next;
                    int value = temp.value;
                    if (distance[next] > distance[nextVertex] + value) {
                        distance[next] = distance[nextVertex] + value;
                        queue.add(new Node(next, distance[next]));
                    }
                }
            }
        }

        for (int i = 1; i <= v; i++) {
            if(visited[i]){
                System.out.println(distance[i]);
            }else{
                System.out.println("INF");
            }
        }
    }
}
