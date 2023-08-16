import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_2251_물통 {
    static int[] sender = {0, 0, 1, 1, 2, 2};
    static int[] receiver = {1, 2, 0, 2, 0, 1};
    static boolean[][] visited;
    static boolean[] answer;
    static int[] now;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        now = new int[3];
        for (int i = 0; i < 3; i++) {
            now[i] = Integer.parseInt(st.nextToken());
        }
        visited = new boolean[201][201];
        answer = new boolean[201];
        bfs();

        for (int i = 0; i < answer.length; i++) {
            if(answer[i]){
                System.out.print(i + " ");
            }
        }
    }

    private static void bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(new Node(0, 0));
        visited[0][0] = true;
        answer[now[2]] = true;
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            int a = n.a;
            int b = n.b;
            int c = now[2] - a - b;
            for (int k = 0; k < 6; k++) {
                int[] next = {a, b, c};
                next[receiver[k]] += next[sender[k]];
                next[sender[k]] = 0;
                if (next[receiver[k]] > now[receiver[k]]) {
                    next[sender[k]] = next[receiver[k]] - now[receiver[k]];
                    next[receiver[k]] = now[receiver[k]];
                }
                if (!visited[next[0]][next[1]]) {
                    visited[next[0]][next[1]] = true;
                    queue.add(new Node(next[0],next[1]));
                    if (next[0] == 0) {
                        answer[next[2]] = true;
                    }
                }
            }
        }
    }


    static class Node{
        int a;
        int b;
        public Node(int a, int b){
            this.a = a;
            this.b = b;
        }
    }
}


