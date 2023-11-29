import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

public class BJ_16954_움직이는_미로_탈출 {
    static class Node{
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static char[][] board;
    static int[] dx = {0, 1, 1, 0, -1, -1, -1, 0, 1};
    static int[] dy = {0, 0, -1, -1, -1, 0, 1, 1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        board = new char[8][8];
        for (int i = 0; i < 8; i++) {
            board[i] = br.readLine().toCharArray();
        }
        System.out.println(bfs());
    }

    private static int bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(7, 0));
        while (!queue.isEmpty()) {
            int s = queue.size();
            for (int i = 0; i < s; i++) {
                Node now = queue.poll();
                int x = now.x;
                int y = now.y;
                if (board[x][y] == '#') {
                    continue;
                }
                if(x == 0 && y == 7){
                    return 1;
                }
                for (int j = 0; j < 9; j++) {
                    int nx = x + dx[j];
                    int ny = y + dy[j];
                    if (nx < 0 || nx >= 8 || ny < 0 || ny >= 8) {
                        continue;
                    }
                    if (board[nx][ny] != '#') {
                        queue.add(new Node(nx, ny));
                    }
                }
            }
            moveWall();
        }
        return 0;
    }

    public static void moveWall() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == '#') {
                    board[i][j] = '.';

                    if (i != 7) {
                        board[i + 1][j] = '#';
                    }
                }
            }
        }
    }
}
