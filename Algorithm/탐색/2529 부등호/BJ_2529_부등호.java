import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.StringTokenizer;

public class BJ_2529_부등호 {
    static int k;
    static boolean[] visited;
    static String[] inequalitySign;
    static int[] temp;
    static Deque<int[]> answer;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        k = Integer.parseInt(br.readLine());
        inequalitySign = new String[k+1];
        visited = new boolean[10];
        temp = new int[k+1];
        answer = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < k; i++) {
            inequalitySign[i] = st.nextToken();
        }
        backtrack(0);
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        int[] minAnswer = answer.pollFirst();
        int[] maxAnswer = answer.pollLast();
        for (int i = 0; i <= k; i++) {
            sb.append(maxAnswer[i]);
            sb1.append(minAnswer[i]);
        }
        sb.append("\n");
        sb.append(sb1);
        bw.write(sb.toString());
        bw.flush();
    }

    private static void backtrack(int depth){
        if(depth == k+1){
            int[] answer2 = Arrays.copyOf(temp, k+1);
            answer.addLast(answer2);
            return;
        }
        for (int i = 0; i < 10; i++) {
            if (depth == 0) {
                visited[i] = true;
                temp[depth] = i;
                backtrack(depth+1);
                visited[i] = false;
                continue;
            }
            if(!visited[i] && inequalityCheck(inequalitySign[depth-1], temp[depth-1], i)){
                visited[i] = true;
                temp[depth] = i;
                backtrack(depth+1);
                visited[i] = false;
            }
        }
    }

    private static boolean inequalityCheck(String sign, int before, int after) {
        if (sign.equals("<")) {
            return before < after;
        }else{
            return before > after;
        }
    }
}
