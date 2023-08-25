import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2630_색종이_만들기 {
    static int[][] graph;
    static int white;
    static int blue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        graph = new int[n][n];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        devideConquer(0,0,n);
        System.out.println(white);
        System.out.println(blue);
    }

    private static void devideConquer(int x, int y, int size){
        if (isPossible(x, y, size)) {
            if(graph[x][y] == 1){
                blue++;
            }else{
                white++;
            }
            return;
        }

        int newSize = size / 2;
        devideConquer(x , y , newSize);
        devideConquer(x , y + newSize , newSize);
        devideConquer(x + newSize, y , newSize);
        devideConquer(x + newSize, y + newSize, newSize);

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
