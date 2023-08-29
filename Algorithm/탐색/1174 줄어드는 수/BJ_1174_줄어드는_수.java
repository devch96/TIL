import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BJ_1174_줄어드는_수 {
    static int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    static List<Long> answer;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        answer = new ArrayList<>();
        dfs(0, 0);
        Collections.sort(answer);
        if (n > 1023) {
            System.out.println(-1);
        } else {
            System.out.println(answer.get(n - 1));
        }
    }

    private static void dfs(long num, int idx){
        if (!answer.contains(num)) {
            answer.add(num);
        }
        if(idx >= 10){
            return;
        }
        dfs((num * 10) + arr[idx], idx + 1);
        dfs(num, idx + 1);
    }
}
