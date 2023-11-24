import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_12886_돌_그룹 {
    static class StoneGroup{
        int a;
        int b;
        int c;

        public StoneGroup(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public boolean isSame(){
            return (a == b && b == c);
        }
    }
    static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        visited = new boolean[1501][1501];
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        visited[a][b] = true;
        System.out.println(bfs(new StoneGroup(a,b,c)));
    }

    private static int bfs(StoneGroup start){
        Queue<StoneGroup> queue = new ArrayDeque<>();
        queue.offer(start);
        while (!queue.isEmpty()) {
            StoneGroup now = queue.poll();
            int a = now.a;
            int b = now.b;
            int c = now.c;
            if(now.isSame()){
                return 1;
            }
            if(a != b){
                int na = a > b ? a - b : a * 2;
                int nb = a > b ? b * 2 : b - a;
                if (!visited[na][nb]) {
                    visited[na][nb] = true;
                    queue.offer(new StoneGroup(na, nb, c));
                }
            }
            if (a != c) {
                int na = a > c ? a - c : a * 2;
                int nc = a > c ? c * 2 : c - a;

                if (!visited[na][nc]) {
                    queue.add(new StoneGroup(na, b, nc));
                    visited[na][nc] = true;
                }
            }

            if (b != c) {
                int nb = b > c ? b - c : b * 2;
                int nc = b > c ? c * 2 : c - b;

                if (!visited[nb][nc]) {
                    queue.offer(new StoneGroup(a, nb, nc));
                    visited[nb][nc] = true;
                }
            }
        }
        return 0;
    }

}
