import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_12919_A와_B_2 {
    static String start;
    static String target;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        start = br.readLine();
        target = br.readLine();
        System.out.println(dfs(target, target.length()) ? 1 : 0);
    }

    private static boolean dfs(String now, int depth){
        if (depth == start.length()) {
            return now.equals(start);
        }
        if(now.charAt(depth-1) == 'A'){
            if (dfs(now.substring(0, depth - 1), depth - 1)) {
                return true;
            }
        }
        if (now.charAt(0) == 'B') {
            if (dfs(reverse(now.substring(1, depth)), depth -1 )) {
                return true;
            }
        }

        return false;
    }

    private static String reverse(String s){
        return new StringBuilder(s).reverse().toString();
    }
}
