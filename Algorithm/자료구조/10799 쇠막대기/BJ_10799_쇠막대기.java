import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_10799_쇠막대기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String s = br.readLine().strip();
        s = s.replace("()", "L");
        Deque<Character> stack = new ArrayDeque<>();
        int answer = 0;
        for (char c : s.toCharArray()) {
            if(c == '('){
                stack.push(c);
            } else if (c == ')') {
                stack.pop();
                answer++;
            }else{
                if (!stack.isEmpty()) {
                    answer += stack.size();
                }
            }
        }
        bw.write(String.valueOf(answer));
        bw.flush();
    }
}
