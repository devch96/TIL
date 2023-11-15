import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_16197_두_동전 {
    static class Coins{
        int firstCoinX;
        int firstCoinY;
        int secondCoinX;
        int secondCoinY;
        int move;
        public Coins(){}

        public Coins(int firstCoinX, int firstCoinY, int secondCoinX, int secondCoinY, int move) {
            this.firstCoinX = firstCoinX;
            this.firstCoinY = firstCoinY;
            this.secondCoinX = secondCoinX;
            this.secondCoinY = secondCoinY;
            this.move = move;
        }
    }
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static char[][] board;
    static boolean[][][][] coinVisited;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new char[n][m];
        coinVisited = new boolean[n][m][n][m];
        Coins coins = new Coins();
        boolean flag = false;
        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < m; j++) {
                char cell = str.charAt(j);
                if(cell == 'o'){
                    if(!flag){
                        coins.firstCoinX = i;
                        coins.firstCoinY = j;
                        flag = true;
                    }else{
                        coins.secondCoinX = i;
                        coins.secondCoinY = j;
                    }
                }
                board[i][j] = str.charAt(j);
            }
        }
        System.out.println(bfs(coins));
    }

    private static int bfs(Coins start){
        Queue<Coins> coinQueue = new ArrayDeque<>();
        coinQueue.offer(start);
        while (!coinQueue.isEmpty()) {
            Coins now = coinQueue.poll();
            if(now.move >= 10){
                break;
            }
            int firstCoinX = now.firstCoinX;
            int firstCoinY = now.firstCoinY;
            int secondCoinX = now.secondCoinX;
            int secondCoinY = now.secondCoinY;
            coinVisited[firstCoinX][firstCoinY][secondCoinX][secondCoinY] = true;
            for (int i = 0; i < 4; i++) {
                int firstCoinNx = firstCoinX + dx[i];
                int firstCoinNy = firstCoinY + dy[i];
                int secondCoinNx = secondCoinX + dx[i];
                int secondCoinNy = secondCoinY + dy[i];
                if (!canMove(firstCoinNx, firstCoinNy)) {
                    firstCoinNx = firstCoinX;
                    firstCoinNy = firstCoinY;
                }
                if (!canMove(secondCoinNx, secondCoinNy)) {
                    secondCoinNx = secondCoinX;
                    secondCoinNy = secondCoinY;
                }
                int flag = 0;
                if(firstCoinNx >= 0 && firstCoinNy >= 00 && firstCoinNx < n && firstCoinNy < m){
                    flag++;
                }
                if(secondCoinNx >= 0 && secondCoinNy >= 00 && secondCoinNx < n && secondCoinNy < m){
                    flag++;
                }
                if(flag == 1){
                    return now.move + 1;
                } else if (flag == 2 && !coinVisited[firstCoinNx][firstCoinNy][secondCoinNx][secondCoinNy]) {
                    coinQueue.offer(new Coins(firstCoinNx, firstCoinNy, secondCoinNx, secondCoinNy, now.move + 1));
                }
            }
        }
        return -1;
    }


    private static boolean canMove(int x, int y){
        if(x >= 0 && y >= 0 && x < n && y < m && board[x][y] == '#') {
            return false;
        }
        return true;
    }

}
