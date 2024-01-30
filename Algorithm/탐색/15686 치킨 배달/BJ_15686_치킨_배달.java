import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_15686_치킨_배달 {
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static boolean[] visited;
    static List<Point> chickenStores = new ArrayList<>();
    static List<Point> houses = new ArrayList<>();
    static List<Point> choiceStores = new ArrayList<>();
    static int totalChickenDistance = Integer.MAX_VALUE;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        int[][] map = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (map[i][j] == 1) {
                    houses.add(new Point(i, j));
                }
                if (map[i][j] == 2) {
                    chickenStores.add(new Point(i, j));
                }
            }
        }
        visited = new boolean[chickenStores.size()];
        backtrack(0, 0);
        System.out.println(totalChickenDistance);
    }

    private static void backtrack(int depth, int start) {
        if (depth == m) {
            int sum = 0;
            for (Point house : houses) {
                int min = Integer.MAX_VALUE;
                for (Point choice : choiceStores) {
                    int distance = Math.abs(house.x - choice.x) + Math.abs(house.y - choice.y);
                    min = Math.min(min, distance);
                }
                sum += min;
            }
            totalChickenDistance = Math.min(totalChickenDistance, sum);
            return;
        }

        for (int i = start; i < chickenStores.size(); i++) {
            if (!visited[i]) {
                visited[i] = true;
                choiceStores.add(chickenStores.get(i));
                backtrack(depth + 1, i + 1);
                choiceStores.remove(chickenStores.get(i));
                visited[i] = false;
            }
        }
    }
}
