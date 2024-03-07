import java.util.Map;
import java.util.TreeMap;

public class PG_Lv3_이중우선순위큐 {
    public int[] solution(String[] operations) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        for(String operation : operations) {
            String[] op = operation.split(" ");
            String command = op[0];
            int direction = Integer.parseInt(op[1]);
            switch(command) {
                case "I":
                    treeMap.compute(direction, (key, value) -> value == null ? 1 : value + 1);
                    break;
                case "D":
                    if (!treeMap.isEmpty()) {
                        if(direction == -1){
                            Map.Entry<Integer, Integer> entry = treeMap.pollFirstEntry();
                            if(entry.getValue() != 1){
                                treeMap.put(entry.getKey(), entry.getValue() - 1);
                            }
                        }else{
                            Map.Entry<Integer, Integer> entry = treeMap.pollLastEntry();
                            if(entry.getValue() != 1){
                                treeMap.put(entry.getKey(), entry.getValue() - 1);
                            }
                        }
                    }
            }
        }
        int[] answer = new int[2];
        Map.Entry<Integer, Integer> smallestEntry = treeMap.pollFirstEntry();
        Map.Entry<Integer, Integer> largestEntry = treeMap.pollLastEntry();
        if (smallestEntry != null) {
            answer[1] = smallestEntry.getKey();
        }
        if(largestEntry == null) {
            answer[0] = answer[1];
        }else{
            answer[0] = largestEntry.getKey();
        }
        return answer;
    }

    public static void main(String[] args) {
        PG_Lv3_이중우선순위큐 a = new PG_Lv3_이중우선순위큐();
        a.solution(new String[]{"I 16", "I -5643", "D -1", "D 1", "D 1", "I 123", "D -1"});
    }
}
