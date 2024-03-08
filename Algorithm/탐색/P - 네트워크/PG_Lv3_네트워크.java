public class PG_Lv3_네트워크 {
    static boolean[] visited;
    public int solution(int n, int[][] computers) {
        visited = new boolean[n];
        int answer = 0;
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (computers[i][j] == 1 && !visited[i]) {
                    visited[i] = true;
                    answer++;
                    dfs(i, computers);
                }
            }
        }
        return answer;
    }

    private void dfs(int startComputer, int[][] computers) {
        for(int i = 0; i < computers[startComputer].length; i++) {
            if(computers[startComputer][i] == 1 && !visited[i]){
                visited[i] = true;
                dfs(i, computers);
            }
        }
    }

    public static void main(String[] args) {
        PG_Lv3_네트워크 p = new PG_Lv3_네트워크();
        System.out.println(p.solution(3, new int[][]{{1, 1, 0}, {1, 1, 1}, {0, 1, 1}}));
    }
}
