import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_10830_행렬_제곱 {
    static int[][] graph;
    static int n;
    static int mod = 1000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        long b = Long.parseLong(st.nextToken());

        graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken()) % mod;
            }
        }
        int[][] answer = divideConquer(graph, b);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(answer[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int[][] divideConquer(int[][] arr, long b) {
        if (b == 1L) {
            return arr;
        }
        int[][] result = divideConquer(arr, b/2);
        result = pow(result, result);
        if (b % 2 == 1L) {
            result = pow(result, graph);
        }
        return result;
    }

    private static int[][] pow(int[][] arr1, int[][] arr2){
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += arr1[i][k] * arr2[k][j];
                    result[i][j] %= mod;
                }
            }
        }
        return result;
    }
}
