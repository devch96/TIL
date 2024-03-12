public class PG_Lv3_등굣길 {
    static int[][] map;
    public int solution(int m, int n, int[][] puddles) {
        map = new int[n][m];
        map[0][0] = 1;
        findPath(m,n,puddles);
        int answer = map[n-1][m-1];
        return answer % 1_000_000_007;
    }

    private void findPath(int m, int n, int[][] puddles) {
        for(int i = 0; i < n; i++) { // 3
            for(int j = 0; j < m; j++) { // 4
                if(isNotPuddles(i,j, puddles)) {
                    if(i != 0) {
                        map[i][j] += map[i-1][j] % 1_000_000_007;
                    }
                    if(j != 0) {
                        map[i][j] += map[i][j-1] % 1_000_000_007;
                    }
                }
            }
        }
    }

    private boolean isNotPuddles(int x, int y, int[][] puddles) {
        for(int[] xy : puddles) {
            if(xy[0] == y+1 && xy[1] == x+1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        PG_Lv3_등굣길 p = new PG_Lv3_등굣길();
        p.solution(4, 3, new int[][]{{2, 2}});
    }
}
