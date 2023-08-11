import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_1872_스택_수열 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }
        Deque<Integer> stack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        int num = 1;
        boolean result = true;
        for (int i = 0; i < n; i++) {
            int arrNum = arr[i];
            if (arrNum >= num) {
                while(arrNum >= num){
                    stack.push(num++);
                    sb.append("+\n");
                }
                stack.pop();
                sb.append("-\n");
            }else{
                int s = stack.pop();
                if (s > arrNum) {
                    bw.write("NO");
                    bw.flush();
                    result = false;
                    break;
                }else{
                    sb.append("-\n");
                }
            }
        }
        if(result){
            bw.write(sb.toString());
            bw.flush();
        }
    }
}
