import java.util.*;

public class PG_Lv2_메뉴_리뉴얼 {
    public static String[] solution(String[] orders, int[] course){
        Map<Integer, Map<String, Integer>> courseMap = new HashMap<>();
        List<String> answer = new ArrayList<>();
        for(int n : course){
            courseMap.put(n, new HashMap<>());
        }
        for(int n : course){
            for(String s : orders){
                if (n <= s.length()) {
                    List<String> comb = combination(sortString(s),n);
                    for(String temp : comb){
                        Map<String,Integer> tempMap = courseMap.get(n);
                        tempMap.put(temp, tempMap.getOrDefault(temp,0)+1);
                        courseMap.put(n, tempMap);
                    }
                }
            }
        }
        for(int n : course){
            int max = 0;
            Map<String, Integer> tempMap = courseMap.get(n);
            for (String key : tempMap.keySet()) {
                max = Math.max(max, tempMap.get(key));
            }
            if(max < 2){
                continue;
            }
            for (String key : tempMap.keySet()) {
                if(tempMap.get(key) == max){
                    answer.add(key);
                }
            }
        }
        Collections.sort(answer);
        return answer.toArray(String[]::new);
    }

    private static String sortString(String s){
        char[] arr = s.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }
    private static List<String> combination(String s, int n) {
        List<String> result = new ArrayList<>();
        combinationHelper(s, n, 0, "", result);
        return result;
    }

    private static void combinationHelper(String s, int n, int start, String current, List<String> result) {
        if (n == 0) {
            result.add(current);
            return;
        }

        for (int i = start; i <= s.length() - n; i++) {
            combinationHelper(s, n - 1, i + 1, current + s.charAt(i), result);
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new String[] {"XYZ", "XWY", "WXA"}, new int[] {2,3,4})));
    }
}
