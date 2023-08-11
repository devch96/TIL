import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1253_좋다 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        List<Integer> numbers = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            numbers.add(Integer.parseInt(st.nextToken()));
        }
        Collections.sort(numbers);
        int answer = 0;
        for (int idx = 0; idx < n; idx++) {
            int target = numbers.get(idx);
            if (twoPointCount(numbers, target, idx)) {
                answer++;
            }
        }
        bw.write(String.valueOf(answer));
        bw.flush();
    }

    private static boolean twoPointCount(List<Integer> numbers, int target, int nowIdx){
        int left_idx = 0;
        int right_idx = numbers.size()-1;
        while (left_idx < right_idx) {
            int tempSum = numbers.get(left_idx) + numbers.get(right_idx);
            if (tempSum == target) {
                if (left_idx != nowIdx && right_idx != nowIdx) {
                    return true;
                } else if (left_idx == nowIdx) {
                    left_idx++;
                } else if (right_idx == nowIdx) {
                    right_idx--;
                }
            }else if(tempSum < target){
                left_idx++;
            }else{
                right_idx--;
            }
        }
        return false;
    }
}
