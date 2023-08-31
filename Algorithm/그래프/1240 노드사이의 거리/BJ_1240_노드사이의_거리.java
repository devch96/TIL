import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1240_노드사이의_거리 {
    static int N;
    static ArrayList<Node> list[];
    static boolean visit[];

    static class Node {
        int next;
        int dist;

        Node (int next, int dist) {
            this.next = next;
            this.dist = dist;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        list = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            list[i] = new ArrayList<>();
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            list[start].add(new Node(end, cost));
            list[end].add(new Node(start, cost));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            sb.append(find(start, end) + "\n");
        }
        System.out.println(sb);
    }
    static int find(int start, int end) {
        visit = new boolean[N + 1];
        Queue<Node> q = new LinkedList<>();
        q.offer(new Node(start, 0));
        visit[start] = true;

        int dist = 0;
        while(!q.isEmpty()) {
            Node node = q.poll();
            if (node.next == end) {
                dist = node.dist;
                break;
            }

            for (Node temp : list[node.next]) {
                if (!visit[temp.next]) {
                    q.offer(new Node(temp.next, node.dist + temp.dist));
                    visit[temp.next] = true;
                }
            }
        }

        return dist;
    }
}


