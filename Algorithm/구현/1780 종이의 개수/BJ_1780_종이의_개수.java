import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1780_종이의_개수 {
    static int[][] graph;
    static int minusOne;
    static int zero;
    static int one;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        graph = new int[n][n];
        minusOne = 0;
        zero = 0;
        one = 0;

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        divideConquer(0,0,n);
        System.out.println(minusOne);
        System.out.println(zero);
        System.out.println(one);
    }

    private static void divideConquer(int x, int y, int size) {
        if (isPossible(x, y, size)) {
            if(graph[x][y] == 1) {
                one++;
            } else if (graph[x][y] == 0) {
                zero++;
            }else{
                minusOne++;
            }
            return;
        }

        int newSize = size / 3;
        divideConquer(x,y,newSize);
        divideConquer(x+newSize,y,newSize);
        divideConquer(x+newSize*2,y,newSize);
        divideConquer(x,y+newSize,newSize);
        divideConquer(x,y+newSize*2,newSize);
        divideConquer(x+newSize,y+newSize,newSize);
        divideConquer(x+newSize,y+newSize*2,newSize);
        divideConquer(x+newSize*2,y+newSize,newSize);
        divideConquer(x+newSize*2,y+newSize*2,newSize);
    }

    private static boolean isPossible(int x, int y, int size) {
        int checkValue = graph[x][y];
        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                if (checkValue != graph[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
