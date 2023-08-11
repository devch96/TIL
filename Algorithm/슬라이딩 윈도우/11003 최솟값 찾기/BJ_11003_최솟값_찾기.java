import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class BJ_11003_최솟값_찾기 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());
        Deque<Node> deque = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int inputNum = Integer.parseInt(st.nextToken());
            while (!deque.isEmpty() && deque.getLast().value > inputNum) {
                deque.removeLast();
            }
            deque.add(new Node(inputNum, i));
            if (deque.getFirst().index <= i - l) {
                deque.removeFirst();
            }
            bw.write(deque.getFirst().value + " ");
        }
        bw.flush();
    }

    static class Node{
        public int value;
        public int index;

        Node(int value, int index){
            this.value = value;
            this.index = index;
        }
    }
}
