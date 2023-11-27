import java.io.*;
import java.util.*;

public class BJ_16946_벽_부수고_이동하기_4 {
    static class Node{
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};

    static Map<Integer, Integer> grouping = new HashMap<>();
    static int[][] map;
    static int[][] group;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        group = new int[n][m];

        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < m; j++) {
                map[i][j] = str.charAt(j) - '0';
            }
        }

        int index = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] == 0 && group[i][j] == 0) {
                    grouping.put(index, bfs(i,j,index));
                    index++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(map[i][j] == 1){
                    sb.append(mapCount(i,j));
                }else {
                    sb.append(0);
                }
            }
            sb.append("\n");
        }
        bw.write(sb.toString());
        bw.flush();
    }

    private static int bfs(int startX, int startY, int groupNum){
        int result = 0;
        group[startX][startY] = groupNum;
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(startX, startY));
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            result++;
            int x = now.x;
            int y = now.y;
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }
                if (map[nx][ny] == 0 && group[nx][ny] == 0) {
                    group[nx][ny] = groupNum;
                    queue.offer(new Node(nx, ny));
                }
            }
        }
        return result;
    }

    private static int mapCount(int x, int y){
        int result = 1;
        Set<Integer> adjacentGroup = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                continue;
            }
            if (group[nx][ny] != 0) {
                adjacentGroup.add(group[nx][ny]);
            }
        }
        for(int groupNum : adjacentGroup){
            result += grouping.get(groupNum);
        }
        return result % 10;
    }
}
