import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_17088_등차수열_반환 {
    static int[] arr;
    static int answer;
    static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        if (n == 1) {
            System.out.println(0);
            return;
        }
        arr = new int[n];
        answer = Integer.MAX_VALUE;
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int[] temp = arr.clone();
                int cnt = 0;
                boolean success = true;
                if (i != 0) {
                    cnt++;
                }
                if (j != 0) {
                    cnt++;
                }
                temp[0] += i;
                temp[1] += j;
                int diff = temp[1] - temp[0];
                for (int k = 2; k < n; k++) {
                    int curDiff = temp[k] - temp[k - 1];
                    boolean flag = false;
                    for (int l = -1; l <= 1; l++) {
                        if (curDiff + l == diff) {
                            if (l != 0) {
                                cnt++;
                            }
                            temp[k] +=l;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    answer = Math.min(answer, cnt);
                }
            }
        }
        if (answer == Integer.MAX_VALUE) {
            answer = -1;
        }
        System.out.println(answer);
    }
}
