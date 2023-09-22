import java.util.*;

public class PG_Lv2_순위_검색 {
    static Map<String, List<Integer>> infos;

    public static int[] solution(String[] info, String[] query){
        infos = new HashMap<>();
        putData(info);
        int[] answer = new int[query.length];
        for (String key : infos.keySet()) {
            Collections.sort(infos.get(key));
        }
        for (int i = 0; i < answer.length; i++) {
            String[] parsingQuery = parsingQuery(query[i]);
            String conditionQuery = String.join("",Arrays.copyOfRange(parsingQuery,0,4));
            String scoreQuery = parsingQuery[4];
            List<Integer> scores = infos.get(conditionQuery);
            if(scores == null){
                answer[i] = 0;
                continue;
            }
            int idx = binarySearch(scores, Integer.parseInt(scoreQuery));
            answer[i] = scores.size() - idx;
        }
        return answer;
    }

    private static void putData(String[] info){
        for(String s : info){
            String[] temp = s.split(" ");
            String[] conditions = Arrays.copyOfRange(temp, 0, 4);
            List<List<String>> combinations = generateCombinations(conditions);
            for (List<String> combination : combinations) {
                String result = replaceWithDash(conditions, combination);
                List<Integer> scores = infos.getOrDefault(result, new ArrayList<>());
                scores.add(Integer.parseInt(temp[4]));
                infos.put(result, scores);
            }
        }
    }

    private static List<List<String>> generateCombinations(String[] arr) {
        List<List<String>> combinations = new ArrayList<>();
        generateCombinations(arr, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    // 재귀 함수로 조합을 생성하는 함수
    private static void generateCombinations(String[] arr, int index, List<String> current, List<List<String>> combinations) {
        if (index == arr.length) {
            combinations.add(new ArrayList<>(current));
            return;
        }

        // 현재 원소를 선택하지 않는 경우
        generateCombinations(arr, index + 1, current, combinations);

        // 현재 원소를 선택하는 경우
        current.add(arr[index]);
        generateCombinations(arr, index + 1, current, combinations);
        current.remove(current.size() - 1); // 원상 복구
    }

    // 선택된 문자열을 '-'로 대체하는 함수
    private static String replaceWithDash(String[] arr, List<String> selected) {
        String[] result = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (selected.contains(arr[i])) {
                result[i] = "-";
            } else {
                result[i] = arr[i];
            }
        }
        return String.join("",result);

    }

    private static String[] parsingQuery(String query){
        return query.split(" and | ");
    }

    private static int binarySearch(List<Integer> list, int target){
        int start = 0;
        int end = list.size()-1;
        while(start <= end){
            int middle = start + (end - start)/2;
            int middleValue = list.get(middle);
            if(middleValue < target){
                start = middle+1;
            }else{
                end = middle-1;
            }
        }
        return start;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new String[] {"java backend junior pizza 150","python frontend senior chicken 210","python frontend senior chicken 150","cpp backend senior pizza 260","java backend junior chicken 80","python backend senior chicken 50"}
        , new String[] {"java and backend and junior and pizza 100","python and frontend and senior and chicken 200","cpp and - and senior and pizza 250","- and backend and senior and - 150","- and - and - and chicken 100","- and - and - and - 150"})));
    }
}
