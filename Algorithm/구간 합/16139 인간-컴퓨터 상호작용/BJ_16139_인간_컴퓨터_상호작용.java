import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_16139_인간_컴퓨터_상호작용 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine().strip();
        int n = Integer.parseInt(br.readLine());

        int[][] arr = new int[s.length()+1][26];
        arr[1][s.charAt(0) -'a']++;
        for (int i = 2; i <= s.length(); i++) {
            int idx = s.charAt(i-1)-'a';
            for (int j = 0; j < 26; j++) {
                int before = arr[i-1][j];
                arr[i][j] = idx == j ? before + 1 : before;
            }
        }
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String q = st.nextToken();
            int start = Integer.parseInt(st.nextToken()) + 1;
            int end = Integer.parseInt(st.nextToken()) + 1;
            int idx = q.charAt(0) - 97;
            System.out.println(arr[end][idx] - arr[start-1][idx]);
        }

    }

}
