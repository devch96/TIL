import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;

public class PG_Lv2_미로_탈출 {
    static class Node {
        int x;
        int y;
        int count;

        public Node(int x, int y, int count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }
    }

    static int[] X = {0, 0, 1, -1};
    static int[] Y = {1, -1, 0, 0};

    public static int solution(String[] maps) {
        int[] start = getStartPosition('S', maps);
        int[] lever = getStartPosition('L', maps);

        int leverCount = bfs('L', start, maps);

        if (leverCount == 0) {
            return -1;
        }

        int exitCount = bfs('E', lever, maps);

        if (exitCount == 0) {
            return -1;
        }

        return leverCount + exitCount;
    }

    private static int bfs(char find, int[] start, String[] maps) {
        Queue<Node> q = new ArrayDeque<>();
        q.offer(new Node(start[0], start[1], 0));

        boolean[][] visited = new boolean[maps.length][maps[0].length()];
        visited[start[0]][start[1]] = true;

        int answer = 0;

        while(!q.isEmpty()) {
            Node cur = q.poll();

            if (maps[cur.x].charAt(cur.y) == find) {
                if (answer == 0) {
                    answer = cur.count;
                } else {
                    answer = Math.min(answer, cur.count);
                }
            }

            for (int i = 0; i < 4; i++) {
                int x = cur.x + X[i];
                int y = cur.y + Y[i];

                if (x < 0 || y < 0
                        || x >= maps.length
                        || y >= maps[0].length()
                        || maps[x].charAt(y) == 'X'
                        || visited[x][y]
                ) {
                    continue;
                }

                visited[x][y] = true;
                q.offer(new Node(x, y, cur.count + 1));
            }
        }

        return answer;
    }

    private static int[] getStartPosition(char find, String[] maps) {
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length(); j++) {
                if (maps[i].charAt(j) == find) {
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }



    public static void main(String[] args) {
        System.out.println(solution(new String[] {"SOOOL","XXXXO","OOOOO","OXXXX","OOOOE"}));
    }
}
