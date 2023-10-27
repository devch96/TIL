import java.io.*;
import java.util.StringTokenizer;

public class BJ_10971_외판원_순환_2 {
    static int[][] graph;
    static int n;
    static boolean[] visited;
    static int minValue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        n = Integer.parseInt(br.readLine());
        graph = new int[n][n];
        visited = new boolean[n];
        minValue = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                graph[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        for (int i = 0; i < n; i++) {
            visited[i] = true;
            backtrack(i,i, 0,1);
            visited[i] = false;
        }
        bw.write(String.valueOf(minValue));
        bw.flush();

    }

    private static void backtrack(int origin,int now, int value, int depth){
        if(depth == n){
            if(graph[now][origin] != 0){
                minValue = Math.min(minValue, value+graph[now][origin]);
            }
            return;
        }
        for (int i = 0; i < n; i++) {
            if(graph[now][i] == 0){
                continue;
            }
            if (!visited[i]) {
                visited[i] = true;
                backtrack(origin,i,value+ graph[now][i], depth+1);
                visited[i] = false;
            }
        }
    }
}
