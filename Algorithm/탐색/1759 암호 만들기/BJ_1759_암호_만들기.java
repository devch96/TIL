import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1759_암호_만들기 {
    static String[] letters;
    static List<Character> vowelList = List.of('a','e','i','o','u');
    static StringBuilder sb;
    static int l;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());
        l = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        letters = new String[c];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < c; i++) {
            letters[i] = st.nextToken();
        }
        Arrays.sort(letters);
        backtrack("",0,0);
        bw.write(sb.toString());
        bw.flush();
    }

    private static void backtrack(String s, int start, int depth){
        if(depth == l && checkRule(s)){
            sb.append(s);
            sb.append("\n");
            return;
        }

        for (int i = start; i < letters.length; i++) {
            backtrack(s.concat(letters[i]), i+1, depth+1);
        }
    }

    private static boolean checkRule(String code){
        int vowel = 0;
        int constant = 0;
        for (char c : code.toCharArray()) {
            if(vowelList.contains(c)){
                vowel++;
            }else{
                constant++;
            }
        }
        return vowel >= 1 && constant >= 2;
    }


}
