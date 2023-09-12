import org.w3c.dom.ls.LSOutput;

import java.util.*;

public class PG_Lv2_광물_캐기 {

    public static int solution(int[] picks, String[] minerals){
        int pickSum = Arrays.stream(picks).sum();
        List<int[]> fatigues = getFatigue(minerals, pickSum);
        int answer = 0;
        Collections.sort(fatigues, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if(o1[2] == o2[2]){
                    return Integer.compare(o2[1], o1[1]);
                }else{
                    return Integer.compare(o2[2], o1[2]);
                }
            }
        });
        for (int i = 0; i < picks.length; i++) {
            int pickCount = picks[i];

            while(pickCount>0){
                if (fatigues.isEmpty()) {
                    return answer;
                }
                answer += fatigues.remove(0)[i];
                pickCount--;
            }
        }
        return answer;
    }

    private static List<int[]> getFatigue(String[] minerals, int pickSum){
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < minerals.length; i += 5) {
            if (result.size() == pickSum) {
                return result;
            }
            int[] fatigues = new int[3];
            int max = i + 5;
            if (i + 5 > minerals.length) {
                max = minerals.length;
            }
            for (int j = i; j < max; j++) {
                switch (minerals[j]){
                    case "diamond":
                        fatigues[0] += 1;
                        fatigues[1] += 5;
                        fatigues[2] += 25;
                        break;
                    case "iron":
                        fatigues[0] += 1;
                        fatigues[1] += 1;
                        fatigues[2] += 5;
                        break;
                    case "stone":
                        fatigues[0] += 1;
                        fatigues[1] += 1;
                        fatigues[2] += 1;
                        break;
                }
            }
            result.add(fatigues);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[] {1,0,1}, new String[] {"iron", "iron", "iron", "iron", "diamond", "diamond", "diamond"}));
    }
}
