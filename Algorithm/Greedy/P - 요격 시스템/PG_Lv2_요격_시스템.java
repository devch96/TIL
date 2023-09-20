import java.util.Arrays;
import java.util.Comparator;

public class PG_Lv2_요격_시스템 {
    public static int solution(int[][] targets){
        Arrays.sort(targets, Comparator.comparingInt(x -> x[0]));
        int answer = 1;
        int rangeStart = targets[0][0];
        int rangeEnd = targets[0][1];
        for (int i = 1; i < targets.length; i++) {
            int nowRangeStart = targets[i][0];
            int nowRangeEnd = targets[i][1];
            if (rangeStart <= nowRangeStart && nowRangeStart < rangeEnd) {
                rangeStart = Math.max(rangeStart, nowRangeStart);
                rangeEnd = Math.min(rangeEnd, nowRangeEnd);
            }else{
                answer++;
                rangeStart = nowRangeStart;
                rangeEnd = nowRangeEnd;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[][] {{4,5},{4,8},{10,14},{11,13},{5,12},{3,7},{1,4}}));
    }
}
