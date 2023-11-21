import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_13460_구슬_탈출_2 {
    static class Node{
        int redX;
        int redY;
        int blueX;
        int blueY;
        int move;

        public Node(int redX, int redY, int blueX, int blueY, int move) {
            this.redX = redX;
            this.redY = redY;
            this.blueX = blueX;
            this.blueY = blueY;
            this.move = move;
        }
    }
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static char[][] board;
    static boolean[][][][] visited;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new char[n][m];
        visited = new boolean[n][m][n][m];
        Node start = new Node(0, 0, 0, 0, 1);

        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < m; j++) {
                char c = str.charAt(j);
                if(c == 'R'){
                    start.redX = i;
                    start.redY = j;
                } else if (c == 'B') {
                    start.blueX = i;
                    start.blueY = j;
                }
                board[i][j] = c;
            }
        }
        System.out.println(bfs(start));
    }

    private static int bfs(Node start){
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.redX][start.redY][start.blueX][start.blueY] = true;
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            int nowRedX = now.redX;
            int nowRedY = now.redY;
            int nowBlueX = now.blueX;
            int nowBlueY = now.blueY;
            int nowMove = now.move;
            if (nowMove > 10) {
                return -1;
            }

            for (int i = 0; i < 4; i++) {
                int nextRx = nowRedX;
                int nextRy = nowRedY;
                int nextBx = nowBlueX;
                int nextBy = nowBlueY;
                boolean isRedInHole = false;
                boolean isBlueInHole = false;
                while (board[nextRx + dx[i]][nextRy + dy[i]] != '#') {
                    nextRx += dx[i];
                    nextRy += dy[i];
                    if (board[nextRx][nextRy] == 'O') {
                        isRedInHole = true;
                        break;
                    }
                }

                while (board[nextBx + dx[i]][nextBy + dy[i]] != '#') {
                    nextBx += dx[i];
                    nextBy += dy[i];
                    if (board[nextBx][nextBy] == 'O') {
                        isBlueInHole = true;
                        break;
                    }
                }

                if(isBlueInHole){
                    continue;
                }
                if (isRedInHole) {
                    return nowMove;
                }

                if (nextRx == nextBx && nextRy == nextBy) {
                    if(i == 0) { // 위쪽으로 기울이기 
                        // 더 큰 x값을 가지는 구슬이 뒤로 감 
                        if (nowRedX > nowBlueX) {
                            nextRx -= dx[i];
                        } else {
                            nextBx -= dx[i];
                        }
                    } else if(i == 1) { // 오른쪽으로 기울이기 
                        // 더 작은 y값을 가지는 구슬이 뒤로 감 
                        if (nowRedY < nowBlueY) {
                            nextRy -= dy[i];
                        } else {
                            nextBy -= dy[i];
                        }
                    } else if(i == 2) { // 아래쪽으로 기울이기 
                        // 더 작은 x값을 가지는 구슬이 뒤로 감 
                        if (nowRedX < nowBlueX) {
                            nextRx -= dx[i];
                        } else {
                            nextBx -= dx[i];
                        }
                    } else { // 왼쪽으로 기울이기 
                        // 더 큰 y값을 가지는 구슬이 뒤로 감 
                        if (nowRedY > nowBlueY) {
                            nextRy -= dy[i];
                        } else {
                            nextBy -= dy[i];
                        }
                    }
                }
                if (!visited[nextRx][nextRy][nextBx][nextBy]) {
                    visited[nextRx][nextRy][nextBx][nextBy] = true;
                    queue.add(new Node(nextRx, nextRy, nextBx, nextBy, nowMove + 1));
                }
            }
        }
        return -1;
    }
}
