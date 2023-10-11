import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class BJ_17413_단어_뒤집기_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String s = br.readLine().strip();
        StringBuilder sb = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        Queue<Character> queue = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if(c == '<'){
                while (!stack.isEmpty()) {
                    sb.append(stack.pop());
                }
                queue.offer(c);
            } else if(c == '>'){
                while (!queue.isEmpty()) {
                    sb.append(queue.poll());
                }
                sb.append(c);
            }else if (!queue.isEmpty()) {
                queue.offer(c);
            }else if(c == ' '){
                while (!stack.isEmpty()) {
                    sb.append(stack.pop());
                }
                sb.append(c);
            }else{
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }
}
