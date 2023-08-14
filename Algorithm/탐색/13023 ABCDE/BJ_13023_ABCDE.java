import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_13023_ABCDE {
    public static List<Integer>[] arrList;
    public static boolean[] visited;
    public static boolean arrive;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        arrList = new ArrayList[n];
        visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            arrList[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
            arrList[end].add(start);
        }
        for (int i = 0; i < n; i++) {
            dfs(i, 1);
            if(arrive){
                break;
            }
        }
        if(arrive){
            System.out.println("1");
        }else{
            System.out.println("0");
        }
    }

    private static void dfs(int now, int depth) {
        if (depth == 5 || arrive) {
            arrive = true;
            return;
        }
        visited[now] = true;
        for (int i : arrList[now]) {
            if (!visited[i]) {
                dfs(i, depth+1);
            }
        }
        visited[now] = false;
    }
}
