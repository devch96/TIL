import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PG_Lv2_무인도_여행 {
    static List<Integer> answer;
    static char[][] graph;
    static boolean[][] visited;
    static int total;

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};

    public static int[] solution(String[] maps){
        graph = new char[maps.length][maps[0].length()];
        visited = new boolean[maps.length][maps[0].length()];
        answer = new ArrayList<>();

        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[0].length(); j++) {
                graph[i][j] = maps[i].charAt(j);
            }
        }

        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[0].length(); j++) {
                if(!visited[i][j] && graph[i][j] != 'X'){
                    total = 0;
                    dfs(i,j);
                    answer.add(total);
                }
            }
        }
        Collections.sort(answer);
        if (answer.isEmpty()) {
            return new int[]{-1};
        }else{
            return answer.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    private static void dfs(int x, int y){
        visited[x][y] = true;
        total += graph[x][y] - '0';
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx < graph.length && nx >= 0 && ny < graph[0].length && ny >= 0 && !visited[nx][ny] && graph[nx][ny] != 'X') {
                dfs(nx,ny);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new String[] {"X591X","X1X5X","X231X", "1XXX1"})));
        System.out.println(Arrays.toString(solution(new String[] {"XXX","XXX","XXX"})));
    }
}
