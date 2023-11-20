import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1062_가르침 {
    static String[] words;
    static boolean[] learned;
    static int n;
    static int k;
    static int maxValue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        words = new String[n];
        learned = new boolean[26];
        maxValue = Integer.MIN_VALUE;
        if(k < 5){
            System.out.println(0);
            return;
        } else if (k == 26) {
            System.out.println(n);
            return;
        }
        learnedBasicWord();
        for (int i = 0; i < n; i++) {
            words[i] = sliceBasicWord(br.readLine());
        }
        backtrack(0,5);
        System.out.println(maxValue);
    }

    private static void learnedBasicWord(){
        learned['a' - 97] = true;
        learned['n' - 97] = true;
        learned['t' - 97] = true;
        learned['i' - 97] = true;
        learned['c' - 97] = true;
    }

    private static String sliceBasicWord(String s){
        s = s.substring(4);
        s = s.substring(0, s.length() - 4);
        return s;
    }


    private static void backtrack(int start, int depth){
        if(depth == k){
            maxValue = Math.max(maxValue,canReadWords());
            return;
        }

        for (int i = start; i < 26; i++) {
            if (!learned[i]) {
                learned[i] = true;
                backtrack(i+1, depth+1);
                learned[i] = false;
            }
        }
    }

    private static int canReadWords(){
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (canRead(words[i])) {
                result++;
            }
        }
        return result;
    }

    private static boolean canRead(String str){
        for (char c : str.toCharArray()) {
            if (!learned[c - 97]) {
                return false;
            }
        }
        return true;
    }

}
