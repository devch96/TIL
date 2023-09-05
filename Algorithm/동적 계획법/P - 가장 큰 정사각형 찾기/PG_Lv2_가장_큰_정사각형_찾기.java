public class PG_Lv2_가장_큰_정사각형_찾기 {
    public static int solution(int[][] board){
        int[][] dp = new int[board.length + 1][board[0].length + 1];
        int max = 0;
        for (int i = 1; i <= board.length; i++) {
            for (int j = 1; j <= board[0].length; j++) {
                if(board[i-1][j-1] == 1){
                    dp[i][j] = Math.min(dp[i-1][j-1], Math.min(dp[i-1][j],dp[i][j-1])) + 1;
                }
                max = Math.max(max, dp[i][j]);
            }
        }
        return max * max;
    }
}
