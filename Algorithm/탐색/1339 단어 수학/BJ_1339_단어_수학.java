import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_1339_단어_수학 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[][] arr = new int[26][1];
        for (int i = 0; i < n; i++) {
            String input = br.readLine().strip();
            int length = input.length();
            for (int j = 0; j < length; j++) {
                char c = input.charAt(j);
                arr[c-65][0] += (int) Math.pow(10, length - 1 - j);
            }
        }

        Arrays.sort(arr,(o1, o2) -> o2[0] - o1[0]);
        int answer = 0;
        int num = 9;
        for (int i = 0; i < 26; i++) {
            if(arr[i][0] == 0){
                continue;
            }
            answer += arr[i][0] * num;
            num--;
        }
        System.out.println(answer);
    }
}
