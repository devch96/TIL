import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2580_스도쿠 {
    static int[][] graph;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        graph = new int[9][9];
        for (int i = 0; i < 9; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 9; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }

    private static void sudoku(int row, int col){
        if (col == 9) {
            sudoku(row+1,0);
            return;
        }
        if(row==9){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    System.out.print(graph[i][j] + " ");
                }
                System.out.println();
            }
            System.exit(0);
        }

        if (graph[row][col] == 0) {
            for (int i = 1; i <= 9; i++) {
                if(isPossible(row,col,i)){
                    graph[row][col]=i;
                    sudoku(row,col+1);
                }
            }
            graph[row][col] = 0;
            return;
        }
        sudoku(row,col+1);
    }


    private static boolean isPossible(int row, int col, int value){
        for (int i = 0; i < 9; i++) {
            if (graph[row][i] == value) {
                return false;
            }
            if(graph[i][col] == value){
                return false;
            }
        }

        int set_row = (row/3)*3;
        int set_col = (col/3)*3;

        for (int i = set_row; i < set_row + 3; i++) {
            for (int j = set_col; j < set_col + 3; j++) {
                if(graph[i][j] == value){
                    return false;
                }
            }
        }
        return true;
    }
}
