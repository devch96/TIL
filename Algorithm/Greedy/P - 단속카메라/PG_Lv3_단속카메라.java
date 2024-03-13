import java.util.Arrays;
import java.util.Comparator;

public class PG_Lv3_단속카메라 {
    public int solution(int[][] routes) {
        Arrays.sort(routes, Comparator.comparingInt(arr -> arr[1]));
        int answer = 0;
        int cameraLoc = -30001;
        for(int[] route : routes) {
            if(route[0] > cameraLoc) {
                answer++;
                cameraLoc = route[1];
            }
        }
        return answer;
    }


    public static void main(String[] args) {
        PG_Lv3_단속카메라 p = new PG_Lv3_단속카메라();
        p.solution(new int[][] {{-20,-15},{-14,-5},{-18,-13},{-5,-3}});
    }
}
