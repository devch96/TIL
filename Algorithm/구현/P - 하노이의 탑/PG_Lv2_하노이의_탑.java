import java.util.ArrayList;
import java.util.List;

public class PG_Lv2_하노이의_탑 {
    static List<int[]> arrList;
    public int[][] solution(int n){
        arrList = new ArrayList<>();
        dfs(n,1,3,2);

        int[][] answer = new int[arrList.size()][];
        for(int i=0; i<arrList.size(); i++){
            answer[i] = arrList.get(i);
        }

        return answer;
    }

    private static void dfs(int n, int start, int to, int mid){
        if(n==1){
            arrList.add(new int[]{start,to});
            return;
        }
        dfs(n-1, start, mid, to);
        arrList.add(new int[] {start, to});
        dfs(n-1, mid, to, start);
    }
}
