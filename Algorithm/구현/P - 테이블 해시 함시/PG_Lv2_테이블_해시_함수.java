import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PG_Lv2_테이블_해시_함수 {
    public static int solution(int[][] data, int col, int row_begin, int row_end){
        Arrays.sort(data, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[col - 1] == o2[col - 1]) {
                    return  o2[0] - o1[0];
                }
                return o1[col-1] - o2[col-1];
            }
        });
        List<Integer> beforeHash = new ArrayList<>();
        for (int i = row_begin - 1; i < row_end; i++) {
            int result = 0;
            for(int d : data[i]){
                result += d % (i+1);
            }
            beforeHash.add(result);
        }
        int answer = beforeHash.get(0);
        for (int i = 1; i < beforeHash.size(); i++) {
            answer = answer ^ beforeHash.get(i);
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[][] {{2,2,6},{1,5,10},{4,2,9},{3,8,3}}, 2, 2, 3));
    }
}
