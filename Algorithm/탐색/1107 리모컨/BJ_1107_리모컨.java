import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1107_리모컨 {
    static boolean[] error;
    static int min = 1_000_000;
    static int target;
    static int targetLength;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String n = br.readLine().strip();
        target = Integer.parseInt(n);
        targetLength = n.length();
        int m = Integer.parseInt(br.readLine());
        error = new boolean[10];
        if(target == 100){
            System.out.println(0);
            return;
        }
        if(m != 0){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 0; i < m; i++) {
                error[Integer.parseInt(st.nextToken())] = true;
            }
        }
        min = Math.min(Math.abs(100 - target), min);
        dfs(0,"");
        System.out.println(min);
    }

    private static void dfs(int index, String click){
        if (index > targetLength) {
            return;
        }
        for (int i = 0; i < 10; i++) {
            if (!error[i]) {
                String newClick = click + i;
                int cnt = Math.abs(target - Integer.parseInt(newClick))+ newClick.length();
                min = Math.min(cnt, min);
                dfs(index + 1, newClick);
            }
        }
    }
}
