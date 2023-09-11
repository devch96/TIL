import java.util.*;

public class PG_Lv2_문자열_압축 {
    public static int solution(String s){
        int min = s.length();
        for (int i = 1; i <= s.length() / 2; i++) {
            Deque<String> deque = getCompressedStringQueue(s,i);
            min = Math.min(min, getCompressedLength(deque));
        }
        return min;
    }

    private static Deque<String> getCompressedStringQueue(String s, int compressedLength){
        Deque<String> queue = new ArrayDeque<>();
        String compressedString;
        for (int i = 0; i < s.length(); i += compressedLength) {
            if (i+compressedLength >= s.length()) {
                compressedString = s.substring(i, s.length());
            }else{
                compressedString = s.substring(i, i + compressedLength);
            }
            if (queue.isEmpty()) {
                queue.addFirst(compressedString);
                continue;
            }
            String temp = queue.peekFirst();
            if(isNumeric(temp)){
                String count = queue.pollFirst();
                String compress = queue.peekFirst();
                if (compress.equals(compressedString)) {
                    count = String.valueOf(Integer.parseInt(count) + 1);
                    queue.addFirst(count);
                }else{
                    queue.addFirst(count);
                    queue.addFirst(compressedString);
                }
            } else if (temp.equals(compressedString)) {
                queue.addFirst("2");
            }else{
                queue.addFirst(compressedString);
            }
        }
        return queue;
    }

    private static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static int getCompressedLength(Deque<String> deque){
        int result = 0;
        for (String s : deque) {
            result += s.length();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solution("aabbaccc"));
        System.out.println(solution("ababcdcdababcdcd"));
        System.out.println(solution("abcabcdede"));
        System.out.println(solution("abcabcabcabcdededededede"));
    }

}
