import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PG_Lv2_수식_최대화 {
    static List<Character> operators;
    static List<Long> operands;
    public static long solution(String expression){
        long answer = 0;
        operators = new ArrayList<>();
        operands = new ArrayList<>();
        int flag = 0;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '*' || c == '+' || c == '-') {
                operators.add(c);
                operands.add(Long.parseLong(expression.substring(flag, i)));
                flag = i+1;
            }
        }
        operands.add(Long.parseLong(expression.substring(flag)));

        answer = findMax('*', '+', '-', answer);
        answer = findMax('*', '-', '+', answer);
        answer = findMax('+', '*', '-', answer);
        answer = findMax('-', '*', '+', answer);
        answer = findMax('-', '+', '*', answer);
        answer = findMax('+', '-', '*', answer);

        return answer;
    }
    
    

    private static long findMax(Character first, Character second, Character third, long answer) {
        long result = 0;

        int idx = 0;
        List<Long> tempNum = operands.stream().collect(Collectors.toList());
        List<Character> tempCal = operators.stream().collect(Collectors.toList());
        while (!tempCal.isEmpty()) {
            if (tempCal.indexOf(first) != -1)
                idx = tempCal.indexOf(first);
            else if (tempCal.indexOf(second) != -1)
                idx = tempCal.indexOf(second);
            else
                idx = tempCal.indexOf(third);

            if (idx == -1)
                break;

            long front = tempNum.remove(idx);
            long end = tempNum.remove(idx);
            Character calc = tempCal.remove(idx);

            switch (calc) {
                case '*':
                    result = front * end;
                    break;
                case '+':
                    result = front + end;
                    break;
                case '-':
                    result = front - end;
                    break;
            }
            tempNum.add(idx, result);
        }

        return Math.max(answer, Math.abs(result));
    }
}
