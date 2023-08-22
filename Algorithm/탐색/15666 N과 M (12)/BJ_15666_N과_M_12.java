import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_15666_N과_M_12 {
    static int n;
    static int m;
    static Set<String> seq;
    static List<String> answer;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        arr = new int[n];
        seq = new HashSet<>();
        answer = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(arr);
        for (int i = 0; i < n; i++) {
            backtrack(String.valueOf(arr[i]), i, 1);
        }
        for(String s : answer){
            System.out.println(s);
        }
    }

    private static void backtrack(String num, int start, int depth){
        if(depth == m){
            if (!seq.contains(num)) {
                answer.add(num);
                seq.add(num);
            }
            return;
        }
        for (int i = start; i < n; i++) {
            backtrack(num + " " + arr[i], i, depth+1);
        }
    }
}
