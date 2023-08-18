import java.util.*;
import java.util.stream.Collectors;

public class PG_Lv2_큰_수_만들기 {
    public static String solution(String number, int k){
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : number.toCharArray()) {
            while (!stack.isEmpty() && stack.peekLast() < c && k > 0) {
                k--;
                stack.removeLast();
            }
            stack.addLast(c);
        }
        if (k!=0){
            stack = substring(stack, k);
        }
        return stack.stream().map(Object::toString).collect(Collectors.joining());
    }

    private static Deque<Character> substring(Deque<Character> stack, int k){
        Deque<Character> result = new ArrayDeque<>();
        int size = stack.size();
        for (int i = 0; i < size - k ; i++) {
            result.addLast(stack.removeFirst());
        }
        return result;
    }

    public static void main(String[] args) {
//        System.out.println(solution("1924",2));
//        System.out.println(solution("1231234",3));
//        System.out.println(solution("4177252841",4));
        System.out.println(solution("987654321",5));
    }
}
