import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PG_Lv2_거리두기_확인하기 {
    static boolean flag;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};
    static char[][] room;
    static boolean[][] visited;

    public static int[] solution(String[][] places){
        List<Integer> answer = new ArrayList<>();
        for(String[] strArr : places){
            if (isValidRoom(strArr)) {
                answer.add(1);
            }else{
                answer.add(0);
            }
        }
        return answer.stream().mapToInt(Integer::intValue).toArray();
    }

    private static boolean isValidRoom(String[] place){
        room = new char[5][5];
        flag = true;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                room[i][j] = place[i].charAt(j);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(room[i][j] == 'P'){
                    visited = new boolean[5][5];
                    dfs(i,j,0);
                }
            }
        }
        return flag;
    }

    private static void dfs(int x, int y, int depth){
        visited[x][y] = true;
        if (depth > 0 && depth <= 2 && room[x][y] == 'P') {
            flag = false;
        }
        if(depth > 2){
            return;
        }
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(0<= nx && nx < 5 && 0<= ny && ny < 5 && !visited[nx][ny] && room[nx][ny] != 'X'){
                dfs(nx,ny,depth+1);
                visited[x][y] = false;
            }
            if (!flag) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new String[][] {{"POOPO", "OOOOO", "OOOXP", "OPOPX", "OOOOO"}})));
    }
}
