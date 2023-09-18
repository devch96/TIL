import java.util.ArrayList;
import java.util.List;

public class PG_Lv2_혼자서_하는_틱택토 {
    static int[] dx = {1, 1, 1, 0, -1, -1, -1, 0};
    static int[] dy = {1, 0, -1, -1, -1, 0, 1, 1};
    static int countO;
    static int countX;
    static char[][] graph;
    static boolean[][] visited;
    static boolean O;
    static boolean X;

    public static int solution(String[] board){
        countO = 0;
        countX = 0;
        O = false;
        X = false;
        graph = new char[3][3];
        List<int[]> startPosition = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char c = board[i].charAt(j);
                if (c == 'O') {
                    countO++;
                    startPosition.add(new int[]{i, j});
                } else if (c == 'X') {
                    countX++;
                    startPosition.add(new int[]{i, j});
                }
                graph[i][j] = c;
            }
        }
        if (countO < countX) {
            return 0;
        }
        for (int[] start : startPosition) {
            visited = new boolean[3][3];
            dfs(start[0], start[1], 1, null);
        }
        if ((countO - countX <= 1) && (countO - countX >= 0)) {
                if(O && X) {
                    return 0;
                }else if(O){
                    if(countO == countX){
                        return 0;
                    }else{
                        return 1;
                    }
                } else if(X){
                    if(countO > countX){
                        return 0;
                    }else{
                        return 1;
                    }
                }
            return 1;
            }
        return 0;
    }

    private static void dfs(int x, int y, int depth, int[] dxy){
        visited[x][y] = true;
        if(depth == 3){
            if (graph[x][y] == 'O') {
                O = true;
            }else{
                X = true;
            }
            return;
        }
        if(depth > 1){
            int nx = x + dxy[0];
            int ny = y + dxy[1];
            if (0 <= nx && nx < 3 && 0 <= ny && ny < 3 && !visited[nx][ny]) {
                if (graph[x][y] == graph[nx][ny]) {
                    dfs(nx,ny,depth+1, dxy);
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (0 <= nx && nx < 3 && 0 <= ny && ny < 3 && !visited[nx][ny]) {
                    if (graph[x][y] == graph[nx][ny]) {
                        dfs(nx,ny,depth+1, new int[] {dx[i],dy[i]});
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(new String[] {"O.X", "O.X", "O.."}));
        System.out.println(solution(new String[] {"OOO", "OOO", "OOO"}));
        System.out.println(solution(new String[] {"OOO", "...", "..."}));
        System.out.println(solution(new String[] {"OOO", "XX.", "X.."}));
        System.out.println(solution(new String[] {".OX", "OXO", "XO."}));
        System.out.println(solution(new String[] {"OOX", "OXO", "XOX"}));
        System.out.println(solution(new String[] {"OOX", "OXO", "XOO"}));
        System.out.println(solution(new String[] {"XXX", "XOO", "OOO"}));
        System.out.println(solution(new String[] {".X.", "X..", ".O."}));
        System.out.println(solution(new String[] {"XXX", "OO.", "OO."}));
        System.out.println(solution(new String[] {"OOO", "XOX", "X.X"}));
        System.out.println(solution(new String[] {"XO.", "OXO", "XOX"}));
        System.out.println(solution(new String[] {"X.X", "X.O", "O.O"}));
    }
}
