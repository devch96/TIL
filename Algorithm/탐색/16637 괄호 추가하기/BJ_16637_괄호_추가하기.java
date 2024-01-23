import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BJ_16637_괄호_추가하기 {
    static int answer;
    static List<Character> operations;
    static List<Integer> numbers;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        String input = br.readLine();
        operations = new ArrayList<>();
        numbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            char c = input.charAt(i);
            if (c == '+' || c == '-' || c == '*') {
                operations.add(c);
            }else{
                numbers.add(Character.getNumericValue(c));
            }
        }
        answer = Integer.MIN_VALUE;
        dfs(numbers.get(0), 0);
        System.out.println(answer);
    }

    private static void dfs(int result, int operationIndex) {
        if (operationIndex >= operations.size()) {
            answer = Math.max(answer, result);
            return;
        }
        int res1 = calc(operations.get(operationIndex), result, numbers.get(operationIndex + 1));
        dfs(res1, operationIndex + 1);

        if (operationIndex + 1 < operations.size()) {
            int res2 = calc(operations.get(operationIndex + 1), numbers.get(operationIndex + 1), numbers.get(operationIndex + 2));
            dfs(calc(operations.get(operationIndex), result, res2), operationIndex + 2);
        }
    }

    private static int calc(char op, int n1, int n2) {
        switch (op) {
            case '+':
                return n1 + n2;
            case '-':
                return n1 - n2;
            case '*':
                return n1 * n2;
        }
        return -1;
    }
}
