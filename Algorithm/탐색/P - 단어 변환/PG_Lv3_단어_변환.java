import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class PG_Lv3_단어_변환 {
    static Map<String, Integer> visited = new HashMap<>();
    static class Node {
        String str;
        int count;
        public Node(String str, int count) {
            this.str = str;
            this.count = count;
        }
    }
    public int solution(String begin, String target, String[] words) {
        int answer = getMinimumChange(begin, target, words);
        return answer;
    }

    private int getMinimumChange(String begin, String target, String[] words) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(begin, 0));
        visited.put(begin, 1);
        while(!queue.isEmpty()) {
            Node now = queue.poll();
            String nowStr = now.str;
            if(nowStr.equals(target)) {
                return now.count;
            }
            for(String nextCandidate : words) {
                if(!visited.containsKey(nextCandidate) && canChange(nowStr, nextCandidate)) {
                    visited.put(nextCandidate, 1);
                    queue.offer(new Node(nextCandidate, now.count+1));
                }
            }
        }
        return 0;
    }

    private boolean canChange(String now, String candidate) {
        int diffChar = 0;
        for(int i = 0; i < now.length(); i++) {
            if(now.charAt(i) != candidate.charAt(i)){
                diffChar++;
            }
        }
        return diffChar == 1;
    }
}
