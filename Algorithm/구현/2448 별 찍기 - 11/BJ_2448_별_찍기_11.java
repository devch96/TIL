import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_2448_별_찍기_11 {
    static char[][] map;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        map = new char[n][n * 2 - 1];
        for (int i = 0; i < n; i++) {
            Arrays.fill(map[i],' ');
        }
        solve(0, n - 1, n);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n * 2 - 1; j++) {
                sb.append(map[i][j]);
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private static void solve(int row, int col, int n){
        if(n == 3){
            map[row][col] = '*';
            map[row+1][col+1] = map[row+1][col-1] = '*';
            map[row+2][col+2] = map[row+2][col-2] = map[row+2][col+1] = map[row+2][col-1] = map[row+2][col] = '*';
        }else{
            int cut = n/2;
            solve(row,col,cut);
            solve(row + cut, col + cut, cut);
            solve(row + cut, col - cut, cut);
        }
    }

}
