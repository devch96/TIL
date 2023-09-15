import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PG_Lv2_숫자_블록 {
    public static int[] solution(long begin, long end){
        int[] answer = new int[(int)(end - begin) + 1];

        for (int i = (int)begin,idx = 0; i <= end; i++) {
            answer[idx++] = getMaxMeasure(i);
        }

        return answer;
    }



    private static int getMaxMeasure(int num){
        if(num == 1){
            return 0;
        }
        List<Integer> measureList = new ArrayList<>();
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                if(num / i <= 10_000_000){
                    return num / i;
                }
                measureList.add(i);
            }
        }
        if (measureList.isEmpty()) {
            return 1;
        }
        return measureList.get(measureList.size()-1);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(1,10)));
    }
}
