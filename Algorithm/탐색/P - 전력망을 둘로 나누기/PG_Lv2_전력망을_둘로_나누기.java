import java.util.ArrayList;
import java.util.List;

public class PG_Lv2_전력망을_둘로_나누기 {
    static List<Integer>[] arrList;
    static boolean[] visited;
    static int maxValue;
    static int count;

    public static int solution(int n, int[][] wires){
        arrList = new List[n + 1];
        maxValue = Integer.MAX_VALUE;

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < wires.length; i++) {
            int start = wires[i][0];
            int end = wires[i][1];
            arrList[start].add(end);
            arrList[end].add(start);
        }
        bruteForce();
        return maxValue;
    }

    private static void bruteForce(){
        for (int i = 1; i < arrList.length; i++) {
            for (int j = 0; j < arrList[i].size(); j++) {
                int removeEdge = arrList[i].remove(j);
                List<Integer> countArr = new ArrayList<>();
                visited = new boolean[arrList.length + 1];
                for (int k = 1; k < arrList.length; k++) {
                    if (!visited[k]) {
                        count = 0;
                        dfs(k);
                        countArr.add(count);
                    }
                }
                if (countArr.size() == 2) {
                    maxValue = Math.min(maxValue, Math.abs(countArr.get(0) - countArr.get(1)));
                }
                arrList[i].add(j, removeEdge);
            }
        }
    }

    private static void dfs(int node){
        visited[node]=true;
        if (!arrList[node].isEmpty()) {
            for (int nextNode : arrList[node]) {
                if(!visited[nextNode]){
                    count++;
                    dfs(nextNode);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(9,new int[][] {{1,3}, {2,3},{3,4},{4,5},{4,6},{4,7},{7,8},{7,9}}));
        System.out.println(solution(4,new int[][] {{1,2}, {2,3},{3,4}}));
        System.out.println(solution(7,new int[][] {{1,2}, {2,7},{3,7},{3,4},{4,5},{6,7}}));
    }
}
