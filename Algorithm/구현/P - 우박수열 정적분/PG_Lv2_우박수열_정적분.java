import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PG_Lv2_우박수열_정적분 {
    public static double[] solution(int k, int[][] ranges){
        List<Integer> collatzPoint = getCollatzPoint(k);
        double[] answer = new double[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            answer[i] = getIntegral(collatzPoint, ranges[i]);
        }
        return answer;
    }

    private static List<Integer> getCollatzPoint(int k){
        List<Integer> result = new ArrayList<>();
        while(k != 1){
            result.add(k);
            k = k % 2 == 0 ? k / 2 : k * 3 + 1;
        }
        result.add(1);
        return result;
    }

    private static double getIntegral(List<Integer> collatzPoint, int[] range){
        double answer = 0;
        int startX = range[0];
        int endX = range[1];
        if(startX == endX){
            endX = collatzPoint.size() - 1;
        }else{
            endX = collatzPoint.size()-1 + endX;
        }
        if(startX > endX){
            return -1;
        }
        for (int x = startX; x < endX ; x++) {
            double square = Math.max(collatzPoint.get(x), collatzPoint.get(x + 1));
            double triangle = (double) Math.abs(collatzPoint.get(x) - collatzPoint.get(x+1)) / 2;
            answer += square - triangle;
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(5,new int[][] {{0,0},{0,-1},{2,-3},{3,-3}})));
        System.out.println(Arrays.toString(solution(3,new int[][] {{0,0},{1,-2},{3,-3}})));
    }
}
