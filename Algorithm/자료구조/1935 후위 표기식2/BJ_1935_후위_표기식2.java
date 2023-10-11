import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_1935_후위_표기식2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        String s = br.readLine().strip();
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Double.parseDouble(br.readLine());
        }

        Deque<Double> stack = new ArrayDeque<>();
        double result = 0;
        for (char c : s.toCharArray()) {
            if(Character.isAlphabetic(c)){
                stack.push(arr[c-'A']);
            }else{
                if (!stack.isEmpty()) {
                    double first = stack.pop();
                    double second = stack.pop();
                    switch (c){
                        case '+'  :
                            result = second + first;
                            stack.push(result);
                            break;
                        case '-':
                            result = second - first;
                            stack.push(result);
                            break;
                        case '*':
                            result = second * first;
                            stack.push(result);
                            break;
                        case '/':
                            result = second / first;
                            stack.push(result);
                            break;
                    }
                }
            }
        }
        System.out.printf("%.2f", stack.pop());
    }
}
