import java.util.ArrayList;
import java.util.List;

public class PG_Lv2_혼자_놀기의_달인 {
    static boolean[] visited;

    public static int solution(int[] cards){
        int max = Integer.MIN_VALUE;
        for(int start : cards){
            visited = new boolean[cards.length+1];
            int first = playing(cards, start);
            if(first == 0){
                return 0;
            }
            for(int secondStart : cards){
                if (!visited[secondStart]) {
                    int second = playing(cards, secondStart);
                    max = Math.max(max,first * second);
                }
            }
        }
        return max;
    }

    private static int playing(int[] cards, int start){
        List<Integer> result = new ArrayList<>();
        visited[start-1] = true;
        result.add(start-1);
        int togo = cards[start-1];
        while (!visited[togo-1]) {
            visited[togo-1] = true;
            result.add(togo-1);
            togo = cards[togo-1];
        }
        return result.size() == cards.length ? 0 : result.size();
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[] {8,6,3,7,2,5,1,4}));
        System.out.println(solution(new int[] {2,3,4,5,6,7,8,9,10,1}));
    }
}
