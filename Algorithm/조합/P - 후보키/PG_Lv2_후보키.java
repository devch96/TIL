import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PG_Lv2_후보키 {
    static String[][] table;
    static List<Set<Character>> candidateKeys;

    public static int solution(String[][] relation){
        table = relation;
        candidateKeys = new ArrayList<>();
        if(relation[0].length == 1) {
            return isUnique(Set.of('0')) ? 1 : 0;
        }

        String columns = "";
        for(int i = 0; i < relation[0].length; i++) {
            columns += i;
        }
        for(int i = 1; i < relation[0].length + 1; i++) {
            combination(columns, new HashSet<>(), i);
        }
        return candidateKeys.size();
    }
    private static void combination(String columns, Set<Character> out, int r) {
        if(r == 0 && isUnique(out) && isMinimal(out)) {
            candidateKeys.add(out);
            return;
        }
        for(int i = 0; i < columns.length(); i++) {
            Set<Character> newOut = new HashSet<>(out);
            newOut.add(columns.charAt(i));
            combination(columns.substring(i + 1), newOut, r - 1);
        }
    }
    private static boolean isUnique(Set<Character> key) {
        Set<String> set = new HashSet<>();
        for (String[] row : table) {
            String projection = "";
            for (char col : key) {
                projection += row[col - '0'];
            }
            if (set.contains(projection)) return false;
            else set.add(projection);
        }
        return true;
    }
    private static boolean isMinimal(Set<Character> key) {
        for(Set<Character> candidateKey: candidateKeys) {
            if(key.containsAll(candidateKey)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(solution(new String[][] { {"a","1","aaa","c","ng"},
                {"a","1","bbb","e","g"},
                {"c","1","aaa","d","ng"},
                {"d","2","bbb","d","ng"}}));
    }
}
