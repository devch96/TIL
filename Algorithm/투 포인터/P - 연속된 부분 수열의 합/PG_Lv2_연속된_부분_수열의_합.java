import java.util.Arrays;

public class PG_Lv2_연속된_부분_수열의_합 {
    public static int[] solution(int[] sequence, int k){
        int startIdx = 0;
        int endIdx = 0;
        int[] answer = new int[2];
        int size = Integer.MAX_VALUE;
        int partSum = sequence[0];
        while (startIdx < sequence.length && endIdx < sequence.length) {
            if(partSum < k){
                endIdx++;
                if (endIdx < sequence.length) {
                    partSum += sequence[endIdx];
                }
            } else if (partSum > k) {
                partSum -= sequence[startIdx];
                startIdx++;
            } else {
                if (size > endIdx - startIdx) {
                    answer[0] = startIdx;
                    answer[1] = endIdx;
                    size = endIdx - startIdx;
                }
                partSum -= sequence[startIdx];
                startIdx++;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new int[]{1, 1, 1, 2, 3, 4, 5}, 5)));
        System.out.println(Arrays.toString(solution(new int[]{1,2,3,4,5}, 7)));
        System.out.println(Arrays.toString(solution(new int[]{2,2,2,2,2}, 6)));
    }
}
