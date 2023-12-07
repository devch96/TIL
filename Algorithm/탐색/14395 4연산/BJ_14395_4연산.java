import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_14395_4연산 {
    static class Node{
        String operation;
        long value;

        public Node(String operation, long value) {
            this.operation = operation;
            this.value = value;
        }
    }

    static Set<Long> hs;
    static long start;
    static long target;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        start = Long.parseLong(st.nextToken());
        target = Long.parseLong(st.nextToken());
        hs = new HashSet<>();
        if (target == start) {
            System.out.println("0");
        } else {
            String answer = bfs();
            if (answer.equals("Impossible")) {
                System.out.println("-1");
            }else{
                System.out.println(answer);
            }
        }

    }

    private static String bfs(){
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node("", start));
        hs.add(start);
        String[] op = new String[] {"*", "+", "-", "/"};
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            String operation = now.operation;
            long value = now.value;
            if (value < 0) {
                continue;
            }
            if(value == target){
                return operation.toString();
            }
            long nextValue = value;
            for (int i = 0; i < 4; i++) {
                switch(i) {
                    case 0:
                        nextValue = value * value;
                        break;
                    case 1:
                        nextValue = value + value;
                        break;
                    case 2:
                        nextValue = value - value;
                        break;
                    case 3:
                        if(value != 0) {
                            nextValue = value / value;
                        }
                        break;
                }
                if (!hs.contains(nextValue)) {
                    hs.add(nextValue);
                    queue.offer(new Node(operation + op[i], nextValue));
                }
            }
        }
        return "Impossible";
    }
}
