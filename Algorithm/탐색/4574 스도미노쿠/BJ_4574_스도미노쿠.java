import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_4574_스도미노쿠 {
    static int[][] graph;
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0, 1};
    static boolean[][] isMarkedDomino;
    static boolean flag;
    static int count;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        count = 0;
        while (true) {
            int n = Integer.parseInt(br.readLine());
            if(n == 0){
                break;
            }
            count++;
            graph = new int[9][9];
            isMarkedDomino = new boolean[10][10];
            for (int i = 0; i < n; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                String lu = st.nextToken();
                int v = Integer.parseInt(st.nextToken());
                String lv = st.nextToken();
                graph[lu.charAt(0) - 65][lu.charAt(1) - '0' - 1]  = u;
                graph[lv.charAt(0) - 65][lv.charAt(1) - '0' - 1]  = v;
                isMarkedDomino[u][v] = true;
                isMarkedDomino[v][u] = true;
            }
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= 9; i++) {
                String lu = st.nextToken();
                graph[lu.charAt(0) - 65][lu.charAt(1) - '0' - 1]  = i;
            }
            flag = false;
            sudominoku(0,0);
        }
    }

    private static void sudominoku(int row, int col){
        if (col == 9) {
            sudominoku(row+1, 0);
            return;
        }
        if(row == 9){
            if(flag){
                return;
            }
            flag = true;
            StringBuilder sb = new StringBuilder();
            sb.append("Puzzle ");
            sb.append(count);
            sb.append("\n");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sb.append(graph[i][j]);
                }
                sb.append("\n");
            }
            System.out.print(sb);
            return;
        }
        if(graph[row][col] != 0){
            sudominoku(row, col+1);
            return;
        }

        for (int i = 1; i <= 9; i++) {
            if (!isPossible(row, col, i)) {
                continue;
            }
            for (int j = 0; j < 4; j++) {
                int newRow = row + dx[j];
                int newCol = col + dy[j];
                if (newRow < 0 || newCol < 0 || newRow >= 9 || newCol >= 9) {
                    continue;
                }
                if (graph[newRow][newCol] == 0) {
                    for (int k = 1; k <= 9; k++) {
                        if (isMarkedDomino[i][k] || !isPossible(newRow, newCol, k) || k == i) {
                            continue;
                        }
                        graph[row][col] = i;
                        graph[newRow][newCol] = k;
                        isMarkedDomino[i][k] = true;
                        isMarkedDomino[k][i] = true;
                        sudominoku(row,col+1);
                        graph[row][col] = 0;
                        graph[newRow][newCol] = 0;
                        isMarkedDomino[i][k] = false;
                        isMarkedDomino[k][i] = false;
                    }
                }
            }
        }

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
