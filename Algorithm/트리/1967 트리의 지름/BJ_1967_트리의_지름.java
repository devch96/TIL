import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1967_트리의_지름 {

    static class Node{
        int destination;
        int value;

        public Node(int destination, int value){
            this.destination = destination;
            this.value = value;
        }
    }
    static List<Node>[] arrList;
    static boolean[] visited;
    static int maxValue = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        arrList = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < n-1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            arrList[start].add(new Node(destination, value));
            arrList[destination].add(new Node(start, value));
        }

        for (int i = 1; i <= n; i++) {
            visited = new boolean[n + 1];
            visited[i] = true;
            dfs(i,0);
        }
        System.out.println(maxValue);
    }

    private static void dfs(int now, int value){
        for (Node node : arrList[now]) {
            if (!visited[node.destination]) {
                visited[node.destination] = true;
                dfs(node.destination, value + node.value);
            }
        }
        maxValue = Math.max(maxValue, value);
    }
}
