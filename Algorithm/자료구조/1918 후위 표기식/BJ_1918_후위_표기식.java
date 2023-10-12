import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_1918_후위_표기식 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine().strip();
        StringBuilder sb = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : str.toCharArray()) {
            if(Character.isAlphabetic(c)){
                sb.append(c);
                continue;
            }
            switch (c){
                case '+':
                case '-':
                case '*':
                case '/':
                    while(!stack.isEmpty() && operatorPriority(stack.peek()) >= operatorPriority(c)){
                        sb.append(stack.pop());
                    }
                    stack.push(c);
                    break;
                case '(':
                    stack.push(c);
                    break;
                case ')':
                    while(!stack.isEmpty() && stack.peek() != '('){
                        sb.append(stack.pop());
                    }
                    stack.pop();
                    break;
            }
        }

        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        System.out.println(sb);
    }

    private static int operatorPriority(char operator){
        if(operator == '(' || operator == ')'){
            return 0;
        }else if (operator == '+' || operator == '-') {
            return 1;
        }else{
            return 2;
        }
    }
}
