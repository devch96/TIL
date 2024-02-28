import java.util.ArrayDeque;
import java.util.Queue;

public class PG_Lv2_석유_시추 {
    static int[] oilGroup;

    class Node {
        int x;
        int y;

        public Node(int y, int x) {
            this.x = x;
            this.y = y;
        }
    }

    static int[] dx = { 1, 0, -1, 0 };
    static int[] dy = { 0, -1, 0, 1 };

    public int[] solution(int[][] land) {
        int answer = 0;
        oilGroup = new int[land[0].length];
        int a = checkOil(land);
        return oilGroup;
    }

    private int checkOil(int[][] land) {
        int result = 0;
        int group = 1;
        for (int j = 0; j < land[0].length; j++) {
            for (int i = 0; i < land.length; i++) {
                if (land[i][j] == 1) {
                    bfs(i, j, land, ++group);
                }
            }
            result = Math.max(result, oilGroup[j]);
        }
        return result;
    }

    private void bfs(int y, int x, int[][] land, int group) {
        int max = 0;
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(y, x));
        int colMin = land[0].length - 1;
        int colMax = 0;
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            if(land[now.y][now.x] != 1) {
                continue;
            }
            land[now.y][now.x] = group;
            max++;
            colMax = Math.max(colMax, now.x);
            colMin = Math.min(colMin, now.x);
            for (int i = 0; i < 4; i++) {
                int nx = now.x + dx[i];
                int ny = now.y + dy[i];
                if (nx < 0 || nx >= land[0].length || ny < 0 || ny >= land.length) {
                    continue;
                }
                if (land[ny][nx] != 1) {
                    continue;
                }
                queue.offer(new Node(ny, nx));
            }
        }
        for (int i = colMin; i <= colMax; i++) {
            oilGroup[i] += max;
        }
    }
}

