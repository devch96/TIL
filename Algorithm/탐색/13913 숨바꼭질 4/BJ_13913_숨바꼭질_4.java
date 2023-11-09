import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_13913_숨바꼭질_4 {
    static class Route{
        public List<Integer> route = new ArrayList<>();

    }
    static int start;
    static int destination;
    static boolean[] visited;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        start = Integer.parseInt(st.nextToken());
        destination = Integer.parseInt(st.nextToken());
        visited = new boolean[100_001];
        if (start > destination) {
            System.out.println(start - destination);
            for (int i = start; i >= destination ; i--) {
                System.out.print(i + " ");
            }
            return;
        }
        List<Integer> answer = bfs();
        System.out.println(answer.size() - 1);
        for(int route : answer){
            System.out.print(route + " ");
        }
    }

    private static List<Integer> bfs(){
        Queue<List<Integer>> queue = new ArrayDeque<>();
        List<Integer> route = new ArrayList<>();
        visited[start] = true;
        route.add(start);
        queue.add(route);
        while (!queue.isEmpty()) {
            List<Integer> now = queue.poll();
            int location = now.get(now.size()-1);
            if(location == destination){
                return now;
            }
            for (int i = 0; i < 3; i++) {
                int next;
                if(i == 0){
                    next = location + 1;
                } else if (i == 1) {
                    next = location - 1;
                }else{
                    next = location * 2;
                }
                if (0 > next || next > 100000) {
                    continue;
                }
                if (!visited[next]) {
                    visited[next] = true;
                    List<Integer> nextRoute = new ArrayList<>(now);
                    nextRoute.add(next);
                    queue.add(nextRoute);
                }
            }
        }
        return null;
    }
}
