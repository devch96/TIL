import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1516_게임_개발 {
    static List<Integer>[] arrList;
    static int[] indegree;
    static int[] selfBuild;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        arrList = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        indegree = new int[n + 1];
        selfBuild = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            selfBuild[i] = Integer.parseInt(st.nextToken());
            while(true){
                int preTemp = Integer.parseInt(st.nextToken());
                if(preTemp == -1){
                    break;
                }
                arrList[preTemp].add(i);
                indegree[i]++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            if(indegree[i] == 0){
                queue.add(i);
            }
        }
        int[] result = new int[n + 1];
        while (!queue.isEmpty()) {
            int now = queue.poll();
            for (int next : arrList[now]) {
                indegree[next]--;
                result[next] = Math.max(result[next], result[now] + selfBuild[now]);
                if (indegree[next] == 0) {
                    queue.add(next);
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            System.out.println(result[i] + selfBuild[i]);
        }
    }
}
