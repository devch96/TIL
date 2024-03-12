public class PG_Lv3_등굣길 {
    static int[][] map;
    public int solution(int m, int n, int[][] puddles) {
        map = new int[n][m];
        map[0][0] = 1;
        if(puddles[0].length > 0){
            for(int[] xy : puddles){
                map[xy[1]-1][xy[0]-1] = -1;
            }
        }
        for(int i = 1 ;i < n; i++){
            map[i][0] = (map[i][0] == -1 | map[i-1][0] == -1) ? -1 : 1;
        }
        for(int j = 1 ;j < m ;j++){
            map[0][j] = (map[0][j] == -1 | map[0][j-1] == -1) ? -1 : 1;
        }
        findPath(m,n);
        return map[n-1][m-1] == -1 ? 0 : map[n-1][m-1];
    }

    private void findPath(int m, int n) {
        for(int i = 1; i < n; i++) { // 3
            for(int j = 1; j < m; j++) { // 4
                if(map[i][j] != -1) {
                    int a = map[i-1][j] == -1 ? 0 : map[i-1][j];
                    int b = map[i][j-1] == -1 ? 0 : map[i][j-1];
                    map[i][j] = (a+b) == 0 ? -1 : (a+b) % 1000000007;
                }
            }
        }
    }

    public static void main(String[] args) {
        PG_Lv3_등굣길 p = new PG_Lv3_등굣길();
        p.solution(4, 3, new int[][]{{2, 2}});
    }
}
