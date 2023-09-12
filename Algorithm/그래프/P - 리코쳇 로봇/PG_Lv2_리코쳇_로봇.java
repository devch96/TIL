import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class PG_Lv2_리코쳇_로봇 {
    static char[][] graph;
    static boolean[][] visited;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};
    static int[] start;
    static int[] destination;

    static class Node {
        public int x;
        public int y;
        public int count;

        public Node(int x, int y, int count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }
    }

    public static int solution(String[] board) {
        graph = new char[board.length][board[0].length()];
        visited = new boolean[board.length][board[0].length()];
        int answer = Integer.MAX_VALUE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length(); j++) {
                char temp = board[i].charAt(j);
                if (temp == 'R') {
                    start = new int[]{i, j};
                } else if (temp == 'G') {
                    destination = new int[]{i, j};
                }
                graph[i][j] = board[i].charAt(j);
            }
        }

        Deque<Node> queue = new ArrayDeque<>();
        int startX = start[0];
        int startY = start[1];
        queue.add(new Node(startX, startY, 0));
        visited[startX][startY] = true;
        while (!queue.isEmpty()) {
            Node nowPosition = queue.poll();
            if (answer <= nowPosition.count) {
                continue;
            }
            if (nowPosition.x == destination[0] && nowPosition.y == destination[1]) {
                answer = Math.min(answer, nowPosition.count);
                continue;
            }
            for (int i = 0; i < 4; i++) {
                int x = nowPosition.x + dx[i];
                int y = nowPosition.y + dy[i];

                if (x < 0 || y < 0 || x >= graph.length || y >= graph[0].length) {
                    continue;
                }

                if (board[x].charAt(y) == 'D') {
                    continue;
                }

                while (x >= 0 && y >= 0
                        && x < graph.length && y < graph[0].length
                        && graph[x][y] != 'D') {
                    x += dx[i];
                    y += dy[i];
                }

                x -= dx[i];
                y -= dy[i];
                if (visited[x][y]) {
                    continue;
                }
                visited[x][y] = true;
                queue.add(new Node(x, y, nowPosition.count + 1));
            }
        }
        return answer == Integer.MAX_VALUE ? -1 : answer;
    }

    public static void main(String[] args) {
        System.out.println(solution(new String[] {"...D..R", ".D.G...", "....D.D", "D....D.", "..D...."}));
    }
}