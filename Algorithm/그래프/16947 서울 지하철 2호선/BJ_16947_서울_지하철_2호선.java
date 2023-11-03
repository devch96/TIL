import java.io.*;
import java.util.*;

public class BJ_16947_서울_지하철_2호선 {
    static class Station{
        int num;
        int distance;

        public Station(int num, int distance) {
            this.num = num;
            this.distance = distance;
        }
    }

    static List<Integer>[] arrList;
    static boolean[] visited;
    static boolean[] isCycle;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(br.readLine());
        arrList = new ArrayList[n+1];

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }
        StringTokenizer st;

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            arrList[start].add(end);
            arrList[end].add(start);
        }


        isCycle = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            if(checkCycle(i,i,i)){
                break;
            }
            isCycle = new boolean[n + 1];
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if(isCycle[i]){
                sb.append(0).append(" ");
            }else{
                visited = new boolean[n + 1];
                visited[i] = true;
                sb.append(bfs(new Station(i, 0))).append(" ");
            }
        }
        bw.write(sb.toString());
        bw.flush();
    }

    private static boolean checkCycle(int prev, int node, int start){
        isCycle[node] = true;
        for (int next : arrList[node]) {
            if (!isCycle[next]) {
                if(checkCycle(node,next,start)){
                    return true;
                }
            }else if(next != prev && next == start){
                    return true;
            }
        }
        isCycle[node] = false;
        return false;
    }

    private static int bfs(Station start){
        Queue<Station> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Station now = queue.poll();
            int nowStationNumber = now.num;
            int nowStationDistance = now.distance;
            if (isCycle[now.num]) {
                return nowStationDistance;
            }
            for (int next : arrList[nowStationNumber]) {
                if (!visited[next]) {
                    visited[next] = true;
                    queue.add(new Station(next, now.distance+1));
                }
            }
        }
        return 0;
    }
}
