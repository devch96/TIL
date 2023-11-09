import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

public class BJ_14226_이모티콘 {
    static class Screen{
        int clipboard;
        int total;
        int time;

        public Screen(int clipboard, int total, int time) {
            this.clipboard = clipboard;
            this.total = total;
            this.time = time;
        }
    }
    static boolean[][] visited;
    static int s;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        visited = new boolean[1001][1001];
        s = Integer.parseInt(br.readLine());
        bfs();
    }

    private static void bfs(){
        Queue<Screen> queue = new ArrayDeque<>();
        queue.offer(new Screen(0, 1, 0));
        visited[0][1] = true;
        while (!queue.isEmpty()) {
            Screen current = queue.poll();
            if(current.total == s){
                System.out.println(current.time);
                return;
            }

            // 클립보드 복사
            queue.offer(new Screen(current.total, current.total, current.time + 1));

            if(current.clipboard != 0 && current.total + current.clipboard <= s &&
                    !visited[current.clipboard][current.total + current.clipboard]){
                visited[current.clipboard][current.total + current.clipboard] = true;
                queue.offer(new Screen(current.clipboard, current.total + current.clipboard, current.time + 1));
            }

            if (current.total >= 1 && !visited[current.clipboard][current.total - 1]) {
                visited[current.clipboard][current.total - 1] = true;
                queue.offer(new Screen(current.clipboard, current.total - 1, current.time + 1));
            }
        }
    }
}
