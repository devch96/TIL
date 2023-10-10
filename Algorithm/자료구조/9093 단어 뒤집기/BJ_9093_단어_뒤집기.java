import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_9093_단어_뒤집기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            Deque<Character> stack = new ArrayDeque<>();
            String sentence = br.readLine() + "\n";
            for (Character c : sentence.toCharArray()) {
                if(c == ' ' || c == '\n'){
                    while (!stack.isEmpty()) {
                        bw.write(stack.pop());
                    }
                    bw.write(c);
                } else{
                  stack.push(c);
                }
            }
            bw.flush();
        }
    }
}
