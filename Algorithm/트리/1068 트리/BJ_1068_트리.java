import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1068_트리 {
    static boolean[] visited;
    static List<Integer>[] arrList;

    static int answer;
    static int remove;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        arrList = new List[n];
        visited = new boolean[n];
        answer = 0;
        for (int i = 0; i < n; i++) {
            arrList[i] = new ArrayList<>();
        }
        int start = -1;
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int a = Integer.parseInt(st.nextToken());
            if(a == -1){
                start = i;
                continue;
            }
            arrList[i].add(a);
            arrList[a].add(i);
        }
        remove = Integer.parseInt(br.readLine());
        if(remove == start){
            System.out.println(0);
        } else{
            dfs(start);
            System.out.println(answer);
        }
    }

    private static void dfs(int start) {
        int leaf = 0;
        visited[start] = true;
        for (int next : arrList[start]) {
            if (!visited[next] && next != remove) {
                leaf++;
                dfs(next);
            }
        }
        if(leaf == 0){
            answer++;
        }
    }
}
