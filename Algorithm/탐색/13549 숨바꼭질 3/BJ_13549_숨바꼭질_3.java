import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_13549_숨바꼭질_3 {
    static class Node{
        int position;
        int count;

        public Node(int position, int count) {
            this.position = position;
            this.count = count;
        }
    }
    static boolean[] visited;
    static int start;
    static int destination;
    static int min;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        start = Integer.parseInt(st.nextToken());
        destination = Integer.parseInt(st.nextToken());
        visited = new boolean[100_001];
        min = Integer.MAX_VALUE;
        if(destination <= start){
            System.out.println(start - destination);
            return;
        }
        bfs();
        System.out.println(min);
    }

    private static void bfs(){
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(new Node(start, 0));
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            visited[now.position] = true;
            if (now.position == destination) {
                min = Math.min(now.count, min);
            }
            for (int i = 0; i < 3; i++) {
                int next;
                int nextCount;
                if(i==0){
                    next = now.position * 2;
                    nextCount = now.count;
                } else if (i == 1) {
                    next = now.position + 1;
                    nextCount = now.count+1;
                }else{
                    next = now.position - 1;
                    nextCount = now.count+1;
                }
                if (0 > next || next > 100000) {
                    continue;
                }
                if (!visited[next]) {
                    queue.add(new Node(next, nextCount));
                }
            }
        }
    }

}
