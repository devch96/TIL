import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_15663_N과_M_9 {
    static List<Integer> list;
    static boolean[] visited;
    static List<String> answer;
    static Set<String> seq;
    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        list = new ArrayList<>();
        visited = new boolean[n];
        answer = new ArrayList<>();
        seq = new HashSet<>();

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            list.add(Integer.parseInt(st.nextToken()));
        }
        Collections.sort(list);
        for (int i = 0; i < n; i++) {
            visited[i] = true;
            backtrack(String.valueOf(list.get(i)), 1);
            visited[i] = false;
        }
        for (String s : answer) {
            System.out.println(s);
        }
    }

    private static void backtrack(String num, int depth){
        if(depth == m){
            if (!seq.contains(num)) {
                answer.add(num);
                seq.add(num);
            }
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            int next = list.get(i);
            if(!visited[i]){
                visited[i] = true;
                backtrack(num+" "+next, depth+1);
                visited[i] = false;
            }
        }
    }
}
