import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1992_쿼드트리 {
    static int[][] graph;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            String line = br.readLine().strip();
            for (int j = 0; j < n; j++) {
                graph[i][j] = line.charAt(j) - '0';
            }
        }
        devideConquer(0,0,n);
        System.out.println(sb);
    }

    private static void devideConquer(int x, int y, int size){
        if(isPossible(x,y,size)){
            sb.append(graph[x][y]);
            return;
        }

        int newSize = size / 2;
        sb.append("(");
        devideConquer(x,y,newSize);
        devideConquer(x,y+newSize,newSize);
        devideConquer(x+newSize,y,newSize);
        devideConquer(x+newSize,y+newSize,newSize);
        sb.append(")");

    }

    private static boolean isPossible(int x, int y, int size){
        int checkValue = graph[x][y];
        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                if (graph[i][j] != checkValue) {
                    return false;
                }
            }
        }
        return true;
    }
}
