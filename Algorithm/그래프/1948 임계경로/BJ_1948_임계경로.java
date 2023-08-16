import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1948_임계경로 {
    static class Node{
        int targetNode;
        int value;

        Node(int targetNode, int value) {
            this.targetNode = targetNode;
            this.value = value;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());

        List<List<Node>> arrList = new ArrayList<>();
        List<List<Node>> reverseArrList = new ArrayList<>();

        for (int i = 0; i <= n; i++) {
            arrList.add(new ArrayList<>());
            reverseArrList.add(new ArrayList<>());
        }

        int[] indegree = new int[n + 1];
        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            arrList.get(start).add(new Node(end, value));
            reverseArrList.get(end).add(new Node(start, value));
            indegree[end]++;
        }
        StringTokenizer st = new StringTokenizer(br.readLine());
        int startCity = Integer.parseInt(st.nextToken());
        int endCity = Integer.parseInt(st.nextToken());

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(startCity);
        int[] result = new int[n + 1];
        while (!queue.isEmpty()) {
            int now = queue.poll();
            for (Node next : arrList.get(now)) {
                indegree[next.targetNode]--;
                result[next.targetNode] = Math.max(result[next.targetNode], result[now] + next.value);
                if (indegree[next.targetNode] == 0) {
                    queue.add(next.targetNode);
                }
            }
        }

        int answer = 0;
        boolean[] visited = new boolean[n + 1];
        queue = new ArrayDeque<>();
        queue.add(endCity);
        visited[endCity] = true;
        while (!queue.isEmpty()) {
            int now = queue.poll();
            for (Node next : reverseArrList.get(now)) {
                if (result[next.targetNode] + next.value == result[now]) {
                    answer++;
                    if (!visited[next.targetNode]) {
                        visited[next.targetNode] = true;
                        queue.add(next.targetNode);
                    }
                }
            }
        }
        System.out.println(result[endCity]);
        System.out.println(answer);
    }
}


