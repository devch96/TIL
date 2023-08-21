import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_5430_AC {
    static boolean error;
    static boolean reversed;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        for (int q = 0; q < t; q++) {
            String p = br.readLine().strip();
            int n = Integer.parseInt(br.readLine());
            Deque<String> deque = new ArrayDeque<>();
            String arr = br.readLine().strip();
            String[] numbers = arr.substring(1, arr.length() - 1).split(",");
            Collections.addAll(deque, numbers);;
            error = false;
            reversed = false;
            for (char c : p.toCharArray()) {
                if (c == 'R') {
                    reversed = !reversed;
                }else{
                    deque = drop(deque);
                }
                if(error){
                    break;
                }
            }
            if(error){
                System.out.println("error");
            }else{
                print(deque);
            }
        }
    }

    private static void print(Deque<String> deque){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (!deque.isEmpty()) {
            if(reversed){
                sb.append(deque.pollLast());
                while (!deque.isEmpty()) {
                    sb.append(',').append(deque.pollLast());
                }
            }else{
                sb.append(deque.pollFirst());
                while (!deque.isEmpty()) {
                    sb.append(',').append(deque.pollFirst());
                }
            }
        }
        sb.append(']');
        System.out.println(sb);
    }


    private static Deque<String> drop(Deque<String> deque) {
        if (deque.isEmpty() || deque.peekFirst().equals("")) {
            error = true;
            return null;
        }else{
            if(!reversed) {
                deque.removeFirst();
                return deque;
            }else{
                deque.removeLast();
                return deque;
            }
        }
    }
}
