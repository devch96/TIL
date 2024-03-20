import java.util.*;

public class PG_Lv3_보석_쇼핑 {
    public int[] solution(String[] gems) {
        Map<String, Integer> gemIndex = new HashMap<>();
        Set<String> gem = new HashSet<>(Arrays.asList(gems));
        int[] answer = new int[2];
        int total = gem.size();
        int length = Integer.MAX_VALUE;
        int L = 0;
        for (int R = 0; R < gems.length; R++) {
            gemIndex.compute(gems[R], (k, v) -> v == null ? 1 : v+1);
            while(gemIndex.get(gems[L]) > 1) {
                gemIndex.put(gems[L], gemIndex.get(gems[L]) - 1);
                L++;
            }
            if(gemIndex.size() == total && length > (R - L)) {
                length = R - L;
                answer[0] = L + 1;
                answer[1] = R + 1;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        PG_Lv3_보석_쇼핑 p = new PG_Lv3_보석_쇼핑();
        p.solution(new String[]{"A","B","B","B","C","D","D","D","D","D","D","D","B","C","A"});

    }
}
