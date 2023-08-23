import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1138_한_줄로_서기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n+1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        List<Integer> answer = new ArrayList<>();
        for (int i = n; i >= 1; i--) {
            answer.add(arr[i],i);
        }
        for (int i = 0; i < n; i++) {
            System.out.print(answer.get(i)+" ");
        }
        System.out.println();
    }
}
