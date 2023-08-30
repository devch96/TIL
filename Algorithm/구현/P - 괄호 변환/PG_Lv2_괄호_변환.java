import java.util.ArrayDeque;
import java.util.Deque;

public class PG_Lv2_괄호_변환 {
    public static String solution(String p) {
        return go(p);
    }

    private static String go(String p){
        if (p.isBlank()) {
            return p;
        }

        StringBuilder sb = new StringBuilder();
        String[] temp = splitString(p);
        String u = temp[0];
        String v = temp[1];
        if(isCorrectBracketString(u)){
            return sb.append(u).append(go(v)).toString();
        }else{
            return sb.append("(").append(go(v)).append(")").append(reverseString(u)).toString();
        }
    }

    private static String[] splitString(String p){
        int open = 0;
        int close = 0;
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) == '(') {
                open++;
            }else{
                close++;
            }
            if(open==close){
                String u = p.substring(0, i + 1);
                String v = "";
                if (p.length() >= i + 1) {
                    v = p.substring(i + 1);
                }
                return new String[] {u, v};
            }
        }
        return new String[]{"", ""};
    }

    private static boolean isCorrectBracketString(String u){
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : u.toCharArray()) {
            if (c == '(') {
                stack.addLast(c);
            }else{
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pollLast();
            }
        }
        return stack.isEmpty();
    }

    private static String reverseString(String u){
        u = u.substring(1,u.length()-1);
        StringBuilder sb = new StringBuilder();
        for (char c : u.toCharArray()) {
            if (c == '(') {
                sb.append(')');
            } else {
                sb.append('(');
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(go("(()())()"));
        System.out.println(go(")("));
        System.out.println(go("()))((()"));
    }
}
